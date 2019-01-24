package me.yokeyword.sample.zhihu.ui.fragment.second.child.childpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.greenrobot.eventbus.Subscribe

import java.util.ArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.zhihu_fragment_first_home.*
import me.yokeyword.eventbusactivityscope.EventBusActivityScope
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.parentStart
import me.yokeyword.sample.R
import me.yokeyword.sample.base.EventBusBaseFragment
import me.yokeyword.sample.base.lazyAndroid
import me.yokeyword.sample.zhihu.MainActivity
import me.yokeyword.sample.zhihu.adapter.HomeAdapter
import me.yokeyword.sample.zhihu.entity.Article
import me.yokeyword.sample.zhihu.event.TabSelectedEvent
import me.yokeyword.sample.zhihu.listener.OnItemClickListener
import me.yokeyword.sample.zhihu.ui.fragment.second.child.DetailFragment

/**
 * Created by YoKeyword on 16/6/3.
 */
class FirstPagerFragment : EventBusBaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    private var mAtTop = true
    private var mScrollTotal: Int = 0

    private val mAdapter by lazyAndroid {
        HomeAdapter(ctx!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View, vh: RecyclerView.ViewHolder) {
                parentStart(DetailFragment.newInstance(mAdapter.getItem(position).title))
            }
        })
        refresh_layout.setColorSchemeResources(R.color.colorPrimary)
        refresh_layout.setOnRefreshListener(this)
        recy.layoutManager = LinearLayoutManager(ctx)
        recy.adapter = mAdapter

        // Init Datas
        val titles = resources.getStringArray(R.array.array_title)
        val contents = resources.getStringArray(R.array.array_content)
        val articleList = MutableList(14) {
            val index = (Math.random() * 3).toInt()
            Article(titles[index], contents[index])
        }
        mAdapter.setItems(articleList)

        recy.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mScrollTotal += dy
                mAtTop = mScrollTotal <= 0
            }
        })
    }

    override fun onRefresh() {
        refresh_layout.postDelayed({ refresh_layout.isRefreshing = false }, 2000)
    }

    private fun scrollToTop() {
        recy.smoothScrollToPosition(0)
    }

    /**
     * 选择tab事件
     */
    @Subscribe
    fun onTabSelectedEvent(event: TabSelectedEvent) {
        if (event.position != MainActivity.SECOND) return

        if (mAtTop) {
            refresh_layout?.isRefreshing = true
            onRefresh()
        } else {
            scrollToTop()
        }
    }

    override fun getLayoutId() = R.layout.zhihu_fragment_second_pager_first

    companion object {
        fun newInstance(): FirstPagerFragment {
            val args = Bundle()

            val fragment = FirstPagerFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
