package me.yokeyword.sample.zhihu.ui.fragment.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.yokeyword.fragmentation.findChildFragment
import me.yokeyword.fragmentation.loadRootFragment
import me.yokeyword.sample.R
import me.yokeyword.sample.zhihu.base.BaseMainFragment
import me.yokeyword.sample.zhihu.ui.fragment.second.child.ViewPagerFragment

/**
 * Created by YoKeyword on 16/6/3.
 */
class ZhihuSecondFragment : BaseMainFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.zhihu_fragment_second, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (findChildFragment(ViewPagerFragment::class.java) == null) {
            loadRootFragment(R.id.fl_second_container, ViewPagerFragment.newInstance())
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        // 这里可以不用懒加载,因为Adapter的场景下,Adapter内的子Fragment只有在父Fragment是show状态时,才会被Attach,Create
    }

    companion object {

        fun newInstance(): ZhihuSecondFragment {

            val args = Bundle()

            val fragment = ZhihuSecondFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
