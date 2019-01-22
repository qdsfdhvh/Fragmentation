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
import me.yokeyword.eventbusactivityscope.EventBusActivityScope
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.parentStart
import me.yokeyword.sample.R
import me.yokeyword.sample.zhihu.MainActivity
import me.yokeyword.sample.zhihu.adapter.HomeAdapter
import me.yokeyword.sample.zhihu.entity.Article
import me.yokeyword.sample.zhihu.event.TabSelectedEvent
import me.yokeyword.sample.zhihu.ui.fragment.second.child.DetailFragment

/**
 * Created by YoKeyword on 16/6/3.
 */
class FirstPagerFragment : SupportFragment(), SwipeRefreshLayout.OnRefreshListener {
    private var mRecy: RecyclerView? = null
    private var mRefreshLayout: SwipeRefreshLayout? = null
    private var mAdapter: HomeAdapter? = null
    private var mAtTop = true
    private var mScrollTotal: Int = 0
    private var mTitles: Array<String>? = null
    private var mContents: Array<String>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.zhihu_fragment_second_pager_first, container, false)
        EventBusActivityScope.getDefault(ctx).register(this)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        mRecy = view.findViewById<View>(R.id.recy) as RecyclerView
        mRefreshLayout = view.findViewById<View>(R.id.refresh_layout) as SwipeRefreshLayout

        mTitles = resources.getStringArray(R.array.array_title)
        mContents = resources.getStringArray(R.array.array_content)

        mRefreshLayout!!.setColorSchemeResources(R.color.colorPrimary)
        mRefreshLayout!!.setOnRefreshListener(this)

        mAdapter = HomeAdapter(ctx)
        val manager = LinearLayoutManager(ctx)
        mRecy!!.layoutManager = manager
        mRecy!!.adapter = mAdapter

        mAdapter!!.setOnItemClickListener { position, _, _ ->
            // 这里的DetailFragment在flow包里
            // 这里是父Fragment启动,要注意 栈层级
            parentStart(DetailFragment.newInstance(mAdapter!!.getItem(position).title))
        }

        // Init Datas
        val articleList = ArrayList<Article>()
        for (i in 0..14) {
            val index = (Math.random() * 3).toInt()
            val article = Article(mTitles!![index], mContents!![index])
            articleList.add(article)
        }
        mAdapter!!.setDatas(articleList)

        mRecy!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mScrollTotal += dy
                mAtTop = mScrollTotal <= 0
            }
        })
    }

    override fun onRefresh() {
        mRefreshLayout!!.postDelayed({ mRefreshLayout!!.isRefreshing = false }, 2000)
    }

    private fun scrollToTop() {
        mRecy!!.smoothScrollToPosition(0)
    }

    /**
     * 选择tab事件
     */
    @Subscribe
    fun onTabSelectedEvent(event: TabSelectedEvent) {
        if (event.position != MainActivity.SECOND) return

        if (mAtTop) {
            mRefreshLayout!!.isRefreshing = true
            onRefresh()
        } else {
            scrollToTop()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBusActivityScope.getDefault(ctx).unregister(this)
    }

    companion object {

        fun newInstance(): FirstPagerFragment {

            val args = Bundle()

            val fragment = FirstPagerFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
