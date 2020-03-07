package com.gp.salik.Model;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gp.salik.activities.AccountSettingsFragment;
import com.gp.salik.activities.TicketsListFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new TicketsListFragment();
            case 1:
                return new AccountSettingsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
