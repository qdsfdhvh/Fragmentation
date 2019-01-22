package me.yokeyword.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_enter.*
import kotlinx.android.synthetic.main.toolbar.*
import me.yokeyword.sample.flow.MainActivity

/**
 * Created by YoKeyword on 16/6/5.
 */
class EnterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)
        initView()
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        btn_flow.setOnClickListener {
            MainActivity.newInstance(this)
        }
        btn_wechat.setOnClickListener {
            me.yokeyword.sample.wechat.MainActivity.newInstance(this)
        }
        btn_zhihu.setOnClickListener {
            me.yokeyword.sample.zhihu.MainActivity.newInstance(this)
        }
    }
}
