package me.yokeyword.sample.wechat.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import me.yokeyword.fragmentation.start
import me.yokeyword.fragmentation.startWithPop
import me.yokeyword.sample.R
import me.yokeyword.sample.wechat.base.BaseBackFragment

/**
 * Created by YoKeyword on 16/2/7.
 */
class CycleFragment : BaseBackFragment() {

    private lateinit var mToolbar: Toolbar
    private var mTvName: TextView? = null
    private var mBtnNext: Button? = null
    private var mBtnNextWithFinish: Button? = null

    private var mNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {
            mNumber = args.getInt(ARG_NUMBER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cycle, container, false)
        initView(view)
        return attachToSwipeBack(view)
    }

    @SuppressLint("SetTextI18n")
    private fun initView(view: View) {
        mToolbar = view.findViewById<View>(R.id.toolbar) as Toolbar
        mTvName = view.findViewById<View>(R.id.tv_name) as TextView
        mBtnNext = view.findViewById<View>(R.id.btn_next) as Button
        mBtnNextWithFinish = view.findViewById<View>(R.id.btn_next_with_finish) as Button

        val title = "CyclerFragment $mNumber"

        mToolbar.title = title
        initToolbarNav(mToolbar)

        mTvName!!.text = title + "\n" + getString(R.string.can_swipe)
        mBtnNext!!.setOnClickListener { start(CycleFragment.newInstance(mNumber + 1)) }
        mBtnNextWithFinish!!.setOnClickListener { startWithPop(CycleFragment.newInstance(mNumber + 1)) }
    }

    companion object {
        private const val ARG_NUMBER = "arg_number"

        fun newInstance(number: Int): CycleFragment {
            val fragment = CycleFragment()
            val args = Bundle()
            args.putInt(ARG_NUMBER, number)
            fragment.arguments = args
            return fragment
        }
    }
}