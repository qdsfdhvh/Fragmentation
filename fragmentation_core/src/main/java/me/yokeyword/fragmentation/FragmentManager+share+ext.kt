package me.yokeyword.fragmentation

import androidx.fragment.app.FragmentManager

@Suppress("UNCHECKED_CAST")
fun <T : ISupportFragment> FragmentManager?.findFragment(clazz: Class<T>): T? {
    val fragments = getISupportFragments()
    if (fragments.isNullOrEmpty()) return null

    return fragments.firstOrNull { it.javaClass.name == clazz.name } as? T
}

@Suppress("UNCHECKED_CAST")
fun <T : ISupportFragment> FragmentManager?.findFragment(tag: String?): T? {
    if (this == null) return null
    return findFragmentByTag(tag) as? T
}

/**
 * 获得FragmentManager中指定containerId的顶栈fragment
 * @param containerId 所依附的界面id，为0时返回此FragmentManager的顶栈
 */
fun FragmentManager?.getTopFragment(containerId: Int = 0): ISupportFragment? {
    val fragments = getISupportFragments()
    if (fragments.isNullOrEmpty()) return null

    for (fa in fragments.reversed()) {
        if (containerId == 0) return fa
        if (fa.supportDelegate.mContainerId == containerId) return fa
    }
    return null
}

fun FragmentManager?.getActiveFragment(parent: ISupportFragment? = null): ISupportFragment? {
    if (this == null) return null

    val fragments = getActiveFragments()
    if (fragments.isNullOrEmpty()) return parent

    for (fa in fragments.reversed()) {
        if (fa is ISupportFragment && fa.isResumed && !fa.isHidden && fa.userVisibleHint) {
            return fa.getChildActiveFragment()
        }
    }
    return parent
}