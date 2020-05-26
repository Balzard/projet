package com.example.see_u_festival.Utils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    /**@param position the index of the fragment in fragmentList
     * @return the fragment at the index position
     **/
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    /**@return number of fragments in the list a.k.a number of tabs in home fragment**/
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /**@param  position is the index of the fragment in fragmentTitleList
     * @return the title of the fragment <=> the title displayed for the tab
     **/
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    /** to add fragment to the fragmentList
     * Used in the home fragment to add to the viewpager
     * **/
    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
}