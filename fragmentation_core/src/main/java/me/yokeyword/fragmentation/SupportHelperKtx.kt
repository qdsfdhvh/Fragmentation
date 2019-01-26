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
        return fm.getTopFragment(containerId)
    }

    /**
     * 获取目标Fragment的前一个SupportFragment
     *
     * @param fragment 目标Fragment
     */
    fun getPreFragment(fragment: Fragment): ISupportFragment? {
        return fragment.getPreFragment()
    }

//    /**
//     * 寻找目标Fragment
//     */
//    fun <T : ISupportFragment> findFragment(fm: FragmentManager?, clazz: Class<T>): T? {
//        return fm.findFragment(clazz)
//    }

//    /**
//     * 寻找目标Fragment
//     */
//    fun <T : ISupportFragment> findFragment(fm: FragmentManager, tag: String?): T? {
//        return fm.findFragment(tag)
//    }

//    fun getActiveFragment(fm: FragmentManager, parent: ISupportFragment? = null): ISupportFragment? {
//        return fm.getActiveFragment(parent)
//    }

    fun getBackStackTopFragment(fm: FragmentManager?, containerId: Int): ISupportFragment? {
        return fm.getBackStackTopFragment(containerId)
    }

    fun <T : ISupportFragment> findBackStackFragment(fm: FragmentManager, tag: String?, clazz: Class<T>): T? {
        return fm.findBackStackFragment(tag, clazz)
    }

    fun getWillPopFragments(fm: FragmentManager, tag: String, includeTarget: Boolean): List<Fragment> {
        return fm.getWillPopFragments(tag, includeTarget)
    }

}