package me.yokeyword.fragmentation.helper.internal

import android.view.View

data class TransactionRecord(
    var tag: String? = null,
    var targetFragmentEnter: Int = Integer.MIN_VALUE,
    var currentFragmentPopExit: Int = Integer.MIN_VALUE,
    var currentFragmentPopEnter: Int = Integer.MIN_VALUE,
    var targetFragmentExit: Int = Integer.MIN_VALUE,
    var dontAddToBackStack: Boolean = false,
    val sharedElementList: ArrayList<SharedElement> = ArrayList()
) {
    fun add(sharedElement: View, sharedName: String) {
        sharedElementList.add(SharedElement(sharedElement, sharedName))
    }

    data class SharedElement(
        val sharedElement: View,
        val sharedName: String
    )
}