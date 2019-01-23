package me.yokeyword.fragmentation.anim

import android.os.Parcelable
import androidx.annotation.AnimRes
import kotlinx.android.parcel.Parcelize
import me.yokeyword.fragmentation.R

@Parcelize
data class FragmentAnimator(
    @AnimRes val enter: Int,
    @AnimRes val exit: Int,
    @AnimRes val popEnter: Int,
    @AnimRes val popExit: Int
): Parcelable {

    fun copy() = FragmentAnimator(enter, exit, popEnter, popExit)

    companion object {
        fun noAnimator() = FragmentAnimator(
            enter = 0,
            exit = 0,
            popEnter = 0,
            popExit = 0)

        fun horizontalAnimator() = FragmentAnimator(
            enter = R.anim.h_fragment_enter,
            exit = R.anim.h_fragment_exit,
            popEnter = R.anim.h_fragment_pop_enter,
            popExit = R.anim.h_fragment_pop_exit)

        fun verticalAnimator() = FragmentAnimator(
            enter = R.anim.v_fragment_enter,
            exit = R.anim.v_fragment_exit,
            popEnter = R.anim.v_fragment_pop_enter,
            popExit = R.anim.v_fragment_pop_exit)
    }
}