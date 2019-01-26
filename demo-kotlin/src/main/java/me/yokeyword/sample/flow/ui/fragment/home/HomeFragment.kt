package me.yokeyword.sample.flow.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import java.util.ArrayList

import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.yokeyword.fragmentation.ISupportActivity
import me.yokeyword.fragmentation.anim.FragmentAnimator
import me.yokeyword.fragmentation.start
import me.yokeyword.sample.R
import me.yokeyword.sample.base.toast
import me.yokeyword.sample.flow.adapter.HomeAdapter
import me.yokeyword.sample.flow.base.BaseMainFragment
import me.yokeyword.sample.flow.entity.Article


class HomeFragment : BaseMainFragment(), Toolbar.OnMenuItemClickListener {

    private var mTitles: Array<String>? = null

    private var mContents: Array<String>? = null

    private var mToolbar: Toolbar? = null
    private var mRecy: RecyclerView? = null
    private var mAdapter: HomeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initView(view)
        //        动态改动 当前Fragment的动画
        //        setFragmentAnimator(fragmentAnimator);
        return view
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_anim -> {
                val popupMenu = PopupMenu(ctx!!, mToolbar!!, GravityCompat.END)
                popupMenu.inflate(R.menu.home_pop)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_anim_veritical -> {
                            (ctx as ISupportActivity).fragmentAnimator = FragmentAnimator.verticalAnimator()
                            Toast.makeText(ctx, R.string.anim_v, Toast.LENGTH_SHORT).show()
                        }
                        R.id.action_anim_horizontal -> {
                            (ctx as ISupportActivity).fragmentAnimator = FragmentAnimator.horizontalAnimator()
                            Toast.makeText(ctx, R.string.anim_h, Toast.LENGTH_SHORT).show()
                        }
                        R.id.action_anim_none -> {
                            (ctx as ISupportActivity).fragmentAnimator = FragmentAnimator.noAnimator()
                            Toast.makeText(ctx, R.string.anim_none, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    popupMenu.dismiss()
                    true
                }
                popupMenu.show()
            }
        }
        return true
    }

    private fun initView(view: View) {
        mToolbar = view.findViewById<View>(R.id.toolbar) as Toolbar
        mRecy = view.findViewById<View>(R.id.recy) as RecyclerView

        mTitles = resources.getStringArray(R.array.array_title)
        mContents = resources.getStringArray(R.array.array_content)

        mToolbar!!.setTitle(R.string.home)
        initToolbarNav(mToolbar, true)
        mToolbar!!.inflateMenu(R.menu.home)
        mToolbar!!.setOnMenuItemClickListener(this)

        mAdapter = HomeAdapter(ctx)
        val manager = LinearLayoutManager(ctx)
        mRecy!!.layoutManager = manager
        mRecy!!.adapter = mAdapter

        mAdapter!!.setOnItemClickListener { position, _ ->
            start(
                DetailFragment.newInstance(
                    mAdapter!!.getItem(position).title
                )
            )
        }

        // Init Datas
        val articleList = ArrayList<Article>()
        for (i in 0..14) {
            val index = (Math.random() * 3).toInt()
            val article = Article(mTitles!![index], mContents!![index])
            articleList.add(article)
        }
        mAdapter!!.setDatas(articleList)
    }

    /**
     * 类似于 Activity的 onNewIntent()
     */
    override fun onNewBundle(args: Bundle?) {
        super.onNewBundle(args)

        toast(args?.getString("from") ?: return)
    }

    companion object {
        private val TAG = "Fragmentation"

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}
