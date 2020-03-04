package com.example.rdm.Model;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.rdm.activities.AccountSettings;
import com.example.rdm.activities.TicketsList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new TicketsList();
            case 1:
                return new AccountSettings();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
