package me.yokeyword.fragmentation.queue


import androidx.fragment.app.FragmentManager

/**
 * Created by YoKey on 17/12/28.
 */

abstract class Action(val action: Type, val fragmentManager: FragmentManager? = null): Runnable {

    /*
     * To Java
     */
    constructor(action: Type): this (action, null)

    constructor(): this (Type.NORMAL)

    var duration: Long = 0

    enum class Type {
        NORMAL,
        POP,
        POP_MOCK,
        BACK,
        LOAD
    }

    companion object {
        const val DEFAULT_POP_TIME = 300L
    }
}
