package me.yokeyword.sample.flow.adapter;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import me.yokeyword.sample.flow.ui.fragment.discover.PagerChildFragment;

/**
 * Created by YoKeyword on 16/2/5.
 */
public class DiscoverFragmentAdapter extends FragmentPagerAdapter {
    String[] mTitles;

    public DiscoverFragmentAdapter(FragmentManager fm, String... titles) {
        super(fm);
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return PagerChildFragment.Companion.newInstance(0);
        } else if (position == 1) {
            return PagerChildFragment.Companion.newInstance(1);
        } else {
            return PagerChildFragment.Companion.newInstance(2);
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
