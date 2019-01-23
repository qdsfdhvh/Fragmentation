package me.yokeyword.sample.zhihu.base

import android.content.Context

import me.yokeyword.fragmentation.popChild
import me.yokeyword.sample.base.BaseFragment
import me.yokeyword.sample.zhihu.ui.fragment.first.ZhiHuFirstFragment

/**
 * 懒加载
 * Created by YoKeyword on 16/6/5.
 */
abstract class BaseMainFragment : BaseFragment() {

    protected var backToFirstListener: OnBackToFirstListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnBackToFirstListener) {
            backToFirstListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnBackToFirstListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        backToFirstListener = null
    }

    /**
     * 处理回退事件
     *
     * @return
     */
    override fun onBackPressedSupport(): Boolean {
        if (childFragmentManager.backStackEntryCount > 1) {
            popChild()
        } else {
            if (this is ZhiHuFirstFragment) {   // 如果是 第一个Fragment 则退出app
                ctx?.finish()
            } else {                                    // 如果不是,则回到第一个Fragment
                backToFirstListener!!.onBackToFirstFragment()
            }
        }
        return true
    }

    interface OnBackToFirstListener {
        fun onBackToFirstFragment()
    }
}
