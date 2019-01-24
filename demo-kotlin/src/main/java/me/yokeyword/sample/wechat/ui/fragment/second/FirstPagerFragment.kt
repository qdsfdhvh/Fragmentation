package me.yokeyword.sample.wechat.ui.fragment.second

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
import me.yokeyword.sample.wechat.adapter.PagerAdapter
import me.yokeyword.sample.wechat.event.TabSelectedEvent
import me.yokeyword.sample.wechat.ui.fragment.MainFragment

/**
 * Created by YoKeyword on 16/6/30.
 */
class FirstPagerFragment : SupportFragment(), SwipeRefreshLayout.OnRefreshListener {
    private var mRefreshLayout: SwipeRefreshLayout? = null
    private var mRecy: RecyclerView? = null
    private var mAdapter: PagerAdapter? = null

    private var mInAtTop = true
    private var mScrollTotal: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.wechat_fragment_tab_second_pager_first, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        EventBusActivityScope.getDefault(ctx).register(this)

        mRecy = view.findViewById<View>(R.id.recy) as RecyclerView
        mRefreshLayout = view.findViewById<View>(R.id.refresh_layout) as SwipeRefreshLayout

        mRefreshLayout!!.setOnRefreshListener(this)

        mAdapter = PagerAdapter(ctx)
        mRecy!!.setHasFixedSize(true)
        val manager = LinearLayoutManager(ctx)
        mRecy!!.layoutManager = manager
        mRecy!!.adapter = mAdapter

        mRecy!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mScrollTotal += dy
                mInAtTop = mScrollTotal <= 0
            }
        })

        mAdapter!!.setOnItemClickListener { _, _, _ ->
            // 通知MainFragment跳转至NewFeatureFragment
            parentStart(NewFeatureFragment.newInstance())
        }

        // Init Datas
        val items = ArrayList<String>()
        for (i in 0..19) {
            val item = "New features"
            items.add(item)
        }
        mAdapter!!.setDatas(items)
    }

    override fun onRefresh() {
        mRefreshLayout!!.postDelayed({ mRefreshLayout!!.isRefreshing = false }, 2500)
    }

    /**
     * Reselected Tab
     */
    @Subscribe
    fun onTabSelectedEvent(event: TabSelectedEvent) {
        if (event.position != MainFragment.SECOND) return

        if (mInAtTop) {
            mRefreshLayout!!.isRefreshing = true
            onRefresh()
        } else {
            scrollToTop()
        }
    }

    private fun scrollToTop() {
        mRecy!!.smoothScrollToPosition(0)
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
