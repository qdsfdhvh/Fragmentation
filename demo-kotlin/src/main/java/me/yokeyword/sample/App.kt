package me.yokeyword.sample

import android.app.Application

import me.yokeyword.fragmentation.Fragmentation
import me.yokeyword.fragmentation.helper.ExceptionHandler

/**
 * Created by YoKey on 16/11/23.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Fragmentation.Builder()
            /*
             * 设置 栈视图 模式:
             *   BUBBLE -> 悬浮球模式
             *   SHAKE  -> 摇一摇唤出
             *   NONE   -> 隐藏， 仅在Debug环境生效
             */
            .stackViewMode(Fragmentation.StackViewMode.BUBBLE)
            /*
             * 是否调试
             */
            .debug(BuildConfig.DEBUG)
            /**
             * 可以获取到[me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning]
             * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
             */
            .handleException(object : ExceptionHandler {
                override fun onException(e: Exception) {
                    // 以BugTags为例子: 把捕获到的 Exception 传到 BugTags 后台。
                    // BugTags.sendException(e);
                }
            })
            .install()
    }
}
