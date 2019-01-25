package me.yokeyword.sample.zhihu.ui.fragment

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_cycle.*
import kotlinx.android.synthetic.main.toolbar.*
import me.yokeyword.fragmentation.start
import me.yokeyword.fragmentation.startWithPop
import me.yokeyword.sample.R
import me.yokeyword.sample.base.BaseFragment
import me.yokeyword.sample.base.initToolbarNav

/**
 * Created by YoKeyword on 16/2/7.
 */
class CycleFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val num = arguments?.getInt(ARG_NUMBER) ?: 0
        val title = "CycleFragment $num"
        initToolbarNav(toolbar, title)
        tv_name.text = title

        btn_next_with_finish.setOnClickListener {
            startWithPop(CycleFragment.newInstance(num + 1))
        }
        btn_next.setOnClickListener {
            start(CycleFragment.newInstance(num + 1))
        }
    }

    override fun getLayoutId() = R.layout.fragment_cycle

    companion object {
        private const val ARG_NUMBER = "arg_number"

        fun newInstance(number: Int): CycleFragment {
            val args = Bundle()
            args.putInt(ARG_NUMBER, number)

            val fragment = CycleFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
