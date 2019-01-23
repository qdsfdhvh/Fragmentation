package me.yokeyword.fragmentation

import androidx.fragment.app.Fragment

/**
 * @param containerId 所依赖的控件id
 * @param fragment 待添加的fragment
 * @param addToBackStack 是否放入栈
 * @param allowAnimation 是否加载动画
 */
fun ISupportFragment.loadRootFragment(containerId: Int,
                                      fragment: ISupportFragment?,
                                      addToBackStack: Boolean = true,
                                      allowAnimation: Boolean = false) {
    supportDelegate.loadRootFragment(containerId, fragment, addToBackStack, allowAnimation)
}

/**
 * @param containerId 所依赖的控件id
 * @param showPosition 默认显示第x个fragment
 * @param fragments 待添加的fragments
 */
fun <T: ISupportFragment> ISupportFragment.loadRootFragments(containerId: Int,
                                       showPosition: Int,
                                       fragments: Array<T?>) {
    supportDelegate.loadMultipleRootFragment(containerId, showPosition, *fragments)
}

/**
 * 跳转到指定fragment
 * @param to 跳转到此fragment
 */
fun ISupportFragment.start(to: ISupportFragment) {
    supportDelegate.start(to)
}

/**
 * 跳转到指定fragment，按需要指定requestCode
 * @param to 跳转到此fragment
 * @param requestCode 返回code
 */
fun ISupportFragment.start(to: ISupportFragment, requestCode: Int) {
    supportDelegate.startForResult(to, requestCode)
}

/**
 * 跳转到指定fragment，不隐藏当前fragment
 * @param to 跳转到此fragment
 */
fun ISupportFragment.startDontHideSelf(to: ISupportFragment) {
    extraTransaction().setCustomAnimations(
        R.anim.v_fragment_enter, R.anim.v_fragment_pop_exit,
        R.anim.v_fragment_pop_enter, R.anim.v_fragment_exit)
        .startDontHideSelf(to)
}

/**
 * 跳转到指定fragment，不隐藏当前fragment，按需要指定requestCode
 * @param to 跳转到此fragment
 * @param requestCode 返回code
 */
fun ISupportFragment.startDontHideSelf(to: ISupportFragment, requestCode: Int) {
    extraTransaction() .setCustomAnimations(
        R.anim.v_fragment_enter, R.anim.v_fragment_pop_exit,
        R.anim.v_fragment_pop_enter, R.anim.v_fragment_exit)
        .startDontHideSelf(to, requestCode)
}

/**
 * 跳转到指定fragment，并关闭当前的fragment
 * @param to 跳转到此fragment
 */
fun ISupportFragment.startWithPop(to: ISupportFragment) {
    supportDelegate.startWithPop(to)
}

/**
 * 替换显示的fragment
 * @param show 待显示的fragment
 * @param hide 待隐藏的fragment
 */
fun ISupportFragment.showHideFragment(show: ISupportFragment?,
                                      hide: ISupportFragment? = null) {
    supportDelegate.showHideFragment(show, hide)
}

/**
 * 退栈
 */
fun ISupportFragment.pop() {
    supportDelegate.pop()
}

/**
 * 此Fragment中的fragments退栈
 */
fun ISupportFragment.popChild() {
    supportDelegate.popChild()
}

/**
 * 寻找相应的fragment
 */
fun <T : ISupportFragment> ISupportFragment.findFragment(clazz: Class<T>): T? {
    if (this !is Fragment) return null
    return SupportHelperKtx.findFragment(fragmentManager, clazz)
}

/**
 * 寻找相应的子fragment
 */
fun <T : ISupportFragment> ISupportFragment.findChildFragment(clazz: Class<T>): T? {
    if (this !is Fragment) return null
    return SupportHelperKtx.findFragment(childFragmentManager, clazz)
}

/**
 * 隐藏软键盘
 */
fun ISupportFragment.hideSoftInput() {
    supportDelegate.hideSoftInput()
}

/**
 * 出栈到目标fragment
 * @param clazz 目标fragment
 * @param self 是否包含该fragment
 */
fun <T : ISupportFragment> ISupportFragment.popToChild(clazz: Class<T>, self: Boolean) {
    supportDelegate.popTo(clazz, self)
}

/**
 * 父级跳转到指定fragment
 */
fun ISupportFragment.parentStart(to: ISupportFragment) {
    if (this !is Fragment) return
    val parent = parentFragment as? ISupportFragment ?: return
    parent.start(to)
}

/**
 * 替换fragment
 */
fun ISupportFragment.replaceFragment(to: ISupportFragment, addToBackStack: Boolean) {
    supportDelegate.replaceFragment(to, addToBackStack)
}