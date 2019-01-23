package me.yokeyword.sample.zhihu.ui.fragment.first

import android.os.Bundle
import me.yokeyword.fragmentation.findChildFragment
import me.yokeyword.fragmentation.loadRootFragment
import me.yokeyword.sample.R
import me.yokeyword.sample.zhihu.base.BaseMainFragment
import me.yokeyword.sample.zhihu.ui.fragment.first.child.FirstHomeFragment

/**
 * Created by YoKeyword on 16/6/3.
 */
class ZhiHuFirstFragment : BaseMainFragment() {

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        if (findChildFragment(FirstHomeFragment::class.java) == null) {
            loadRootFragment(R.id.fl_first_container, FirstHomeFragment.newInstance())
        }
    }

    override fun getLayoutId() = R.layout.zhihu_fragment_first

    companion object {

        fun newInstance(): ZhiHuFirstFragment {
            val args = Bundle()

            val fragment = ZhiHuFirstFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
