package com.whattabiz.bibliosbookpoint;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {
    private final Context mContext;

    public PageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position);

    }

    @Override
    public int getCount() {
        return NavigationHomeActivity.BookCategories.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // set page title
        return NavigationHomeActivity.BookCategories.get(position);
    }
}
