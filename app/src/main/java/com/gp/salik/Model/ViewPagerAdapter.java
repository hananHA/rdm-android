package com.gp.salik.Model;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gp.salik.activities.AccountSettingsFragment;
import com.gp.salik.activities.TicketDetailsFragment;
import com.gp.salik.activities.TicketsListFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String tID;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /*public ViewPagerAdapter(FragmentManager fm, String tID) {
        super(fm);
        this.tID = tID;
    }

    public void settID(String tID) {
        this.tID = tID;
    }

    public String gettID() {
        return tID;
    }*/

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new TicketsListFragment();
            case 1:
                return new AccountSettingsFragment();
            case 2:
                return new TicketDetailsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
