package me.yokeyword.sample.flow.ui.fragment.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_pager.*
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.parentStart
import me.yokeyword.sample.R
import me.yokeyword.sample.base.BaseFragment
import me.yokeyword.sample.flow.adapter.PagerAdapter
import me.yokeyword.sample.flow.ui.fragment.CycleFragment


class PagerChildFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PagerAdapter(ctx)
        recy.layoutManager = LinearLayoutManager(ctx)
        recy.adapter = adapter


        adapter.setOnItemClickListener { _, _ ->
            parentStart(CycleFragment.newInstance(1))
        }

        val from = arguments?.getInt(ARG_FROM) ?: 0

        recy.post {
            val items = ArrayList<String>()
            for (i in 0..19) {
                items.add(when(from) {
                    0 -> getString(R.string.recommend) + " " + i
                    1 -> getString(R.string.hot) + " " + i
                    else -> getString(R.string.favorite) + " " + i
                })
            }
            adapter.setDatas(items)
        }
    }

    override fun getLayoutId() = R.layout.fragment_pager

    companion object {
        private const val ARG_FROM = "arg_from"

        fun newInstance(from: Int): PagerChildFragment {
            val args = Bundle()
            args.putInt(ARG_FROM, from)

            val fragment = PagerChildFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
