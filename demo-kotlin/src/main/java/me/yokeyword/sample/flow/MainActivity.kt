package me.yokeyword.sample.flow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast

import com.google.android.material.navigation.NavigationView

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import me.yokeyword.fragmentation.*
import me.yokeyword.fragmentation.anim.FragmentAnimator
import me.yokeyword.sample.R
import me.yokeyword.sample.base.toast
import me.yokeyword.sample.flow.base.BaseMainFragment
import me.yokeyword.sample.flow.ui.fragment.account.LoginFragment
import me.yokeyword.sample.flow.ui.fragment.discover.DiscoverFragment
import me.yokeyword.sample.flow.ui.fragment.home.HomeFragment
import me.yokeyword.sample.flow.ui.fragment.shop.ShopFragment

/**
 * 流程式demo  tip: 多使用右上角的"查看栈视图"
 * Created by YoKeyword on 16/1/29.
 */
class MainActivity : SupportActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    BaseMainFragment.OnFragmentOpenDrawerListener,
    LoginFragment.OnLoginSuccessListener {

    private var mLastTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = findFragment(HomeFragment::class.java)
        if (fragment == null) {
            loadRootFragment(R.id.fl_container, HomeFragment.newInstance())
        }
        initView()
    }

    /**
     * 设置动画，也可以使用setFragmentAnimator()设置
     */
//    override fun onCreateFragmentAnimator(): FragmentAnimator {
//        // 设置默认Fragment动画  默认竖向(和安卓5.0以上的动画相同)
//        return super.onCreateFragmentAnimator()
//        // 设置横向(和安卓4.x动画相同)
////        return FragmentAnimator.horizontalAnimator()
//        // 设置自定义动画
////        return FragmentAnimator(enter, popEnter, popExit, exit)
//    }

    private fun initView() {
        val toggle = ActionBarDrawerToggle(this, drawer_layout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setCheckedItem(R.id.nav_home)
        nav_view.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            drawer_layout.postDelayed(this::goLogin, 250)
        }

    }

    override fun onBackPressedSupport() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
            return
        }

        val top = getTopFragment()
        if (top is BaseMainFragment) {
            nav_view.setCheckedItem(R.id.nav_home)
        }

        if (supportFragmentManager.backStackEntryCount > 1) {
            pop()
        } else {
            if (System.currentTimeMillis() - mLastTime < WAIT_TIME) {
                finish()
            } else {
                mLastTime = System.currentTimeMillis()
                toast(R.string.press_again_exit)
            }
        }
    }

    /**
     * 打开抽屉
     */
    override fun onOpenDrawer() {
        if (!drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        drawer_layout.postDelayed({
            val top = getTopFragment() ?: return@postDelayed

            when(item.itemId) {
                R.id.nav_home -> {
                    val home = findFragment(HomeFragment::class.java) ?: return@postDelayed

                    val bundle = Bundle()
                    bundle.putString("from", "From:${top.javaClass.simpleName}")
                    home.putNewBundle(bundle)
                    top.start(home, ISupportFragment.SINGLETASK)
                }
                R.id.nav_discover -> {
                    val discover = findFragment(DiscoverFragment::class.java)

                    if (discover == null) {
                        top.startWithPopTo(DiscoverFragment.newInstance(), HomeFragment::class.java, false)
                    } else {
                        top.start(discover, ISupportFragment.SINGLETASK)
                    }
                }
                R.id.nav_shop -> {
                    val shop = findFragment(ShopFragment::class.java)

                    if (shop == null) {
                        top.startWithPopTo(ShopFragment.newInstance(), HomeFragment::class.java, false)
                    } else {
                        top.start(shop, ISupportFragment.SINGLETASK)
                    }
                }
                R.id.nav_login -> {
                    goLogin()
                }
                R.id.nav_swipe_back -> {
                    startActivity(Intent(this, SwipeBackSampleActivity::class.java))
                }
            }

        }, 300)
        return true
    }

    private fun goLogin() {
        start(LoginFragment.newInstance())
    }

    override fun onLoginSuccess(account: String) {
        tv_name.text = account
        img_nav.setImageResource(R.drawable.ic_account_circle_white_48dp)
        toast(R.string.sign_in_success)
    }

    companion object {
        // 再点一次退出程序时间设置
        private const val WAIT_TIME = 2000L

        fun newInstance(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }

}
