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
    if (fragment == null) return
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
 * @param requestCode 返回code，默认0
 * @param mode 启动类型
 */
fun ISupportActivity.start(to: ISupportFragment,
                           requestCode: Int = 0,
                           mode: Transaction.LaunchMode = Transaction.LaunchMode.STANDARD) {
    supportDelegate.start(to, requestCode, mode)
}

///**
// * 跳转到指定fragment，并指定requestCode
// * @param to 跳转到此fragment
// * @param requestCode 返回code
// */
//fun ISupportActivity.startForResult(to: ISupportFragment, requestCode: Int) {
//    supportDelegate.startForResult(to, requestCode)
//}

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
    if (show == null) return
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
    return supportFragmentManager.findFragment(clazz)
}

fun <T : ISupportFragment> ISupportActivity.findFragment(tag: String?): T? {
    if (this !is FragmentActivity) return null
    return supportFragmentManager.findFragment(tag)
}

/**
 * 出栈到目标fragment
 * @param clazz 目标fragment
 * @param self 是否包含该fragment
 */
fun <T : ISupportFragment> ISupportActivity.popToChild(clazz: Class<T>, self: Boolean) {
    supportDelegate.popTo(clazz.name, self)
}

/**
 * 获取顶栈
 */
fun ISupportActivity.getTopFragment(containerId: Int = 0): ISupportFragment? {
    if (this !is FragmentActivity) return null
    return supportFragmentManager.getTopFragment(containerId)
}