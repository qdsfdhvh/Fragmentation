package me.yokeyword.fragmentation.helper.internal

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import me.yokeyword.fragmentation.*

class VisibleDelegate(private val support: ISupportFragment,
                      private val fragment: Fragment) {

    constructor(support: ISupportFragment): this(support, support as Fragment)

    private val handler by lazy { Handler(Looper.getMainLooper()) }

    private var isFirstVisible = true   // 是否第一次显示
    private var supportVisible = false  // 是否正在显示

    private var needDispatch = true
    private var invisibleWhenLeave = false
    private var firstCreateViewCompatReplace = true

    private var saveInstanceState: Bundle? = null

    fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) return

        // setUserVisibleHint() may be called before onCreate()
        saveInstanceState = savedInstanceState
        invisibleWhenLeave = savedInstanceState.getBoolean(FRAGMENTATION_STATE_SAVE_IS_INVISIBLE_WHEN_LEAVE)
        firstCreateViewCompatReplace = savedInstanceState.getBoolean(FRAGMENTATION_STATE_SAVE_COMPAT_REPLACE)
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(FRAGMENTATION_STATE_SAVE_IS_INVISIBLE_WHEN_LEAVE, invisibleWhenLeave)
        outState.putBoolean(FRAGMENTATION_STATE_SAVE_COMPAT_REPLACE, firstCreateViewCompatReplace)
    }

    fun onActivityCreated() {
        if (!firstCreateViewCompatReplace && fragment.tag?.startsWith("android:switcher:") == true) {
            return
        }

        if (firstCreateViewCompatReplace) {
            firstCreateViewCompatReplace = false
        }

        if (!invisibleWhenLeave && fragment.isUserVisible() && fragment.isParentUserVisible()) {
            needDispatch = false
            safeDispatchUserVisibleHint(true)
        }
    }

    fun onResume() {
        if (!isFirstVisible) {
            if (!supportVisible && !invisibleWhenLeave && fragment.isUserVisible()) {
                needDispatch = false
                dispatchSupportVisible(true)
            }
        }
    }

    fun onPause() {
        if (supportVisible && fragment.isUserVisible()) {
            needDispatch = false
            invisibleWhenLeave = false
            dispatchSupportVisible(false)
        } else {
            invisibleWhenLeave = true
        }
    }

    fun onHiddenChanged(hidden: Boolean) {
        if (!hidden && !fragment.isResumed) {
            //if fragment is shown but not resumed, ignore...
            invisibleWhenLeave = false
            return
        }
        if (hidden) {
            safeDispatchUserVisibleHint(false)
        } else {
            enqueueDispatchVisible()
        }
    }

    fun onDestroyView() {
        isFirstVisible = true
    }

    fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (fragment.isResumed || (!fragment.isAdded && isVisibleToUser)) {
            if (!supportVisible && isVisibleToUser) {
                safeDispatchUserVisibleHint(true)
            }
            else if (supportVisible && !isVisibleToUser) {
                dispatchSupportVisible(false)
            }
        }
    }

    private fun safeDispatchUserVisibleHint(visible: Boolean) {
        if (isFirstVisible) {
            if (!visible) return
            enqueueDispatchVisible()
        } else {
            dispatchSupportVisible(visible)
        }
    }

    private fun enqueueDispatchVisible() {
        handler.post { dispatchSupportVisible(true) }
    }

    private fun dispatchSupportVisible(visible: Boolean) {
        if (visible && fragment.isParentSupportInvisible()) return

        if (supportVisible == visible) {
            needDispatch = true
            return
        }

        supportVisible = visible
        if (visible) {
            if (checkAddState()) return
            support.onSupportVisible()

            if (isFirstVisible) {
                isFirstVisible = false
                support.onLazyInitView(saveInstanceState)
            }
            dispatchChild(true)
        } else {
            dispatchChild(false)
            support.onSupportInvisible()
        }
    }

    private fun dispatchChild(visible: Boolean) {
        if (!needDispatch) {
            needDispatch = true
        } else {
            if (checkAddState()) return

            val fragments = fragment.getChildActiveFragments() ?: return
            if (fragments.isNullOrEmpty()) return
            for (child in fragments) {
                if (child is ISupportFragment && child.isUserVisible()) {
                    child.supportDelegate.visibleDelegate.dispatchSupportVisible(visible)
                }
            }
        }
    }

    private fun checkAddState(): Boolean {
        if (!fragment.isAdded) {
            supportVisible = !supportVisible
            return true
        }
        return false
    }

    fun isSupportVisible() = supportVisible

    companion object {
        private const val FRAGMENTATION_STATE_SAVE_IS_INVISIBLE_WHEN_LEAVE = "fragmentation_invisible_when_leave"
        private const val FRAGMENTATION_STATE_SAVE_COMPAT_REPLACE = "fragmentation_compat_replace"
    }
}