package me.yokeyword.fragmentation

interface Transaction {

    fun loadRootFragment(containerId: Int, to: ISupportFragment, addToBackStack: Boolean = true, allowAnim: Boolean = false)

    fun <T : ISupportFragment> loadMultipleRootFragment(containerId: Int, showPosition: Int, fragments: Array<T?>)

    fun start(to: ISupportFragment, requestCode: Int = 0, mode: LaunchMode = LaunchMode.STANDARD)

    fun startDontHideSelf(to: ISupportFragment, requestCode: Int = 0, mode: LaunchMode = LaunchMode.STANDARD)

    fun startWithPop(to: ISupportFragment)

    fun startWithPopTo(to: ISupportFragment, tag: String, self: Boolean)

    fun pop()

    fun popTo(tag: String, self: Boolean, runnable: Runnable? = null, popAnim: Int = TransactionDelegate.DEFAULT_POPTO_ANIM)

    fun popQuiet()

    fun removeFragment(fragment: ISupportFragment, showPre: Boolean = true)

    fun replaceFragment(to: ISupportFragment, addToBackStack: Boolean = true)

    fun showHideFragment(show: ISupportFragment, hide: ISupportFragment? = null)

    enum class LaunchMode(val id: Int) {
        STANDARD(0),
        SINGLE_TOP(1),
        SINGLE_TASK(2)
    }

}