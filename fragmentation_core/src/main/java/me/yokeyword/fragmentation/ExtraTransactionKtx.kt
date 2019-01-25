package me.yokeyword.fragmentation

import android.os.Build
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import me.yokeyword.fragmentation.helper.internal.TransactionRecord

interface ExtraTransactionKtx {

    /**
     * @param tag Optional tag name for the fragment, to later retrieve the
     *            fragment with {@link SupportHelperKtx(FragmentManager, String)}
     *            , pop(String)
     *            or FragmentManager.findFragmentByTag(String).
     */
    fun setTag(tag: String): ExtraTransactionKtx

    /**
     * Set specific animation resources to run for the fragments that are
     * entering and exiting in this transaction. The <code>currentFragmentPopEnter</code>
     * and <code>targetFragmentExit</code> animations will be played for targetFragmentEnter/currentFragmentPopExit
     * operations specifically when popping the back stack.
     */
    fun setCustomAnimations(@AnimatorRes @AnimRes targetFragmentEnter: Int,
                            @AnimatorRes @AnimRes currentFragmentPopExit: Int,
                            @AnimatorRes @AnimRes currentFragmentPopEnter: Int = 0,
                            @AnimatorRes @AnimRes targetFragmentExit: Int = 0): ExtraTransactionKtx

    /**
     * Don't add this extraTransaction to the back stack.
     */
    fun dontAddToBackStack(): DontAddToBackStackTransaction

    /**
     * Used with custom Transitions to map a View from a removed or hidden
     * Fragment to a View from a shown or added Fragment.
     * <var>sharedElement</var> must have a unique transitionName in the View hierarchy.
     *
     * @param sharedElement A View in a disappearing Fragment to match with a View in an
     *                      appearing Fragment.
     * @param sharedName    The transitionName for a View in an appearing Fragment to match to the shared
     *                      element.
     * @see Fragment#setSharedElementReturnTransition(Object)
     * @see Fragment#setSharedElementEnterTransition(Object)
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun addSharedElement(sharedElement: View, sharedName: String): ExtraTransactionKtx

    fun loadRootFragment(containerId: Int, toFragment: ISupportFragment, addToBackStack: Boolean, allowAnim: Boolean)

    fun start(to: ISupportFragment, @ISupportFragment.LaunchMode launchMode: Int = ISupportFragment.STANDARD)

    fun startDontHideSelf(to: ISupportFragment, @ISupportFragment.LaunchMode launchMode: Int = ISupportFragment.STANDARD)

    fun startForResult(to: ISupportFragment, requestCode: Int)

    fun startForResultDontHideSelf(to: ISupportFragment, requestCode: Int)

    fun startWithPop(to: ISupportFragment)

    fun startWithPopTo(to: ISupportFragment, targetFragmentTag: String, includeTargetFragment: Boolean)

    fun replace(to: ISupportFragment)

    /**
     * 使用setTag()自定义Tag时，使用下面popTo()／popToChild()出栈
     *
     * @param targetFragmentTag     通过setTag()设置的tag
     * @param includeTargetFragment 是否包含目标(Tag为targetFragmentTag)Fragment
     */
    fun popTo(targetFragmentTag: String,
              includeTargetFragment: Boolean,
              afterPopTransactionRunnable: Runnable? = null,
              popAnim: Int = TransactionDelegate.DEFAULT_POPTO_ANIM)

    fun popToChild(targetFragmentTag: String,
                   includeTargetFragment: Boolean,
                   afterPopTransactionRunnable: Runnable? = null,
                   popAnim: Int = TransactionDelegate.DEFAULT_POPTO_ANIM)

    /**
     * 使用dontAddToBackStack() 加载Fragment时， 使用remove()移除Fragment
     */
    fun remove(fragment: ISupportFragment, showPreFragment: Boolean)

    interface DontAddToBackStackTransaction {
        /**
         * add() +  hide(preFragment)
         */
        fun start(to: ISupportFragment)

        /**
         * Only add()
         */
        fun add(to: ISupportFragment)

        /**
         * replace()
         */
        fun replace(to: ISupportFragment)
    }

}

