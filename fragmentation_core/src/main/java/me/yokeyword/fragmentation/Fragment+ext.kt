package me.yokeyword.fragmentation

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

internal fun Fragment.getActiveFragments(): List<Fragment?>? {
    return fragmentManager.getActiveFragments()
}

internal fun Fragment.getChildActiveFragments(): List<Fragment?>? {
    return childFragmentManager.getActiveFragments()
}

internal fun Fragment.getISupportFragments(): List<ISupportFragment>? {
    return fragmentManager.getISupportFragments()
}

internal fun Fragment.getChildISupportFragments(): List<ISupportFragment>? {
    return childFragmentManager.getISupportFragments()
}

internal fun Fragment.getChildActiveFragment(): ISupportFragment? {
    if (this !is ISupportFragment) return null
    return childFragmentManager.getActiveFragment(this)
}

internal fun Fragment.getPreFragment(): ISupportFragment? {
    val fragments = fragmentManager.getActiveFragments()
    if (fragments.isNullOrEmpty()) return null

    val index = fragments.indexOf(this)
    for (i in index - 1 downTo 0) {
        val pre = fragments[i]
        if (pre is ISupportFragment) {
            return pre
        }
    }
    return null
}

internal fun Fragment.isUserVisible(): Boolean {
    return !isHidden && userVisibleHint
}

internal fun Fragment.isParentUserVisible(): Boolean {
    val bak = parentFragment ?: return true
    return bak.isUserVisible()
}

internal fun Fragment.isParentSupportInvisible(): Boolean {
    val bak = parentFragment ?: return false
    return bak is ISupportFragment && !bak.isSupportVisible
}

internal fun Fragment.findContainerById(containerId: Int): ViewGroup? {
    val container: View?

    val parent = parentFragment
    when {
        parent != null -> container = parent.findContainerById(containerId)
        view != null -> container = view!!.findViewById(containerId)
        activity != null -> container = activity!!.findViewById(containerId)
        else -> return null
    }

    if (container is ViewGroup) return container
    return null
}