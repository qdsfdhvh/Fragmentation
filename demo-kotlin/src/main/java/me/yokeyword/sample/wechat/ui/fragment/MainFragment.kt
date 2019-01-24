package me.yokeyword.sample.wechat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.wechat_fragment_main.*
import me.yokeyword.eventbusactivityscope.EventBusActivityScope
import me.yokeyword.fragmentation.*
import me.yokeyword.sample.R
import me.yokeyword.sample.wechat.event.TabSelectedEvent
import me.yokeyword.sample.wechat.ui.fragment.first.WechatFirstTabFragment
import me.yokeyword.sample.wechat.ui.fragment.second.WechatSecondTabFragment
import me.yokeyword.sample.wechat.ui.fragment.third.WechatThirdTabFragment
import me.yokeyword.sample.wechat.ui.view.BottomBar
import me.yokeyword.sample.wechat.ui.view.BottomBarTab

/**
 * Created by YoKeyword on 16/6/30.
 */
class MainFragment : SupportFragment() {

    private val mFragments: Array<ISupportFragment?> = arrayOfNulls(3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.wechat_fragment_main, container, false)
        initView(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val firstFragment = findChildFragment(WechatFirstTabFragment::class.java)
        if (firstFragment == null) {
            mFragments[FIRST] = WechatFirstTabFragment.newInstance()
            mFragments[SECOND] = WechatSecondTabFragment.newInstance()
            mFragments[THIRD] = WechatThirdTabFragment.newInstance()

            loadRootFragments(R.id.fl_tab_container, FIRST, mFragments)
//            loadMultipleRootFragment(R.id.fl_tab_container, FIRST, mFragments[FIRST],
//                mFragments[SECOND], mFragments[THIRD])
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment
            mFragments[SECOND] = findChildFragment(WechatSecondTabFragment::class.java)
            mFragments[THIRD] = findChildFragment(WechatThirdTabFragment::class.java)
        }
    }

    private fun initView(view: View) {
        bottomBar
            .addItem(BottomBarTab(ctx, R.drawable.ic_message_white_24dp, getString(R.string.msg)))
            .addItem(BottomBarTab(ctx, R.drawable.ic_account_circle_white_24dp, getString(R.string.discover)))
            .addItem(BottomBarTab(ctx, R.drawable.ic_discover_white_24dp, getString(R.string.more)))

        // 模拟未读消息
        bottomBar.getItem(FIRST)!!.unreadCount = 9

        bottomBar.setOnTabSelectedListener(object : BottomBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int, prePosition: Int) {
                showHideFragment(mFragments[position], mFragments[prePosition])

                val tab = bottomBar.getItem(FIRST)
                if (position == FIRST) {
                    tab!!.unreadCount = 0
                } else {
                    tab!!.unreadCount = tab.unreadCount + 1
                }
            }

            override fun onTabUnselected(position: Int) {

            }

            override fun onTabReselected(position: Int) {
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                EventBusActivityScope.getDefault(ctx).post(TabSelectedEvent(position))
            }
        })
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        if (requestCode == REQ_MSG && resultCode == ISupportFragment.RESULT_OK) {

        }
    }

    companion object {
        private const val REQ_MSG = 10

        const val FIRST = 0
        const val SECOND = 1
        const val THIRD = 2

        fun newInstance(): MainFragment {
            val args = Bundle()

            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
