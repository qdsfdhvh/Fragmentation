package me.yokeyword.fragmentation

import android.os.Bundle
import android.view.MotionEvent
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import me.yokeyword.fragmentation.anim.FragmentAnimator
import me.yokeyword.fragmentation.debug.DebugStackDelegate
import me.yokeyword.fragmentation.queue.Action

class SupportActivityDelegate(private val support: ISupportActivity) {

    private val activity: FragmentActivity

    init {
        if (support !is FragmentActivity) {
            throw RuntimeException("Must extends FragmentActivity/AppCompatActivity")
        }
        activity = support
    }

    var popMultipleNoAnim = false
    var fragmentClickable = true

    private lateinit var fragmentAnimator: FragmentAnimator
    private val debugStackDelegate by lazy { DebugStackDelegate(activity) }
    val delegate by lazy { TransactionDelegate(support) }

    /**
     * 当Fragment根布局 没有 设定background属性时,
     * Fragmentation默认使用Theme的android:windowbackground作为Fragment的背景,
     * 可以通过该方法改变Fragment背景。
     */
    @DrawableRes var defaultFragmentBackground = 0

    fun onCreate(savedInstanceState: Bundle?) {
        fragmentAnimator = support.onCreateFragmentAnimator()
        debugStackDelegate.onCreate(Fragmentation.getDefault().getMode())
    }

    fun onPostCreate(savedInstanceState: Bundle?) {
        debugStackDelegate.onPostCreate(Fragmentation.getDefault().getMode())
    }

    fun onBackPressed() {
        delegate.enqueue(object : Action(Type.BACK) {
            override fun run() {
                if (!fragmentClickable) fragmentClickable = true

                val active = getSupportFragmentManager().getActiveFragment()
                if (delegate.dispatchBackPressedEvent(active)) return
                support.onBackPressedSupport()
            }
        })
    }

    fun onBackPressedSupport() {
        if (getSupportFragmentManager().backStackEntryCount > 1) {
            pop()
        } else {
            ActivityCompat.finishAfterTransition(activity)
        }
    }

    fun onDestroy() {
        debugStackDelegate.onDestroy()
    }

    fun dispatchTouchEvent(ev: MotionEvent?) = !fragmentClickable


    /**
     * 获取设置的全局动画 copy
     *
     * @return FragmentAnimator
     */
    fun getFragmentAnimator() = fragmentAnimator.copy()

    /**
     * Set all fragments animation.
     * 设置Fragment内的全局动画
     */
    fun setFragmentAnimator(animator: FragmentAnimator) {
        this.fragmentAnimator = animator

        val fragments = getSupportFragmentManager().getISupportFragments()
        if (fragments.isNullOrEmpty()) return

        for (fa in fragments) {
            val delegate = fa.supportDelegate
            if (delegate.mAnimByActivity) {
                delegate.fragmentAnimator = animator.copy()
                if (delegate.mAnimHelper != null) {
                    delegate.mAnimHelper.notifyChanged(delegate.fragmentAnimator)
                }
            }
        }
    }

    /**
     * Set all fragments animation.
     * 构建Fragment转场动画
     * <p/>
     * 如果是在Activity内实现,则构建的是Activity内所有Fragment的转场动画,
     * 如果是在Fragment内实现,则构建的是该Fragment的转场动画,此时优先级 > Activity的onCreateFragmentAnimator()
     *
     * @return FragmentAnimator对象
     */
    fun onCreateFragmentAnimator() = FragmentAnimator.verticalAnimator()

    /**
     * 显示栈视图dialog,调试时使用
     */
    fun showFragmentStackHierarchyView() = debugStackDelegate.showFragmentStackHierarchyView()

    /**
     * 显示栈视图日志,调试时使用
     */
    fun logFragmentStackHierarchy(tag: String) = debugStackDelegate.logFragmentRecords(tag)

    /**
     * Causes the Runnable r to be added to the action queue.
     * <p>
     * The runnable will be run after all the previous action has been run.
     * <p>
     * 前面的事务全部执行后 执行该Action
     */
    fun post(runnable: Runnable) {
        delegate.post(runnable)
    }

