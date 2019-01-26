package me.yokeyword.sample.flow.ui.fragment.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList
import java.util.Arrays
import androidx.appcompat.widget.Toolbar
import me.yokeyword.fragmentation.findChildFragment
import me.yokeyword.fragmentation.loadRootFragment
import me.yokeyword.fragmentation.replaceFragment
import me.yokeyword.sample.R
import me.yokeyword.sample.base.toast
import me.yokeyword.sample.flow.base.BaseMainFragment

/**
 * Created by YoKeyword on 16/2/4.
 */
class ShopFragment : BaseMainFragment() {

    private var mToolbar: Toolbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)
        initView(view, savedInstanceState)
        return view
    }

    private fun initView(view: View, savedInstanceState: Bundle?) {
        mToolbar = view.findViewById<View>(R.id.toolbar) as Toolbar

        mToolbar!!.setTitle(R.string.shop)
        initToolbarNav(mToolbar)

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
        toast("onBackPressedSupport-->return false, " + getString(R.string.upper_process))
        return false
    }

    /**
     * 替换加载 内容Fragment
     *
     * @param fragment
     */
    fun switchContentFragment(fragment: ContentFragment) {
        val contentFragment = findChildFragment(ContentFragment::class.java)
        if (contentFragment != null) {
            contentFragment.replaceFragment(fragment, false)
        }
    }

    companion object {
        fun newInstance(): ShopFragment {
            val args = Bundle()

            val fragment = ShopFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
