package me.yokeyword.sample.flow.ui.fragment_swipe_back

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.yokeyword.fragmentation.start
import me.yokeyword.sample.R
import me.yokeyword.sample.flow.adapter.PagerAdapter


class RecyclerSwipeBackFragment : BaseSwipeBackFragment() {

    private lateinit var mToolbar: Toolbar

    private var mRecy: RecyclerView? = null
    private var mAdapter: PagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_swipe_back_recy, container, false)

        initView(view)

        return attachToSwipeBack(view)
    }

    private fun initView(view: View) {
        mRecy = view.findViewById<View>(R.id.recy) as RecyclerView

        mToolbar = view.findViewById(R.id.toolbar)
        _initToolbar(mToolbar)

        mAdapter = PagerAdapter(ctx)
        val manager = LinearLayoutManager(ctx)
        mRecy!!.layoutManager = manager
        mRecy!!.adapter = mAdapter

        mAdapter!!.setOnItemClickListener { _, _ -> start(FirstSwipeBackFragment.newInstance()) }

        // Init Datas
        val items = ArrayList<String>()
        for (i in 0..19) {
            val item: String
            item = getString(R.string.favorite) + " " + i
            items.add(item)
        }
        mAdapter!!.setDatas(items)
    }

    companion object {
        private const val ARG_FROM = "arg_from"

        fun newInstance(): RecyclerSwipeBackFragment {
            val fragment = RecyclerSwipeBackFragment()
            return fragment
        }
    }
}
