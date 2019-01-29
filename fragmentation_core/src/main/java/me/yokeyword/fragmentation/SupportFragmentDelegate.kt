package me.yokeyword.fragmentation

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.FragmentationMagician
import me.yokeyword.fragmentation.Contracts.FRAGMENTATION_ARG_CONTAINER
import me.yokeyword.fragmentation.Contracts.FRAGMENTATION_ARG_CUSTOM_ENTER_ANIM
import me.yokeyword.fragmentation.Contracts.FRAGMENTATION_ARG_CUSTOM_EXIT_ANIM
import me.yokeyword.fragmentation.Contracts.FRAGMENTATION_ARG_CUSTOM_POP_EXIT_ANIM
import me.yokeyword.fragmentation.Contracts.FRAGMENTATION_ARG_IS_SHARED_ELEMENT
import me.yokeyword.fragmentation.Contracts.FRAGMENTATION_ARG_REPLACE
import me.yokeyword.fragmentation.Contracts.FRAGMENTATION_ARG_RESULT_RECORD
import me.yokeyword.fragmentation.Contracts.FRAGMENTATION_ARG_ROOT_STATUS
import me.yokeyword.fragmentation.Contracts.FRAGMENTATION_STATE_SAVE_ANIMATOR
import me.yokeyword.fragmentation.Contracts.FRAGMENTATION_STATE_SAVE_IS_HIDDEN
import me.yokeyword.fragmentation.anim.FragmentAnimator
import me.yokeyword.fragmentation.helper.internal.AnimatorHelper
import me.yokeyword.fragmentation.helper.internal.ResultRecord
import me.yokeyword.fragmentation.helper.internal.TransactionRecord
import me.yokeyword.fragmentation.helper.internal.VisibleDelegate

class SupportFragmentDelegate(private val support: ISupportFragment) {

    private val fragment: Fragment

    private lateinit var supportActivity: ISupportActivity
    private lateinit var activity: FragmentActivity

    private lateinit var delegate: TransactionDelegate
    private lateinit var transaction: Transaction
    private lateinit var childTransaction: Transaction

    init {
        if (support !is Fragment) {
            throw RuntimeException("Must extends Fragment")
        }
        fragment = support
    }

    private var rootStatus = Status.UN_ROOT
    private var isSharedElement = false
    var containerId = 0
    private var replaceMode = false
    private var customEnterAnim = Int.MIN_VALUE
    private var customExitAnim = Int.MIN_VALUE
    private var customPopExitAnim = Int.MIN_VALUE

    private var isHidden = true
    private var saveInstanceState: Bundle? = null

    private var rootViewClickable = false
    private var firstCreateView = true
    private lateinit var animator: FragmentAnimator

    var transactionRecord: TransactionRecord? = null
    var lockAnim = false
    var newBundle: Bundle? = null
    var animByActivity = true
    val animHelper by lazy { AnimatorHelper(activity.applicationContext, animator) }
    val visibleDelegate by lazy { VisibleDelegate(support) }

    private val handler by lazy { Handler(Looper.getMainLooper()) }

    private val notifyEnterAnimEndRunnable = object : Runnable {
        override fun run() {
            support.onEnterAnimationEnd(saveInstanceState)

            if (rootViewClickable) return

            val view = fragment.view ?: return
            val pre = fragment.getPreFragment() ?: return

            val duration = pre.supportDelegate.getExitAnimDuration()
            val enter = animHelper.loadAnimation(customEnterAnim) ?: animHelper.enterAnim

            handler.postDelayed({
                view.isClickable = false
            }, duration - enter.duration)
        }
    }

    fun extraTransaction(): ExtraTransaction {
        return ExtraTransactionImpl(activity, support, delegate, false)
    }

    fun onAttach(activity: FragmentActivity) {
        if (activity !is ISupportActivity) {
            throw RuntimeException("Must implements ISupportActivity")
        }
        this.supportActivity = activity
        this.activity = activity
        this.delegate = activity.supportDelegate.getTransactionDelegate()

        this.transaction = TransactionImpl(support, fragment.fragmentManager, delegate)
        this.childTransaction = TransactionImpl(support, fragment.childFragmentManager, delegate)
    }

