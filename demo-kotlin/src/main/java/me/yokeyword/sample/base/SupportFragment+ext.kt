package me.yokeyword.sample.base

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.sample.R

fun SupportFragment.initToolbarNav(toolbar: Toolbar, @StringRes resId: Int) {
    toolbar.setTitle(resId)
    initToolbarNav(toolbar)
}

fun SupportFragment.initToolbarNav(toolbar: Toolbar, title: String) {
    toolbar.title = title
    initToolbarNav(toolbar)
}

fun SupportFragment.initToolbarNav(toolbar: Toolbar) {
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    toolbar.setNavigationOnClickListener { onBackPressedSupport() }
}

fun SupportFragment.toast(@StringRes resId: Int) {
    Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show()
}

fun SupportFragment.toast(msg: String) {
    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
}
