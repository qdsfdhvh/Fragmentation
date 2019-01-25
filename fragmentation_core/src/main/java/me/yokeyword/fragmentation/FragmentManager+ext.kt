package me.yokeyword.fragmentation

import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentationMagician
import me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning

internal fun FragmentManager?.getActiveFragments(): List<Fragment>? {
    if (this == null) return null
    return FragmentationMagician.getActiveFragments(this)
}

internal fun FragmentManager?.getISupportFragments(): List<ISupportFragment>? {
    return getActiveFragments()
        ?.filter { it is ISupportFragment }
        ?.map { it as ISupportFragment }
}

internal fun FragmentManager?.getActiveFragment(parent: ISupportFragment? = null): ISupportFragment? {
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

internal fun FragmentManager?.getTopFragment(containerId: Int = 0): ISupportFragment? {
    val fragments = getISupportFragments()
    if (fragments.isNullOrEmpty()) return null

    for (fa in fragments.reversed()) {
        if (containerId == 0) return fa
        if (fa.supportDelegate.mContainerId == containerId) return fa
    }
    return null
}

internal fun FragmentManager?.getBackStackTopFragment(containerId: Int): ISupportFragment? {
    if (this == null) return null

    val count = backStackEntryCount
    for (i in count - 1 downTo 0) {
        val entry = getBackStackEntryAt(i)
        val fa = findFragmentByTag(entry.name)
        if (fa is ISupportFragment) {
            if (containerId == 0) return fa
            if (fa.supportDelegate.mContainerId == containerId) return fa
        }
    }
    return null
}

@Suppress("UNCHECKED_CAST")
internal fun <T : ISupportFragment> FragmentManager?.findBackStackFragment(tag: String?, clazz: Class<T>): T? {
    if (this == null) return null

    val bak = if (TextUtils.isEmpty(tag)) clazz.name else tag

    val count = backStackEntryCount
    for (i in count - 1 downTo 0) {
        val entry = getBackStackEntryAt(i)
        if (bak == entry.name) {
            val fa = findFragmentByTag(entry.name)
            if (fa is ISupportFragment) return fa as? T
        }
    }
    return null
}

internal fun FragmentManager?.getWillPopFragments(tag: String, includeTarget: Boolean): List<Fragment> {
    if (this == null) return emptyList()

    val fragments = getActiveFragments()
    if (fragments.isNullOrEmpty()) return emptyList()

    val target = findFragmentByTag(tag)
    val reversed = fragments.asReversed()

    val index = reversed.indexOf(target)
    val startIndex = when {
        includeTarget -> index
        index + 1 < fragments.size -> index + 1
        else -> -1
    }
    if (startIndex == -1) return emptyList()

    return reversed.filter { it.view != null }
}


@Suppress("UNCHECKED_CAST")
internal fun <T : ISupportFragment> FragmentManager?.findFragment(clazz: Class<T>): T? {
    val fragments = getISupportFragments()
    if (fragments.isNullOrEmpty()) return null

    return fragments.firstOrNull { it.javaClass.name == clazz.name } as? T
}

@Suppress("UNCHECKED_CAST")
internal fun <T : ISupportFragment> FragmentManager?.findFragment(tag: String?): T? {
    if (this == null) return null
    return findFragmentByTag(tag) as? T
}

internal fun FragmentManager?.handleAfterSaveInStateTransactionException(action: String) {
    if (this == null) return
    val stateSaved = FragmentationMagician.isStateSaved(this)
    if (stateSaved) {
        val e = AfterSaveStateTransactionWarning(action)
        Fragmentation.getDefault().getHandler()?.onException(e)
    }
}