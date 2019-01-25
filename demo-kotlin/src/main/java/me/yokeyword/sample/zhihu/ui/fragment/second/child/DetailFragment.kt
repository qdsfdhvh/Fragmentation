package me.yokeyword.sample.zhihu.ui.fragment.second.child

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.startForResult
import me.yokeyword.sample.R
import me.yokeyword.sample.base.BaseFragment
import me.yokeyword.sample.base.initToolbarNav
import me.yokeyword.sample.base.lazyAndroid
import me.yokeyword.sample.base.toast

/**
 * Created by YoKeyword on 16/2/3.
 */
class DetailFragment : BaseFragment() {

//    private lateinit var mToolbar: Toolbar
//    private var mTvContent: TextView? = null
//    private var mFab: FloatingActionButton? = null
//    private var mTitle: String? = null

    private val title by lazyAndroid {
        arguments?.getString(ARG_TITLE) ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbarNav(toolbar, title)
    }

    /**
     * 这里演示:
     * 比较复杂的Fragment页面会在第一次start时,导致动画卡顿
     * Fragmentation提供了onEnterAnimationEnd()方法,该方法会在 入栈动画 结束时回调
     * 所以在onCreateView进行一些简单的View初始化(比如 toolbar设置标题,返回按钮; 显示加载数据的进度条等),
     * 然后在onEnterAnimationEnd()方法里进行 复杂的耗时的初始化 (比如FragmentPagerAdapter的初始化 加载数据等)
     */
    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        initDelayView()
    }

    private fun initDelayView() {
        tv_content.setText(R.string.large_text)
        fab.setOnClickListener {
            startForResult(ModifyDetailFragment.newInstance(title), REQ_MODIFY_FRAGMENT)
        }
    }

    override fun getLayoutId() = R.layout.fragment_detail

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        when(requestCode) {
            REQ_MODIFY_FRAGMENT -> {
                if (resultCode == ISupportFragment.RESULT_OK && data != null) {
                    val value = data.getString(KEY_RESULT_TITLE, "")
                    tv_content.text = value
                    arguments?.putString(ARG_TITLE,  value)
                    toast(R.string.modify_title)
                }
            }
        }
    }

    companion object {
        private const val REQ_MODIFY_FRAGMENT = 100
        private const val ARG_TITLE = "arg_title"

        internal const val KEY_RESULT_TITLE = "title"

        fun newInstance(title: String): DetailFragment {
            val fragment = DetailFragment()
            val bundle = Bundle()
            bundle.putString(ARG_TITLE, title)
            fragment.arguments = bundle
            return fragment
        }
    }
}