class ExtraTransactionKtxImpl(private val activity: FragmentActivity,
                              private val support: ISupportFragment,
                              private val fragment: Fragment,
                              private val delegate: TransactionDelegate,
                              private val fromActivity: Boolean): ExtraTransactionKtx, ExtraTransactionKtx.DontAddToBackStackTransaction  {

    constructor(activity: FragmentActivity, support: ISupportFragment, delegate: TransactionDelegate, fromActivity: Boolean):
            this(activity, support, support as Fragment, delegate, fromActivity)

    private val record = TransactionRecord()

    override fun setTag(tag: String): ExtraTransactionKtx {
        record.tag = tag
        return this
    }

    override fun setCustomAnimations(targetFragmentEnter: Int,
                                     currentFragmentPopExit: Int,
                                     currentFragmentPopEnter: Int,
                                     targetFragmentExit: Int): ExtraTransactionKtx {
        record.targetFragmentEnter = targetFragmentEnter
        record.currentFragmentPopExit = currentFragmentPopExit
        record.currentFragmentPopEnter = currentFragmentPopEnter
        record.targetFragmentExit = targetFragmentExit
        return this
    }

    override fun dontAddToBackStack(): ExtraTransactionKtx.DontAddToBackStackTransaction {
        record.dontAddToBackStack = true
        return this
    }

    override fun addSharedElement(sharedElement: View, sharedName: String): ExtraTransactionKtx {
        record.add(sharedElement, sharedName)
        return this
    }

    override fun loadRootFragment(containerId: Int,
                                  toFragment: ISupportFragment,
                                  addToBackStack: Boolean,
                                  allowAnim: Boolean) {
        toFragment.supportDelegate.mTransactionRecord = record
        delegate.loadRootTransaction(getFragmentManager(),
            containerId, toFragment, addToBackStack, allowAnim)
    }

    override fun start(to: ISupportFragment) {
        start(to, ISupportFragment.STANDARD)
    }

    override fun start(to: ISupportFragment, launchMode: Int) {
        to.supportDelegate.mTransactionRecord = record
        delegate.dispatchStartTransaction(getFragmentManager(),
            support, to, 0,
            launchMode,
            TransactionDelegate.TYPE_ADD)
    }

    override fun add(to: ISupportFragment) {
        to.supportDelegate.mTransactionRecord = record
        delegate.dispatchStartTransaction(getFragmentManager(),
            support, to, 0,
            ISupportFragment.STANDARD,
            TransactionDelegate.TYPE_ADD_WITHOUT_HIDE)
    }

    override fun replace(to: ISupportFragment) {
        to.supportDelegate.mTransactionRecord = record
        delegate.dispatchStartTransaction(getFragmentManager(),
            support, to, 0,
            ISupportFragment.STANDARD,
            TransactionDelegate.TYPE_REPLACE)
    }

    override fun startDontHideSelf(to: ISupportFragment, launchMode: Int) {
        to.supportDelegate.mTransactionRecord = record
        delegate.dispatchStartTransaction(getFragmentManager(),
            support, to, 0,
            launchMode,
            TransactionDelegate.TYPE_ADD_WITHOUT_HIDE)
    }

    override fun startForResult(to: ISupportFragment, requestCode: Int) {
        to.supportDelegate.mTransactionRecord = record
        delegate.dispatchStartTransaction(getFragmentManager(),
            support, to, requestCode,
            ISupportFragment.STANDARD,
            TransactionDelegate.TYPE_ADD_RESULT)
    }

    override fun startForResultDontHideSelf(to: ISupportFragment, requestCode: Int) {
        to.supportDelegate.mTransactionRecord = record
        delegate.dispatchStartTransaction(getFragmentManager(),
            support, to, requestCode,
            ISupportFragment.STANDARD,
            TransactionDelegate.TYPE_ADD_RESULT_WITHOUT_HIDE)
    }

    override fun startWithPop(to: ISupportFragment) {
        to.supportDelegate.mTransactionRecord = record
        delegate.startWithPop(getFragmentManager(),support, to)
    }

    override fun startWithPopTo(to: ISupportFragment,
                                targetFragmentTag: String,
                                includeTargetFragment: Boolean) {
        to.supportDelegate.mTransactionRecord = record
        delegate.startWithPopTo(getFragmentManager(),
            support, to, targetFragmentTag, includeTargetFragment)
    }

    override fun popTo(targetFragmentTag: String,
                       includeTargetFragment: Boolean,
                       afterPopTransactionRunnable: Runnable?,
                       popAnim: Int) {
        delegate.popTo(targetFragmentTag,
            includeTargetFragment,
            afterPopTransactionRunnable,
            getFragmentManager(),
            popAnim)
    }

    override fun popToChild(targetFragmentTag: String,
                            includeTargetFragment: Boolean,
                            afterPopTransactionRunnable: Runnable?,
                            popAnim: Int) {
        if (fromActivity) {
            popTo(targetFragmentTag,
                includeTargetFragment,
                afterPopTransactionRunnable,
                popAnim)
        } else {
            delegate.popTo(targetFragmentTag,
                includeTargetFragment,
                afterPopTransactionRunnable,
                fragment.childFragmentManager,
                popAnim)
        }
    }

    override fun remove(fragment: ISupportFragment, showPreFragment: Boolean) {
        delegate.remove(getFragmentManager(), fragment as Fragment, showPreFragment)
    }

    private fun getFragmentManager(): FragmentManager {
        return fragment.fragmentManager ?: activity.supportFragmentManager
    }

}