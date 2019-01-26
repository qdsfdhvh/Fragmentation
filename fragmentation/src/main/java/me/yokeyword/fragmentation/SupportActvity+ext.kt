package me.yokeyword.fragmentation

import android.view.KeyEvent
import android.view.MotionEvent

internal fun dispatchTouchEventEvent(activeFragment: ISupportFragment?, ev: MotionEvent?): Boolean {
    if (activeFragment is SupportFragment) {
        val result = activeFragment.dispatchTouchEventSupport(ev)
        if (result) {
            return true
        }

        val parentFragment = activeFragment.parentFragment
        if (parentFragment is SupportFragment && dispatchTouchEventEvent(parentFragment, ev)) {
            return true
        }
    }
    return false
}

internal fun dispatchKeyEvent(activeFragment: ISupportFragment?, event: KeyEvent?): Boolean {
    if (activeFragment is SupportFragment) {
        val result = activeFragment.dispatchKeyEventSupport(event)
        if (result) return true

        val parentFragment = activeFragment.parentFragment
        if (parentFragment is SupportFragment && dispatchKeyEvent(parentFragment, event)) {
            return true
        }
    }
    return false
}

internal fun SupportActivity.getActiveFragment(): ISupportFragment? {
    return supportFragmentManager.getActiveFragment()
}