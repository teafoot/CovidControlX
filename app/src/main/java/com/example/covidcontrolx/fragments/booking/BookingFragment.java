package com.example.covidcontrolx.fragments.booking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.covidcontrolx.R;
import com.example.covidcontrolx.fragments.booking.hospital.MapsFragment;

public class BookingFragment extends Fragment {
    public BookingFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_search, container, false);
        final View fragmentSearchView = inflater.inflate(R.layout.fragment_booking, container, false);
        MapsFragment mapsFragment = new MapsFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame_layout_fragment, mapsFragment).commit();
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragmentSearch, mapsFragment).commit(); // this frame layout is not necessary, just use HomeActivity's frame layout.
        return fragmentSearchView;
    }
}