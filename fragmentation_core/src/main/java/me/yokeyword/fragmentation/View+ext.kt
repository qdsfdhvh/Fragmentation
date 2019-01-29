package me.yokeyword.fragmentation

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

internal fun View?.showSoftInput() {
    if (this == null) return
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
    requestFocus()
    postDelayed({
        imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
    }, 200L)
}

internal fun View?.hideSoftInput() {
    if (this == null) return
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
    imm.hideSoftInputFromWindow(windowToken, 0)
}