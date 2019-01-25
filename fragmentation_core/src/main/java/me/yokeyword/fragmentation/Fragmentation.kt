package me.yokeyword.fragmentation

import me.yokeyword.fragmentation.helper.ExceptionHandler

class Fragmentation {

    private var debug: Boolean = false
    private var mode: StackViewMode = StackViewMode.NONE
    private var handler: ExceptionHandler? = null

    fun isDebug() = debug

    fun getHandler() = handler

    fun getMode() = mode

    class Builder {

        private var debug = false
        private var mode = StackViewMode.NONE
        private var handler: ExceptionHandler? = null

        /**
         * @param debug Suppressed Exception("Can not perform this action after onSaveInstanceState!")
         *              when debug=false
         */
        fun debug(debug: Boolean): Builder {
            this.debug = debug
            return this
        }
        /**
         * Sets the mode to display the stack view
         *
         * None if debug(false).
         *
         * Default:NONE
         */
        fun stackViewMode(mode: StackViewMode): Builder {
            this.mode = mode
            return this
        }

        /**
         * @param handler Handled Exception("Can not perform this action after onSaveInstanceState!")
         *                when debug=false.
         */
        fun handleException(handler: ExceptionHandler?): Builder {
            this.handler = handler
            return this
        }

        fun install() {
            val default = getDefault()
            default.debug = debug
            default.mode = mode
            default.handler = handler
        }
    }

    private object Holder {
        var instance = Fragmentation()
    }

    companion object {
        fun getDefault() = Holder.instance
    }

    enum class StackViewMode {
        NONE,   // Do not display stack view
        SHAKE,  // Shake it to display stack view.
        BUBBLE  // As a bubble display stack view.
    }
}