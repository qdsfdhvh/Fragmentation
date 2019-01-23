package me.yokeyword.sample.wechat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import me.yokeyword.fragmentation.SupportActivity
import me.yokeyword.fragmentation.anim.FragmentAnimator
import me.yokeyword.fragmentation.findFragment
import me.yokeyword.fragmentation.loadRootFragment
import me.yokeyword.sample.R
import me.yokeyword.sample.wechat.ui.fragment.MainFragment

/**
 * 仿微信交互方式Demo
 * Created by YoKeyword on 16/6/30.
 */
class MainActivity : SupportActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wechat_activity_main)

        if (findFragment(MainFragment::class.java) == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance())
        }
    }

    override fun onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport()
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        // 设置横向(和安卓4.x动画相同)
        return FragmentAnimator.horizontalAnimator()
    }

    companion object {
        fun newInstance(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
