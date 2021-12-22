package com.example.covidcontrolx.fragments.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.covidcontrolx.R;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public HomeFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_home, container, false);
        final View fragmentHome = inflater.inflate(R.layout.fragment_home, container, false);

        // To reset tabs to first one
        fragmentHome.setFocusableInTouchMode(true); // required
        fragmentHome.requestFocus(); // required
        fragmentHome.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) { // equivalent to onBackPressed for Activities
                    if (viewPager.getCurrentItem() == 0) {
                        return false; // don't do anything, use super.onBackPressed(); of HomeActivity
                    } else {
                        viewPager.setCurrentItem(0); // switch to first tab
                        return true;
                    }
                }
                return false;
            }
        });

        tabLayout = fragmentHome.findViewById(R.id.home_tablayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager = fragmentHome.findViewById(R.id.home_viewpager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addTab(tabLayout.newTab().setText("COVID Stats"));
        tabLayout.addTab(tabLayout.newTab().setText("COVID News"));

        DynamicFragmentAdapter dynamicFragmentAdapter = new DynamicFragmentAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(dynamicFragmentAdapter);
        viewPager.setCurrentItem(0);

        return fragmentHome;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            // recreate stats and news fragment
            DynamicFragmentAdapter dynamicFragmentAdapter = new DynamicFragmentAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(dynamicFragmentAdapter);
            viewPager.setCurrentItem(0);
        } catch (Exception e) {
            Log.d(TAG, "HomeFragment.onStart(): " + e.getMessage());
        }
    }
}