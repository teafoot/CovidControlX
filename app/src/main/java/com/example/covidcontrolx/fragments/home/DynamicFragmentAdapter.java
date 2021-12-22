package com.example.covidcontrolx.fragments.home;

import com.example.covidcontrolx.fragments.home.news.NewsFragment;
import com.example.covidcontrolx.fragments.home.stats.StatsFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class DynamicFragmentAdapter extends FragmentStatePagerAdapter {
    private int numTabs;
  
    public DynamicFragmentAdapter(FragmentManager fragmentManager, int numTabs) {
        super(fragmentManager);
        this.numTabs = numTabs;
    }
  
    @Override
    public Fragment getItem(int position) {
//        Bundle bundle = new Bundle();
//        bundle.putInt("position", position);
//
//        Fragment fragment = StatsFragment.newInstance();
//        fragment.setArguments(bundle);
//        return fragment;

        Fragment fragment;
        if (position == 0) {
            fragment = StatsFragment.newInstance();
        } else { // position = 1
            fragment = NewsFragment.newInstance();
        }
        return fragment;
    }
  
    @Override
    public int getCount() {
        return numTabs;
    }
}