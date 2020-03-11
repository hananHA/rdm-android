package com.gp.salik.Model;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gp.salik.activities.RootFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0) {
            return new RootFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }

}
