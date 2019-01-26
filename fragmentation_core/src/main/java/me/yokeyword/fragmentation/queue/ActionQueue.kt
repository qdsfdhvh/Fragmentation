package me.yokeyword.fragmentation.queue

import android.os.Handler
import android.os.Looper
import me.yokeyword.fragmentation.SupportHelperKtx
import me.yokeyword.fragmentation.getBackStackTopFragment
import java.util.*

class ActionQueue(private val mainHandler: Handler) {

    private val queue by lazy { LinkedList<Action>() }

    fun enqueue(action: Action) {
        if (isThrottleBACK(action)) return

        if (action.action == Action.Type.LOAD
            || queue.isEmpty() && Thread.currentThread() == Looper.getMainLooper().thread) {
            action.run()
            return
        }

        mainHandler.post {
            enqueueAction(action)
        }
    }

    private fun enqueueAction(action: Action) {
        queue.add(action)
        if (queue.size == 1) {
            handleAction()
        }
    }

    private fun handleAction() {
        if (queue.isEmpty()) return

        val action = queue.peek()
        action.run()

        executeNextAction(action)
    }

    private fun executeNextAction(action: Action) {
        if (action.action == Action.Type.POP) {
            val top = action.fragmentManager.getBackStackTopFragment()
            action.duration = top?.supportDelegate?.exitAnimDuration ?: Action.DEFAULT_POP_TIME
        }
        mainHandler.postDelayed({
            queue.poll()
            handleAction()
        }, action.duration)
    }

    private fun isThrottleBACK(action: Action): Boolean {
        if (action.action == Action.Type.BACK) {
            val head = queue.peek()
            return head != null && head.action == Action.Type.POP
        }
        return false
    }
}