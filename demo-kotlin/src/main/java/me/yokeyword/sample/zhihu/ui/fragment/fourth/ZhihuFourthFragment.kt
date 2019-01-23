package me.yokeyword.sample.zhihu.ui.fragment.fourth

import android.os.Bundle
import kotlinx.android.synthetic.main.zhihu_fragment_fourth.*
import me.yokeyword.fragmentation.findChildFragment
import me.yokeyword.fragmentation.loadRootFragment
import me.yokeyword.sample.R
import me.yokeyword.sample.zhihu.base.BaseMainFragment
import me.yokeyword.sample.zhihu.ui.fragment.fourth.child.AvatarFragment
import me.yokeyword.sample.zhihu.ui.fragment.fourth.child.MeFragment

/**
 * Created by YoKeyword on 16/6/3.
 */
class ZhihuFourthFragment : BaseMainFragment() {

    override fun getLayoutId() = R.layout.zhihu_fragment_fourth

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        loadFragment()
        toolbar.setTitle(R.string.me)
    }

    private fun loadFragment() {
        if (findChildFragment(AvatarFragment::class.java) != null) return
        loadRootFragment(R.id.fl_fourth_container_upper, AvatarFragment.newInstance())
        loadRootFragment(R.id.fl_fourth_container_lower, MeFragment.newInstance())
    }

    fun onBackToFirstFragment() {
        backToFirstListener?.onBackToFirstFragment()
    }

    companion object {
        fun newInstance(): ZhihuFourthFragment {
            val args = Bundle()

            val fragment = ZhihuFourthFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
