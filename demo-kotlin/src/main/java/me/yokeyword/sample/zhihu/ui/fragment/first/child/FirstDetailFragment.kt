package me.yokeyword.sample.zhihu.ui.fragment.first.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.zhihu_fragment_first_detail.*
import me.yokeyword.fragmentation.start
import me.yokeyword.sample.R
import me.yokeyword.sample.base.BaseFragment
import me.yokeyword.sample.base.initToolbarNav
import me.yokeyword.sample.base.lazyAndroid
import me.yokeyword.sample.zhihu.entity.Article
import me.yokeyword.sample.zhihu.ui.fragment.CycleFragment

/**
 * Created by YoKeyword on 16/6/5.
 */
class FirstDetailFragment : BaseFragment() {

    private val article by lazyAndroid {
        arguments!!.getParcelable<Article>(ARG_ITEM)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbarNav(toolbar)

        img_detail.setImageResource(article.imgRes)
        tv_content.text = article.title
        fab.setOnClickListener {
            start(CycleFragment.newInstance(1))
        }
    }

    override fun getLayoutId() = R.layout.zhihu_fragment_first_detail

    companion object {
        private const val ARG_ITEM = "arg_item"

        fun newInstance(article: Article): FirstDetailFragment {

            val args = Bundle()
            args.putParcelable(ARG_ITEM, article)
            val fragment = FirstDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
