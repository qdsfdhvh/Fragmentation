package me.yokeyword.sample.zhihu.ui.fragment.third

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.yokeyword.fragmentation.findChildFragment
import me.yokeyword.fragmentation.loadRootFragment
import me.yokeyword.sample.R
import me.yokeyword.sample.zhihu.base.BaseMainFragment
import me.yokeyword.sample.zhihu.ui.fragment.third.child.ShopFragment

/**
 * Created by YoKeyword on 16/6/3.
 */
class ZhihuThirdFragment : BaseMainFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.zhihu_fragment_third, container, false)
        return view
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        if (findChildFragment(ShopFragment::class.java) == null) {
            // ShopFragment是flow包里的
            loadRootFragment(R.id.fl_third_container, ShopFragment.newInstance())
        }
    }

    companion object {

        fun newInstance(): ZhihuThirdFragment {

            val args = Bundle()

            val fragment = ZhihuThirdFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
