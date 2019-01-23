package me.yokeyword.sample.zhihu.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.fragment_cycle.*
import kotlinx.android.synthetic.main.toolbar.*
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.start
import me.yokeyword.fragmentation.startWithPop
import me.yokeyword.sample.R
import me.yokeyword.sample.base.BaseFragment
import me.yokeyword.sample.base.initToolbarNav

/**
 * Created by YoKeyword on 16/2/7.
 */
class CycleFragment : BaseFragment() {

    private var mNumber: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = "CycleFragment $mNumber"
        initToolbarNav(toolbar, title)
        tv_name.text = title

        btn_next_with_finish.setOnClickListener {
            startWithPop(CycleFragment.newInstance(mNumber + 1))
        }
        btn_next.setOnClickListener {
            start(CycleFragment.newInstance(mNumber + 1))
        }
    }

    override fun getLayoutId() = R.layout.fragment_cycle

    companion object {
        private const val ARG_NUMBER = "arg_number"

        fun newInstance(number: Int): CycleFragment {
            val fragment = CycleFragment()
            val args = Bundle()
            args.putInt(ARG_NUMBER, number)
            fragment.arguments = args
            return fragment
        }
    }
}
