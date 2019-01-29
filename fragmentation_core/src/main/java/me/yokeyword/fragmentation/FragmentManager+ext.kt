package me.yokeyword.fragmentation

import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentationMagician
import me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning

internal fun FragmentManager?.getActiveFragments(): List<Fragment?>? {
    if (this == null) return null
    return FragmentationMagician.getActiveFragments(this)
}

internal fun FragmentManager?.getISupportFragments(): List<ISupportFragment>? {
    return getActiveFragments()
        ?.filter { it is ISupportFragment }
        ?.map { it as ISupportFragment }
}

internal fun FragmentManager?.getBackStackTopFragment(containerId: Int = 0): ISupportFragment? {
    if (this == null) return null

    val count = backStackEntryCount
    for (i in count - 1 downTo 0) {
        val entry = getBackStackEntryAt(i)
        val fa = findFragmentByTag(entry.name)
        if (fa is ISupportFragment) {
            if (containerId == 0) return fa
            if (fa.supportDelegate.containerId == containerId) return fa
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


// TODO
internal fun FragmentManager?.getWillPopFragments(tag: String, includeTarget: Boolean): List<Fragment> {
    if (this == null) return emptyList()

    val fragments = getActiveFragments()
    if (fragments.isNullOrEmpty()) return emptyList()

    val target = findFragmentByTag(tag)

    val size = fragments.size
    var startIndex = -1
    for (i in size - 1 downTo 0) {
        if (target == fragments[i]) {
            if (includeTarget) {
                startIndex = i
            } else if (i + 1 < size) {
                startIndex = i + 1
            }
            break
        }
    }
    if (startIndex == -1) return emptyList()

    val list = ArrayList<Fragment>(size - startIndex)
    for (i in size - 1 downTo startIndex) {
        val fragment = fragments[i]
        if (fragment != null && fragment.view != null) {
            list.add(fragment)
        }
    }
    return list
}

internal fun FragmentManager?.handleAfterSaveInStateTransactionException(action: String) {
    if (this == null) return
    val stateSaved = FragmentationMagician.isStateSaved(this)
    if (stateSaved) {
        val e = AfterSaveStateTransactionWarning(action)
        Fragmentation.getDefault().getHandler()?.onException(e)
    }
}