package me.yokeyword.sample.wechat.base

import android.widget.Toast

import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.sample.R

/**
 * 懒加载
 * Created by YoKeyword on 16/6/5.
 */
abstract class BaseMainFragment : SupportFragment() {
    private var lastExitTime: Long = 0

    /**
     * 处理回退事件
     *
     * @return
     */
    override fun onBackPressedSupport(): Boolean {
        if (System.currentTimeMillis() - lastExitTime < WAIT_TIME) {
            ctx?.finish()
        } else {
            lastExitTime = System.currentTimeMillis()
            Toast.makeText(ctx, R.string.press_again_exit, Toast.LENGTH_SHORT).show()
        }
        return true
    }

    companion object {
        // 再点一次退出程序时间设置
        private const val WAIT_TIME = 2000L
    }
}