    fun onCreate(savedInstanceState: Bundle?) {
        visibleDelegate.onCreate(savedInstanceState)

        fragment.arguments?.let {
            rootStatus = it.getSerializable(FRAGMENTATION_ARG_ROOT_STATUS) as? Status ?: Status.UN_ROOT
            isSharedElement = it.getBoolean(FRAGMENTATION_ARG_IS_SHARED_ELEMENT, false)
            containerId = it.getInt(FRAGMENTATION_ARG_CONTAINER, 0)
            replaceMode = it.getBoolean(FRAGMENTATION_ARG_REPLACE, false)

            customEnterAnim = it.getInt(FRAGMENTATION_ARG_CUSTOM_ENTER_ANIM, Int.MIN_VALUE)
            customExitAnim = it.getInt(FRAGMENTATION_ARG_CUSTOM_EXIT_ANIM, Int.MIN_VALUE)
            customPopExitAnim = it.getInt(FRAGMENTATION_ARG_CUSTOM_POP_EXIT_ANIM, Int.MIN_VALUE)
        }

        var bakAnim: FragmentAnimator? = null
        if (savedInstanceState != null) {
            saveInstanceState = savedInstanceState
            bakAnim = savedInstanceState.getParcelable(FRAGMENTATION_STATE_SAVE_ANIMATOR)
            isHidden = savedInstanceState.getBoolean(FRAGMENTATION_STATE_SAVE_IS_HIDDEN)
            containerId = savedInstanceState.getInt(FRAGMENTATION_ARG_CONTAINER)

            // RootFragment
            if (rootStatus != Status.UN_ROOT) {
                FragmentationMagician.reorderIndices(fragment.fragmentManager)
            }
        } else {
            support.onCreateFragmentAnimator()
        }
        animator = bakAnim ?: support.onCreateFragmentAnimator()

        // Fix the overlapping BUG on pre-24.0.0
        processRestoreInstanceState(savedInstanceState)

        val enter = animHelper.loadAnimation(customEnterAnim) ?: return
        enter.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                supportActivity.supportDelegate.fragmentClickable = false
                handler.postDelayed({
                    supportActivity.supportDelegate.fragmentClickable = true
                }, enter.duration)

            }

            override fun onAnimationEnd(animation: Animation?) {

            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
    }

    fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (supportActivity.supportDelegate.popMultipleNoAnim || lockAnim) {
            if (transit == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE && enter) {
                return animHelper.noneAnimFixed
            }
            return animHelper.noneAnim
        }
        when(transit) {
            FragmentTransaction.TRANSIT_FRAGMENT_OPEN -> {
                return if (enter) {
                    if (rootStatus == Status.ROOT_ANIM_DISABLE) {
                        animHelper.noneAnim
                    } else {
                        val enterAnim = animHelper.enterAnim
                        fixAnimationListener(enterAnim)
                        enterAnim
                    }
                } else {
                    animHelper.popExitAnim
                }
            }
            FragmentTransaction.TRANSIT_FRAGMENT_CLOSE -> {
                return if (enter) animHelper.popEnterAnim else animHelper.exitAnim
            }
            else -> {
                if (isSharedElement && enter) {
                    notifyEnterAnimEnd()
                }

                if (!enter) {
                    return animHelper.compatChildFragmentExitAnim(fragment)
                }

                return null
            }
        }
    }

    fun onSaveInstanceState(outState: Bundle) {
        visibleDelegate.onSaveInstanceState(outState)
        outState.putParcelable(FRAGMENTATION_STATE_SAVE_ANIMATOR, animator)
        outState.putBoolean(FRAGMENTATION_STATE_SAVE_IS_HIDDEN, fragment.isHidden)
        outState.putInt(FRAGMENTATION_ARG_CONTAINER, containerId)
    }

    fun onActivityCreated(savedInstanceState: Bundle?) {
        visibleDelegate.onActivityCreated()

        val view = fragment.view
        if (view != null) {
            rootViewClickable = view.isClickable
            view.isClickable = true
            setBackground(view)
        }

        if (savedInstanceState != null
            || rootStatus == Status.ROOT_ANIM_DISABLE
            || fragment.tag?.startsWith("android:switcher:") == true
            || (replaceMode && !firstCreateView)) {
            notifyEnterAnimEnd()
        } else if (customEnterAnim != Int.MIN_VALUE) {
            fixAnimationListener(if (customEnterAnim == 0) animHelper.noneAnim else animHelper.loadAnimation(customExitAnim)!!)
        }

        if (firstCreateView) {
            firstCreateView = false
        }
    }

    fun onResume() {
        visibleDelegate.onResume()
    }

    fun onPause() {
        visibleDelegate.onPause()
    }

    fun onDestroyView() {
        supportActivity.supportDelegate.fragmentClickable = true
        visibleDelegate.onDestroyView()
        handler.removeCallbacks(notifyEnterAnimEndRunnable)
    }

    fun onDestroy() {
        delegate.handleResultRecord(fragment)

    }

    fun onHiddenChanged(hidden: Boolean) {
        visibleDelegate.onHiddenChanged(hidden)
    }

    fun setUserVisibleHint(isVisibleToUser: Boolean) {
        visibleDelegate.setUserVisibleHint(isVisibleToUser)
    }

    /**
     * Causes the Runnable r to be added to the action queue.
     * <p>
     * The runnable will be run after all the previous action has been run.
     * <p>
     * 前面的事务全部执行后 执行该Action
     */
    fun post(runnable: Runnable) = delegate.post(runnable)

    /**
     * Called when the enter-animation end.
     * 入栈动画 结束时,回调
     */
    fun onEnterAnimationEnd(savedInstanceState: Bundle?) {}


    /**
     * Lazy initial，Called when fragment is first visible.
     *
     *
     * 同级下的 懒加载 ＋ ViewPager下的懒加载  的结合回调方法
     */
    fun onLazyInitView(savedInstanceState: Bundle?) {}

    /**
     * Called when the fragment is visible.
     *
     *
     * 当Fragment对用户可见时回调
     *
     *
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    fun onSupportVisible() {}

    /**
     * Called when the fragment is invivible.
     *
     *
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    fun onSupportInvisible() {}

    /**
     * Return true if the fragment has been supportVisible.
     */
    fun isSupportVisible() = visibleDelegate.isSupportVisible()

    /**
     * Set fragment animation with a higher priority than the ISupportActivity
     * 设定当前Fragment动画,优先级比在ISupportActivity里高
     */
    fun onCreateFragmentAnimator(): FragmentAnimator = supportActivity.fragmentAnimator

    fun getFragmentAnimator(): FragmentAnimator {
        return animator
    }

    fun setFragmentAnimator(animator: FragmentAnimator, animByActivity: Boolean = false) {
        this.animator = animator
        animHelper.notifyChanged(animator)
        this.animByActivity = animByActivity
    }

    /**
     * 类似 {@link Activity#setResult(int, Intent)}
     * <p>
     * Similar to {@link Activity#setResult(int, Intent)}
     *
     * @see #startForResult(ISupportFragment, int)
     */
    fun setFragmentResult(resultCode: Int, bundle: Bundle?) {
        val args = fragment.arguments
        if (args == null || !args.containsKey(FRAGMENTATION_ARG_RESULT_RECORD)) return

        val record: ResultRecord = args.getParcelable(FRAGMENTATION_ARG_RESULT_RECORD) ?: return
        record.resultCode = resultCode
        record.resultBundle = bundle
    }

    /**
     * 类似 [Activity.onActivityResult]
     * <p>
     * Similar to [Activity.onActivityResult]
     *
     * @see #startForResult
     */
    fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {}

    /**
     * 在start(TargetFragment,LaunchMode)时,启动模式为SingleTask/SingleTop, 回调TargetFragment的该方法
     * 类似 [Activity.onNewIntent]
     *
     *
     * Similar to [Activity.onNewIntent]
     *
     * @param args putNewBundle(Bundle newBundle)
     * @see .start
     */
    fun onNewBundle(args: Bundle?) {}

    /**
     * 添加NewBundle,用于启动模式为SingleTask/SingleTop时
     *
     * @see .start
     */
    fun putNewBundle(newBundle: Bundle?) {
        this.newBundle = newBundle
    }

    /**
     * Back Event
     *
     * @return false则继续向上传递, true则消费掉该事件
     */
    fun onBackPressedSupport(): Boolean {
        return false
    }

    /**
     * 隐藏软键盘
     */
    fun hideSoftInput() {
        val activity = fragment.activity ?: return
        activity.window.decorView.hideSoftInput()
    }

    /**
     * 显示软键盘,调用该方法后,会在onPause时自动隐藏软键盘
     */
    fun showSoftInput(view: View) {
        view.showSoftInput()
    }

    fun loadRootFragment(containerId: Int, to: ISupportFragment,
                         addToBackStack: Boolean = true,
                         allowAnim: Boolean = false) {
        childTransaction.loadRootFragment(containerId, to, addToBackStack, allowAnim)
    }

    fun <T : ISupportFragment> loadMultipleRootFragment(containerId: Int,
                                                        showPosition: Int,
                                                        fragments: Array<T?>) {
        childTransaction.loadMultipleRootFragment(containerId, showPosition, fragments)
    }

    fun start(to: ISupportFragment, requestCode: Int = 0, mode: Transaction.LaunchMode = Transaction.LaunchMode.STANDARD) {
        transaction.start(to, requestCode, mode)
    }

    fun startDontHideSelf(to: ISupportFragment, requestCode: Int = 0, mode: Transaction.LaunchMode = Transaction.LaunchMode.STANDARD) {
        transaction.startDontHideSelf(to, requestCode, mode)
    }

    fun startWithPop(to: ISupportFragment) {
        transaction.startWithPop(to)
    }

    fun startWithPopTo(to: ISupportFragment, tag: String, self: Boolean) {
        transaction.startWithPopTo(to, tag, self)
    }

    fun pop() {
        transaction.pop()
    }

    fun popTo(tag: String, self: Boolean, runnable: Runnable? = null, popAnim: Int = TransactionDelegate.DEFAULT_POPTO_ANIM ) {
        transaction.popTo(tag, self, runnable, popAnim)
    }

    fun popQuiet() {
        transaction.popQuiet()
    }

    fun removeFragment(fragment: ISupportFragment, showPre: Boolean = true) {
        transaction.removeFragment(fragment, showPre)
    }

    fun replaceFragment(to: ISupportFragment, addToBackStack: Boolean = true) {
        transaction.replaceFragment(to, addToBackStack)
    }

    fun showHideFragment(show: ISupportFragment, hide: ISupportFragment? = null) {
        transaction.showHideFragment(show, hide)
    }


    fun startChild(to: ISupportFragment, requestCode: Int = 0, mode: Transaction.LaunchMode = Transaction.LaunchMode.STANDARD) {
        childTransaction.start(to, requestCode, mode)
    }

    fun startChildDontHideSelf(to: ISupportFragment, requestCode: Int = 0, mode: Transaction.LaunchMode = Transaction.LaunchMode.STANDARD) {
        childTransaction.startDontHideSelf(to, requestCode, mode)
    }

    fun startChildWithPop(to: ISupportFragment) {
        childTransaction.startWithPop(to)
    }

    fun startChildWithPopTo(to: ISupportFragment, tag: String, self: Boolean) {
        childTransaction.startWithPopTo(to, tag, self)
    }

    fun popChild() {
        childTransaction.pop()
    }

    fun popToChild(tag: String, self: Boolean, runnable: Runnable? = null, popAnim: Int) {
        childTransaction.popTo(tag, self, runnable, popAnim)
    }

    fun popQuietChild() {
        childTransaction.popQuiet()
    }

    fun removeChildFragment(fragment: ISupportFragment, showPre: Boolean = true) {
        childTransaction.removeFragment(fragment, showPre)
    }

    fun replaceChildFragment(to: ISupportFragment, addToBackStack: Boolean = true) {
        childTransaction.replaceFragment(to, addToBackStack)
    }

    fun showHideChildFragment(show: ISupportFragment, hide: ISupportFragment? = null) {
        childTransaction.showHideFragment(show, hide)
    }

    //

    private fun processRestoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val ft = fragment.fragmentManager?.beginTransaction() ?: return

            if (isHidden) {
                ft.hide(fragment)
            } else {
                ft.show(fragment)
            }
            ft.commitAllowingStateLoss()
        }
    }

    private fun fixAnimationListener(enter: Animation) {
        // AnimationListener is not reliable.
        handler.postDelayed(notifyEnterAnimEndRunnable, enter.duration)
        supportActivity.supportDelegate.fragmentClickable = true

        if (animListener != null) {
            handler.post {
                animListener?.onStart()
                animListener = null
            }
        }
    }

    private fun notifyEnterAnimEnd() {
        handler.post(notifyEnterAnimEndRunnable)
        supportActivity.supportDelegate.fragmentClickable = true
    }

    private fun getWindowBackground(): Int {
        val a = activity.theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground))
        val background = a.getResourceId(0, 0)
        a.recycle()
        return background
    }

    fun setBackground(view: View) {
        if (fragment.tag?.startsWith("android:switcher:") == true
            || rootStatus != Status.UN_ROOT
            || view.background != null) {
            return
        }

        var default = supportActivity.supportDelegate.defaultFragmentBackground
        if (default == 0) default = getWindowBackground()
        view.setBackgroundResource(default)
    }

    fun getExitAnim(): Animation {
        return animHelper.loadAnimation(customExitAnim) ?: animHelper.exitAnim
    }

    internal fun getExitAnimDuration(): Long {
        return getExitAnim().duration
    }

    private var animListener: EnterAnimListener? = null

    fun setEnterAnimListener(listener: EnterAnimListener?) {
        animListener = listener
    }

    interface EnterAnimListener {
        fun onStart()
    }

    enum class Status {
        UN_ROOT,
        ROOT_ANIM_DISABLE,
        ROOT_ANIM_ENABLE
    }

    companion object {
        private const val NOT_FOUND_ANIM_TIME = 300L
    }
}