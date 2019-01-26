package me.yokeyword.fragmentation

import androidx.fragment.app.Fragment

class SupportFragmentDelegateKtx(private val support: ISupportFragment) {

    private val fragment: Fragment

    init {
        if (support !is Fragment) {
            throw RuntimeException("Must extends Fragment")
        }
        fragment = support
    }

    private var rootStatus = Status.UN_ROOT




    enum class Status {
        UN_ROOT,
        ROOT_ANIM_DISABLE,
        ROOT_ANIM_ENABLE
    }

    companion object {

        private const val NOT_FOUND_ANIM_TIME = 300L
    }
}