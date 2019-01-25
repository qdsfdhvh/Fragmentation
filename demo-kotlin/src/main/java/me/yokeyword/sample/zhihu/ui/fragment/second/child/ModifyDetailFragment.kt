package me.yokeyword.sample.zhihu.ui.fragment.second.child

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_modify_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.hideSoftInput
import me.yokeyword.fragmentation.start
import me.yokeyword.sample.R
import me.yokeyword.sample.base.BaseFragment
import me.yokeyword.sample.base.initToolbarNav
import me.yokeyword.sample.base.toast
import me.yokeyword.sample.zhihu.ui.fragment.CycleFragment

/**
 * Created by YoKeyword on 16/2/7.
 */
class ModifyDetailFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbarNav(toolbar, R.string.start_result_test)

        val mTitle = arguments?.getString(ARG_TITLE) ?: ""
        et_modify_title!!.setText(mTitle)

        btn_modify.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(DetailFragment.KEY_RESULT_TITLE, et_modify_title.text.toString())
            setFragmentResult(ISupportFragment.RESULT_OK, bundle)

            toast(R.string.modify_success)
        }
        btn_next.setOnClickListener {
            start(CycleFragment.newInstance(1))
        }
    }

    override fun getLayoutId() = R.layout.fragment_modify_detail

    override fun onSupportInvisible() {
        super.onSupportInvisible()
        hideSoftInput()
    }

    companion object {
        private const val ARG_TITLE = "arg_title"

        fun newInstance(title: String): ModifyDetailFragment {
            val args = Bundle()

            val fragment = ModifyDetailFragment()
            args.putString(ARG_TITLE, title)
            fragment.arguments = args
            return fragment
        }
    }
}
