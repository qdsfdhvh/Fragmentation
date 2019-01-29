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

    private lateinit var animator: FragmentAnimator
    private val delegate by lazy { TransactionDelegate(support) }
    private val debugStack by lazy { DebugStackDelegate(activity) }
    private val transaction by lazy { TransactionImpl(null, activity.supportFragmentManager, delegate) }

    /**
     * 当Fragment根布局 没有 设定background属性时,
     * Fragmentation默认使用Theme的android:windowbackground作为Fragment的背景,
     * 可以通过该方法改变Fragment背景。
     */
    @DrawableRes var defaultFragmentBackground = 0

    /**
     * 针对每个Fragment都重新生成一个 额外事物
     */
    private val extraArray by lazy { HashMap<ISupportFragment, ExtraTransaction>() }

    fun extraTransaction(): ExtraTransaction {
        val fragment = activity.supportFragmentManager.getTopFragment()!!
        var extra = extraArray[fragment]
        if (extra == null) {
            extra = ExtraTransactionImpl(activity, fragment, delegate, true)
            extraArray[fragment] = extra
        }
        return extra
    }

    fun onCreate(savedInstanceState: Bundle?) {
        animator = support.onCreateFragmentAnimator()
        debugStack.onCreate(Fragmentation.getDefault().getMode())
    }

    fun onPostCreate(savedInstanceState: Bundle?) {
        debugStack.onPostCreate(Fragmentation.getDefault().getMode())
    }

    fun onBackPressed() {
        delegate.enqueue(object : Action(Type.BACK) {
            override fun run() {
                if (!fragmentClickable) fragmentClickable = true

                val active = activity.supportFragmentManager.getActiveFragment()
                if (delegate.dispatchBackPressedEvent(active)) return
                support.onBackPressedSupport()
            }
        })
    }

    fun onBackPressedSupport() {
        if (activity.supportFragmentManager.backStackEntryCount > 1) {
            pop()
        } else {
            ActivityCompat.finishAfterTransition(activity)
        }
    }

    fun onDestroy() {
        debugStack.onDestroy()
        extraArray.clear()
    }

    fun dispatchTouchEvent(ev: MotionEvent?) = !fragmentClickable


    /**
     * 获取设置的全局动画 copy
     *
     * @return FragmentAnimator
     */
    fun getFragmentAnimator() = animator.copy()

    /**
     * Set all fragments animation.
     * 设置Fragment内的全局动画
     */
    fun setFragmentAnimator(animator: FragmentAnimator) {
        this.animator = animator

        val fragments = activity.supportFragmentManager.getISupportFragments()
        if (fragments.isNullOrEmpty()) return

        for (fa in fragments) {
            val delegate = fa.supportDelegate
//            if (delegate.mAnimByActivity) {
//                delegate.animator = animator.copy()
//                if (delegate.mAnimHelper != null) {
//                    delegate.mAnimHelper.notifyChanged(delegate.animator)
//                }
//            }
            if (delegate.animByActivity) {
//                delegate.setFragmentAnimator(animator.copy())
//                delegate.animHelper.notifyChanged(delegate.getFragmentAnimator())
                delegate.setFragmentAnimator(animator.copy(), true)
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
    fun showFragmentStackHierarchyView() = debugStack.showFragmentStackHierarchyView()

    /**
     * 显示栈视图日志,调试时使用
     */
    fun logFragmentStackHierarchy(tag: String) = debugStack.logFragmentRecords(tag)

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

    fun loadRootFragment(containerId: Int, to: ISupportFragment,
                         addToBackStack: Boolean = true, allowAnimation: Boolean = false) {
        transaction.loadRootFragment(containerId, to, addToBackStack, allowAnimation)
    }

    fun loadMultipleRootFragment(containerId: Int,
                                 showPosition: Int,
                                 vararg fragments: ISupportFragment?) {
        transaction.loadMultipleRootFragment(containerId, showPosition, fragments)
    }

    fun start(to: ISupportFragment,
              requestCode: Int = 0,
              mode: Transaction.LaunchMode = Transaction.LaunchMode.STANDARD) {
        transaction.start(to, requestCode, mode)
    }

    fun startDontHideSelf(to: ISupportFragment,
                          requestCode: Int = 0,
                          mode: Transaction.LaunchMode = Transaction.LaunchMode.STANDARD) {
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

    fun popTo(tag: String,
              self: Boolean,
              runnable: Runnable? = null,
              popAnim: Int = TransactionDelegate.DEFAULT_POPTO_ANIM) {
        transaction.popTo(tag, self, runnable, popAnim)
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

    fun getTransactionDelegate() = delegate

}