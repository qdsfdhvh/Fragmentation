package me.yokeyword.sample.zhihu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.zhihu_activity_main.*
import me.yokeyword.eventbusactivityscope.EventBusActivityScope
import me.yokeyword.fragmentation.*
import me.yokeyword.sample.R
import me.yokeyword.sample.zhihu.base.BaseMainFragment
import me.yokeyword.sample.zhihu.event.TabSelectedEvent
import me.yokeyword.sample.zhihu.ui.fragment.first.ZhiHuFirstFragment
import me.yokeyword.sample.zhihu.ui.fragment.first.child.FirstHomeFragment
import me.yokeyword.sample.zhihu.ui.fragment.fourth.ZhihuFourthFragment
import me.yokeyword.sample.zhihu.ui.fragment.fourth.child.MeFragment
import me.yokeyword.sample.zhihu.ui.fragment.second.ZhihuSecondFragment
import me.yokeyword.sample.zhihu.ui.fragment.second.child.ViewPagerFragment
import me.yokeyword.sample.zhihu.ui.fragment.third.ZhihuThirdFragment
import me.yokeyword.sample.zhihu.ui.fragment.third.child.ShopFragment
import me.yokeyword.sample.zhihu.ui.view.BottomBar
import me.yokeyword.sample.zhihu.ui.view.BottomBarTab

/**
 * 类知乎 复杂嵌套Demo tip: 多使用右上角的"查看栈视图"
 * Created by YoKeyword on 16/6/2.
 */
class MainActivity : SupportActivity(), BaseMainFragment.OnBackToFirstListener {

    private val mFragments = arrayOfNulls<SupportFragment>(4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zhihu_activity_main)

        val zhiHu = findFragment(ZhiHuFirstFragment::class.java)
        if (zhiHu == null) {
            mFragments[FIRST] = ZhiHuFirstFragment.newInstance()
            mFragments[SECOND] = ZhihuSecondFragment.newInstance()
            mFragments[THIRD] = ZhihuThirdFragment.newInstance()
            mFragments[FOURTH] = ZhihuFourthFragment.newInstance()

            loadRootFragments(R.id.fl_container, FIRST, mFragments)
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = zhiHu
            mFragments[SECOND] = findFragment(ZhihuSecondFragment::class.java)
            mFragments[THIRD] = findFragment(ZhihuThirdFragment::class.java)
            mFragments[FOURTH] = findFragment(ZhihuFourthFragment::class.java)
        }

        initView()
    }

    private fun initView() {
        bottomBar.addItem(BottomBarTab(this, R.drawable.ic_home_white_24dp))
            .addItem(BottomBarTab(this, R.drawable.ic_discover_white_24dp))
            .addItem(BottomBarTab(this, R.drawable.ic_message_white_24dp))
            .addItem(BottomBarTab(this, R.drawable.ic_account_circle_white_24dp))
        bottomBar.setOnTabSelectedListener(object : BottomBar.OnTabSelectedListener {

            override fun onTabSelected(position: Int, prePosition: Int) {
                showHideFragment(mFragments[position], mFragments[prePosition])
            }

            override fun onTabUnselected(position: Int) {

            }

            override fun onTabReselected(position: Int) {
                Log.d("TAG", "position = $position")
                val currentFragment = mFragments[position] ?: return
                val count = currentFragment.childFragmentManager.backStackEntryCount

                // 如果不在该类别Fragment的主页,则回到主页;
                if (count > 1) {
                    when (currentFragment) {
                        is ZhiHuFirstFragment -> currentFragment.popToChild(FirstHomeFragment::class.java, false)
                        is ZhihuSecondFragment -> currentFragment.popToChild(ViewPagerFragment::class.java, false)
                        is ZhihuThirdFragment -> currentFragment.popToChild(ShopFragment::class.java, false)
                        is ZhihuFourthFragment -> currentFragment.popToChild(MeFragment::class.java, false)
                    }
                    return
                }


                // 这里推荐使用EventBus来实现 -> 解耦
                if (count == 1) {
                    // 在FirstPagerFragment中接收, 因为是嵌套的孙子Fragment 所以用EventBus比较方便
                    // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                    EventBusActivityScope.getDefault(this@MainActivity).post(TabSelectedEvent(position))
                }
            }
        })
    }

    override fun onBackPressedSupport() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            pop()
        } else {
            ActivityCompat.finishAfterTransition(this)
        }
    }

    override fun onBackToFirstFragment() {
        bottomBar.setCurrentItem(0)
    }

    companion object {
        const val FIRST = 0
        const val SECOND = 1
        const val THIRD = 2
        const val FOURTH = 3

        fun newInstance(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }

    /**
     * 这里暂没实现,忽略
     */
    //    @Subscribe
    //    public void onHiddenBottombarEvent(boolean hidden) {
    //        if (hidden) {
    //            mBottomBar.hide();
    //        } else {
    //            mBottomBar.show();
    //        }
    //    }
}
