package me.yokeyword.sample.base

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import me.yokeyword.fragmentation.SupportActivity
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.sample.R

fun SupportActivity.toast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun SupportActivity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
