package me.yokeyword.fragmentation

import androidx.fragment.app.FragmentActivity


/**
 * @param containerId 所依赖的控件id
 * @param fragment 待添加的fragment
 * @param addToBackStack 是否放入栈
 * @param allowAnimation 是否加载动画
 */
fun ISupportActivity.loadRootFragment(containerId: Int,
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
fun <T: ISupportFragment> ISupportActivity.loadRootFragments(containerId: Int,
                                       showPosition: Int,
                                       fragments: Array<T?>) {
    supportDelegate.loadMultipleRootFragment(containerId, showPosition, *fragments)
}

/**
 * 跳转到指定fragment，并隐藏当前的fragment
 * @param to 跳转到此fragment
 * @param launchMode 添加类型
 */
fun ISupportActivity.start(to: ISupportFragment,
                           @ISupportFragment.LaunchMode launchMode: Int = ISupportFragment.STANDARD) {
    supportDelegate.start(to, launchMode)
}

/**
 * 跳转到指定fragment，并指定requestCode
 * @param to 跳转到此fragment
 * @param requestCode 返回code
 */
fun ISupportActivity.startForResult(to: ISupportFragment, requestCode: Int) {
    supportDelegate.startForResult(to, requestCode)
}

/**
 * 跳转到指定fragment，并关闭当前的fragment
 * @param to 跳转到此fragment
 */
fun ISupportActivity.startWithPop(to: ISupportFragment) {
    supportDelegate.startWithPop(to)
}

/**
 * 替换显示的fragment
 * @param show 待显示的fragment
 * @param hide 待隐藏的fragment
 */
fun ISupportActivity.showHideFragment(show: ISupportFragment?,
                                      hide: ISupportFragment? = null) {
    supportDelegate.showHideFragment(show, hide)
}

/**
 * 退栈
 */
fun ISupportActivity.pop() {
    supportDelegate.pop()
}

/**
 * 寻找相应的fragment
 */
fun <T : ISupportFragment> ISupportActivity.findFragment(clazz: Class<T>): T? {
    if (this !is FragmentActivity) return null
    return SupportHelperKtx.findFragment(supportFragmentManager, clazz)
}

/**
 * 出栈到目标fragment
 * @param clazz 目标fragment
 * @param self 是否包含该fragment
 */
fun <T : ISupportFragment> ISupportActivity.popToChild(clazz: Class<T>, self: Boolean) {
    supportDelegate.popTo(clazz, self)
}