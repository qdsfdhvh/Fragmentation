package me.yokeyword.sample.flow.ui.fragment.shop

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_content.*
import me.yokeyword.fragmentation.anim.FragmentAnimator
import me.yokeyword.fragmentation.parentStart
import me.yokeyword.sample.R
import me.yokeyword.sample.base.BaseFragment
import me.yokeyword.sample.flow.ui.fragment.CycleFragment

/**
 * Created by YoKeyword on 16/2/9.
 */
class ContentFragment : BaseFragment() {

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_content.text = "Content:\n${arguments?.getString(ARG_MENU)}"

        btn_next.setOnClickListener {
            // 和MsgFragment同级别的跳转 交给MsgFragment处理
            parentStart(CycleFragment.newInstance(1))
        }
    }

//    override fun onBackPressedSupport(): Boolean {
//        // ContentFragment是ShopFragment的栈顶子Fragment,可以在此处理返回按键事件
//        return super.onBackPressedSupport()
//    }

    override fun onCreateFragmentAnimator() =  FragmentAnimator.noAnimator()

    override fun getLayoutId() = R.layout.fragment_content

    companion object {
        private const val ARG_MENU = "arg_menu"

        fun newInstance(menu: String): ContentFragment {
            val args = Bundle()
            args.putString(ARG_MENU, menu)

            val fragment = ContentFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
