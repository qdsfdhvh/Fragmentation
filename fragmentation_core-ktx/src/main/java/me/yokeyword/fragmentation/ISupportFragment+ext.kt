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
    if (fragment == null) return
    supportDelegate.loadRootFragment(containerId, fragment, addToBackStack, allowAnimation)
}

/**
 * @param containerId 所依赖的控件id
 * @param showPosition 默认显示第x个fragment
 * @param fragments 待添加的fragments
 */
fun <T : ISupportFragment> ISupportFragment.loadRootFragments(containerId: Int,
                                                             showPosition: Int,
                                                             fragments: Array<T?>) {
    supportDelegate.loadMultipleRootFragment(containerId, showPosition, fragments)
}

/**
 * 跳转到指定fragment
 * @param to 待加载的fragment
 * @param mode 启动类型
 */
fun ISupportFragment.start(to: ISupportFragment,
                           requestCode: Int = 0,
                           mode: Transaction.LaunchMode = Transaction.LaunchMode.STANDARD) {
    supportDelegate.start(to, requestCode, mode)
}

///**
// * 跳转到指定fragment，按需要指定requestCode
// * @param to 待加载的fragment
// * @param requestCode 返回code
// */
//fun ISupportFragment.startForResult(to: ISupportFragment, requestCode: Int) {
//    supportDelegate.start(to, requestCode)
//}

/**
 * 跳转到指定fragment，不隐藏当前fragment
 * @param to 待加载的fragment
 */
fun ISupportFragment.startDontHideSelf(to: ISupportFragment) {
    extraTransaction().setCustomAnimations(
        R.anim.v_fragment_enter, R.anim.v_fragment_pop_exit,
        R.anim.v_fragment_pop_enter, R.anim.v_fragment_exit)
        .startDontHideSelf(to)
}

/**
 * 跳转到指定fragment，不隐藏当前fragment，按需要指定requestCode
 * @param to 待加载的fragment
 * @param requestCode 返回code
 */
fun ISupportFragment.startDontHideSelf(to: ISupportFragment, requestCode: Int) {
    extraTransaction().setCustomAnimations(
        R.anim.v_fragment_enter, R.anim.v_fragment_pop_exit,
        R.anim.v_fragment_pop_enter, R.anim.v_fragment_exit)
        .startDontHideSelf(to, requestCode)
}

/**
 * 跳转到指定fragment，并关闭当前的fragment
 * @param to 待加载的fragment
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
    if (null == show) return
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

fun <T : ISupportFragment> ISupportFragment.popTo(clazz: Class<T>,
                                                  self: Boolean = true,
                                                  runnable: Runnable? = null,
                                                  popAnim: Int = TransactionDelegate.DEFAULT_POPTO_ANIM) {
    supportDelegate.popTo(clazz.name, self, runnable, popAnim)
}

/**
 * 寻找相应的fragment
 */
fun <T : ISupportFragment> ISupportFragment.findFragment(clazz: Class<T>): T? {
    if (this !is Fragment) return null
    return fragmentManager.findFragment(clazz)
}

fun <T : ISupportFragment> ISupportFragment.findFragment(tag: String?): T? {
    if (this !is Fragment) return null
    return fragmentManager.findFragment(tag)
}

/**
 * 寻找相应的子fragment
 */
fun <T : ISupportFragment> ISupportFragment.findChildFragment(clazz: Class<T>): T? {
    if (this !is Fragment) return null
    return childFragmentManager.findFragment(clazz)
}

fun <T : ISupportFragment> ISupportFragment.findChildFragment(tag: String?): T? {
    if (this !is Fragment) return null
    return childFragmentManager.findFragment(tag)
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
 * @param self 是否包含自身
 */
fun <T : ISupportFragment> ISupportFragment.popToChild(clazz: Class<T>, self: Boolean) {
    supportDelegate.popTo(clazz.name, self)
}

/**
 * 替换fragment
 * @param to 待替换的fragment
 * @param addToBackStack 是否保存到栈
 */
fun ISupportFragment.replaceFragment(to: ISupportFragment, addToBackStack: Boolean = false) {
    supportDelegate.replaceFragment(to, addToBackStack)
}

/**
 * 显示指定fragment，并退栈到目标class的fragment
 * @param to 待显示的fragment
 * @param self 目标fragment自身是否需要退栈
 */
fun <T : ISupportFragment> ISupportFragment.startWithPopTo(to: ISupportFragment, clazz: Class<T>, self: Boolean = false) {
    supportDelegate.startWithPopTo(to, clazz.name, self)
}

/**
 * 子级跳转到指定fragment
 * @param to 跳转到此fragment
 * @param mode 启动类型
 */
fun ISupportFragment.startChild(to: ISupportFragment, @ISupportFragment.LaunchMode mode: Int = ISupportFragment.STANDARD) {
    supportDelegate.startChild(to, mode)
}

/**
 * 子级跳转到指定fragment，按需要指定requestCode
 * @param to 跳转到此fragment
 * @param requestCode 返回code
 */
fun ISupportFragment.startChildForResult(to: ISupportFragment, requestCode: Int) {
    supportDelegate.startChild(to, requestCode)
}

/**
 * 子级跳转到指定fragment，并关闭当前的fragment
 * @param to 跳转到此fragment
 */
fun ISupportFragment.startChildWithPop(to: ISupportFragment) {
    supportDelegate.startChildWithPop(to)
}

/**
 * 替换fragment
 * @param to 待替换的fragment
 * @param addToBackStack 是否保存到栈
 */
fun ISupportFragment.replaceChildFragment(to: ISupportFragment, addToBackStack: Boolean = false) {
    supportDelegate.replaceChildFragment(to, addToBackStack)
}

fun ISupportFragment.popQuiet() {
    supportDelegate.popQuiet()
}


// Simple User

/**
 * 父级跳转到指定fragment
 */
fun ISupportFragment.parentStart(to: ISupportFragment) {
    if (this !is Fragment) return
    val parent = parentFragment as? ISupportFragment ?: return
    parent.start(to)
}


// Wait to del

/**
 * @param containerId 所依赖的控件id
 * @param showPosition 默认显示第x个fragment
 * @param fragments 待添加的fragments
 */
fun <T : ISupportFragment> ISupportFragment.loadMultipleRootFragment(containerId: Int,
                                                                     showPosition: Int,
                                                                     vararg fragments: T?) {
    supportDelegate.loadMultipleRootFragment(containerId, showPosition, fragments)
}