    fun loadRootFragment(containerId: Int,
                         to: ISupportFragment?,
                         addToBackStack: Boolean = true,
                         allowAnimation: Boolean = false) {
        delegate.loadRootTransaction(getSupportFragmentManager(),
            containerId, to,
            addToBackStack, allowAnimation)
    }

    fun loadMultipleRootFragment(containerId: Int,
                                 showPosition: Int,
                                 vararg fragments: ISupportFragment?) {
        delegate.loadMultipleRootTransaction(getSupportFragmentManager(),
            containerId, showPosition, *fragments)
    }

    fun showHideFragment(show: ISupportFragment?, hide: ISupportFragment? = null) {
        delegate.showHideFragment(getSupportFragmentManager(), show, hide)
    }

    fun start(to: ISupportFragment,
              @ISupportFragment.LaunchMode launchMode: Int = ISupportFragment.STANDARD) {
        delegate.dispatchStartTransaction(getSupportFragmentManager(),
            getTopFragment(), to, 0,
            launchMode, TransactionDelegate.TYPE_ADD)
    }

    fun startForResult(to: ISupportFragment,
                       requestCode: Int,
                       @ISupportFragment.LaunchMode launchMode: Int = ISupportFragment.STANDARD) {
        delegate.dispatchStartTransaction(getSupportFragmentManager(),
            getTopFragment(), to, requestCode,
            launchMode, TransactionDelegate.TYPE_ADD_RESULT)
    }

    fun startWithPop(to: ISupportFragment) {
        delegate.startWithPop(getSupportFragmentManager(),
            getTopFragment(), to)
    }

    fun <T : ISupportFragment> startWithPopTo(to: ISupportFragment,
                                             clazz: Class<T>,
                                             includeTargetFragment: Boolean) {
        delegate.startWithPopTo(getSupportFragmentManager(),
            getTopFragment(), to, clazz.name, includeTargetFragment)
    }

    fun replaceFragment(to: ISupportFragment, addToBackStack: Boolean) {
        delegate.dispatchStartTransaction(getSupportFragmentManager(),
            getTopFragment(), to, 0,
            ISupportFragment.STANDARD,
            if (addToBackStack) TransactionDelegate.TYPE_REPLACE else TransactionDelegate.TYPE_REPLACE_DONT_BACK)
    }

    fun pop() {
        delegate.pop(getSupportFragmentManager())
    }

    fun <T : ISupportFragment> popTo(clazz: Class<T>,
                                     includeTargetFragment: Boolean,
                                     runnable: Runnable? = null,
                                     popAnim: Int = TransactionDelegate.DEFAULT_POPTO_ANIM) {
        delegate.popTo(clazz.name, includeTargetFragment, runnable, getSupportFragmentManager(), popAnim)
    }

    /**
     * 针对每个Fragment都重新生成一个 额外事物
     */
    private val extraArray by lazy { HashMap<ISupportFragment, ExtraTransaction>() }

    fun extraTransaction(): ExtraTransaction {
        val fragment = getTopFragment()!!
        var extra = extraArray[fragment]
        if (extra == null) {
            extra = ExtraTransactionImpl(activity, getTopFragment()!!, delegate, true)
            extraArray[fragment] = extra
        }
        return extra
    }

    fun getTransactionDelegate() = delegate

    private fun getSupportFragmentManager() = activity.supportFragmentManager

    private fun getTopFragment() = getSupportFragmentManager().getTopFragment()


    // Wait to del

    fun <T : ISupportFragment> popTo(clazz: Class<T>,
                                     includeTargetFragment: Boolean) {
        popTo(clazz, includeTargetFragment, null)
    }

    fun <T : ISupportFragment> popTo(clazz: Class<T>,
                                     includeTargetFragment: Boolean,
                                     runnable: Runnable?) {
        popTo(clazz, includeTargetFragment, runnable, TransactionDelegate.DEFAULT_POPTO_ANIM)
    }

}