package me.yokeyword.sample.zhihu.ui.fragment.third.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import java.util.ArrayList
import java.util.Arrays
import androidx.appcompat.widget.Toolbar
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.findChildFragment
import me.yokeyword.fragmentation.loadRootFragment
import me.yokeyword.fragmentation.replaceFragment
import me.yokeyword.sample.R
import me.yokeyword.sample.zhihu.ui.fragment.third.child.child.ContentFragment
import me.yokeyword.sample.zhihu.ui.fragment.third.child.child.MenuListFragment

/**
 * Created by YoKeyword on 16/2/4.
 */
class ShopFragment : SupportFragment() {

    private var mToolbar: Toolbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        mToolbar = view.findViewById<View>(R.id.toolbar) as Toolbar

        mToolbar!!.setTitle(R.string.shop)

        if (findChildFragment(MenuListFragment::class.java) == null) {
            val listMenus = ArrayList(Arrays.asList(*resources.getStringArray(R.array.array_menu)))
            val menuListFragment = MenuListFragment.newInstance(listMenus)
            loadRootFragment(R.id.fl_list_container, menuListFragment)
            // false:  不加入回退栈;  false: 不显示动画
            loadRootFragment(R.id.fl_content_container, ContentFragment.newInstance(listMenus[0]), false, false)
        }
    }

    override fun onBackPressedSupport(): Boolean {
        // ContentFragment是ShopFragment的栈顶子Fragment,会先调用ContentFragment的onBackPressedSupport方法
        Toast.makeText(
            ctx,
            "onBackPressedSupport-->return false, " + getString(R.string.upper_process),
            Toast.LENGTH_SHORT
        ).show()
        return false
    }

    /**
     * 替换加载 内容Fragment
     *
     * @param fragment
     */
    fun switchContentFragment(fragment: ContentFragment) {
        val contentFragment = findChildFragment(ContentFragment::class.java)
        contentFragment?.replaceFragment(fragment, false)
    }

    companion object {
        private val TAG = ShopFragment::class.java.simpleName

        fun newInstance(): ShopFragment {
            val args = Bundle()

            val fragment = ShopFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
