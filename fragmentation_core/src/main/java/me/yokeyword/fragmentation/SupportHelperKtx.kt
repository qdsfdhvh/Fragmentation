package me.yokeyword.fragmentation

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentationMagician

object SupportHelperKtx {

    private const val SHOW_SPACE = 200L

    fun showSoftInput(view: View?) {
        val imm = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
        view.requestFocus()
        view.postDelayed({
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        }, SHOW_SPACE)
    }

    fun hideSoftInput(view: View?) {
        val imm = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 获得栈顶SupportFragment
     */
    fun getTopFragment(fm: FragmentManager, containerId: Int = 0): ISupportFragment? {
        val fragments = fm.getISupportFragments()
        if (fragments.isNullOrEmpty()) return null

        for (fa in fragments.reversed()) {
            if (containerId == 0) return fa
            if (fa.supportDelegate.mContainerId == containerId) return fa
        }
        return null
    }

    /**
     * 获取目标Fragment的前一个SupportFragment
     *
     * @param fragment 目标Fragment
     */
    fun getPreFragment(fragment: Fragment): ISupportFragment? {
        val fragments = fragment.fragmentManager.getActiveFragments()
        if (fragments.isNullOrEmpty()) return null

        val index = fragments.indexOf(fragment)
        for (i in index - 1 downTo 0) {
            val pre = fragments[i]
            if (pre is ISupportFragment) {
                return pre
            }
        }
        return null
    }

    /**
     * 寻找目标Fragment
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : ISupportFragment> findFragment(fm: FragmentManager?, clazz: Class<T>): T? {
        val fragments = fm.getISupportFragments()
        if (fragments.isNullOrEmpty()) return null

        return fragments.firstOrNull { it.javaClass.name == clazz.name } as? T
    }

    /**
     * 寻找目标Fragment
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : ISupportFragment> findFragment(fm: FragmentManager, tag: String?): T? {
        return fm.findFragmentByTag(tag) as? T
    }

    fun getActiveFragment(fm: FragmentManager, parent: ISupportFragment? = null): ISupportFragment? {
        val fragments = fm.getActiveFragments()
        if (fragments.isNullOrEmpty()) return parent

        for (fa in fragments.reversed()) {
            if (fa is ISupportFragment && fa.isResumed && !fa.isHidden && fa.userVisibleHint) {
                return getActiveFragment(fa.childFragmentManager, fa)
            }
        }
        return parent
    }

    fun getBackStackTopFragment(fm: FragmentManager, containerId: Int): ISupportFragment? {
        val count = fm.backStackEntryCount
        for (i in count - 1 downTo 0) {
            val entry = fm.getBackStackEntryAt(i)
            val fa = fm.findFragmentByTag(entry.name)
            if (fa is ISupportFragment) {
                if (containerId == 0) return fa
                if (fa.supportDelegate.mContainerId == containerId) return fa
            }
        }
        return null
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ISupportFragment> findBackStackFragment(fm: FragmentManager, tag: String?, clazz: Class<T>): T? {
        val bak = if (TextUtils.isEmpty(tag)) clazz.name else tag

        val count = fm.backStackEntryCount
        for (i in count - 1 downTo 0) {
            val entry = fm.getBackStackEntryAt(i)
            if (bak == entry.name) {
                val fa = fm.findFragmentByTag(entry.name)
                if (fa is ISupportFragment) return fa as? T
            }
        }
        return null
    }

    fun getWillPopFragments(fm: FragmentManager, tag: String, includeTarget: Boolean): List<Fragment> {
        val fragments = fm.getActiveFragments()
        if (fragments.isNullOrEmpty()) return emptyList()

        val target = fm.findFragmentByTag(tag)
        val size = fragments.size

        var startIndex = -1
        for (i in size - 1 downTo 0) {
            if (target == fragments[i]) {
                if (includeTarget) {
                    startIndex = i
                }
                else if (i + 1 < size) {
                    startIndex = i + 1
                }
                break
            }
        }

        if (startIndex == -1) return emptyList()

        val list = ArrayList<Fragment>(size)
        for (i in size - 1 downTo startIndex) {
            val fragment = fragments[i]
            if (fragment != null && fragment.view != null) {
                list.add(fragment)
            }
        }
        return list
    }

    private fun FragmentManager?.getActiveFragments(): List<Fragment?>? {
        if (this == null) return null
        return FragmentationMagician.getActiveFragments(this)
    }

    private fun FragmentManager?.getISupportFragments(): List<ISupportFragment>? {
        return getActiveFragments()
            ?.filter { it is ISupportFragment }
            ?.map { it as ISupportFragment }
    }

}