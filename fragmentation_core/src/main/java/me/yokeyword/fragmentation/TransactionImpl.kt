package me.yokeyword.fragmentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

internal class TransactionImpl(private val support: ISupportFragment?,
                               private val fragmentManager: FragmentManager?,
                               private val delegate: TransactionDelegate): Transaction {

    override fun loadRootFragment(containerId: Int, to: ISupportFragment,
                                  addToBackStack: Boolean,
                                  allowAnim: Boolean) {
        delegate.loadRootTransaction(fragmentManager,
            containerId, to,
            addToBackStack, allowAnim)
    }

    override fun <T : ISupportFragment> loadMultipleRootFragment(containerId: Int,
                                                                 showPosition: Int,
                                                                 fragments: Array<T?>) {
        delegate.loadMultipleRootTransaction(fragmentManager,containerId, showPosition, *fragments)
    }

    override fun start(to: ISupportFragment, requestCode: Int, mode: Transaction.LaunchMode) {
        delegate.dispatchStartTransaction(fragmentManager,
            getSupport(), to, requestCode,
            mode.id,
            if (requestCode == 0) TransactionDelegate.TYPE_ADD
            else TransactionDelegate.TYPE_ADD_RESULT)
    }

    override fun startDontHideSelf(to: ISupportFragment, requestCode: Int, mode: Transaction.LaunchMode) {
        delegate.dispatchStartTransaction(fragmentManager,
            getSupport(), to, requestCode,
            mode.id,
            if (requestCode == 0) TransactionDelegate.TYPE_ADD_WITHOUT_HIDE
            else TransactionDelegate.TYPE_ADD_RESULT_WITHOUT_HIDE)
    }

    override fun startWithPop(to: ISupportFragment) {
        delegate.startWithPop(fragmentManager, getSupport(), to)
    }

    override fun startWithPopTo(to: ISupportFragment, tag: String, self: Boolean) {
        delegate.startWithPopTo(fragmentManager,
            getSupport(), to, tag, self)
    }

    override fun pop() {
        delegate.pop(fragmentManager)
    }

    override fun popTo(tag: String, self: Boolean, runnable: Runnable?, popAnim: Int) {
        delegate.popTo(tag, self, runnable, fragmentManager, popAnim)
    }

    override fun popQuiet() {
        delegate.popQuiet(fragmentManager)
    }

    override fun removeFragment(fragment: ISupportFragment, showPre: Boolean) {
        if (fragment !is Fragment) return
        delegate.remove(fragmentManager, fragment, showPre)
    }

    override fun replaceFragment(to: ISupportFragment, addToBackStack: Boolean) {
        delegate.dispatchStartTransaction(fragmentManager,
            getSupport(), to, 0,
            ISupportFragment.STANDARD,
            if (addToBackStack) TransactionDelegate.TYPE_REPLACE else TransactionDelegate.TYPE_REPLACE_DONT_BACK)
    }

    override fun showHideFragment(show: ISupportFragment, hide: ISupportFragment?) {
        delegate.showHideFragment(fragmentManager, show, hide)
    }

    private fun getSupport() = support ?: fragmentManager.getTopFragment()

}