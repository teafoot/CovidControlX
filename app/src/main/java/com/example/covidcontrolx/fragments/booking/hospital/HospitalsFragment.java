package com.example.covidcontrolx.fragments.booking.hospital;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.covidcontrolx.R;
import com.example.covidcontrolx.fragments.booking.BookingFragment;
import com.example.covidcontrolx.fragments.booking.models.Hospital;
import com.example.covidcontrolx.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HospitalsFragment extends Fragment implements HospitalRecyclerViewAdapter.OnHospitalClickListener {
    private HospitalViewModel hospitalViewModel;
    private RecyclerView hospitalsRecyclerView;
    private HospitalRecyclerViewAdapter hospitalRecyclerViewAdapter;
    private List<Hospital> hospitalList;
    private ImageView btnBackArrow;

    public HospitalsFragment(){
    }

    public static HospitalsFragment newInstance() {
        return new HospitalsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_hospitals, container, false);

        View hospitalsFragmentView = inflater.inflate(R.layout.fragment_hospitals, container, false);

        hospitalViewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);
        hospitalList = new ArrayList<>();

        btnBackArrow = hospitalsFragmentView.findViewById(R.id.back_arrow2);
        btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame_layout_fragment, new BookingFragment()).commit();
                getActivity().onBackPressed(); // HomeActivity (to pop the fragment stack)
            }
        });

        if (!hospitalViewModel.getFilterActive().getValue()) { // filter is not active
            if (hospitalViewModel.getHospitals().getValue() != null) {
                hospitalsRecyclerView = hospitalsFragmentView.findViewById(R.id.hospitals_recyclerview);
                hospitalsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                hospitalList = hospitalViewModel.getHospitals().getValue();
                hospitalRecyclerViewAdapter = new HospitalRecyclerViewAdapter(getActivity().getApplicationContext(), hospitalList, this);
                hospitalsRecyclerView.setAdapter(hospitalRecyclerViewAdapter);
                if (hospitalList.size() == 0) {
                    hospitalsFragmentView.findViewById(R.id.txt_no_hospitals).setVisibility(View.VISIBLE);
                } else {
                    hospitalsFragmentView.findViewById(R.id.txt_no_hospitals).setVisibility(View.INVISIBLE);
                }
            }
        } else { // filter is active
            if (hospitalViewModel.getFilterHospitals().getValue() != null) {
                hospitalsRecyclerView = hospitalsFragmentView.findViewById(R.id.hospitals_recyclerview);
                hospitalsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                hospitalList = hospitalViewModel.getFilterHospitals().getValue();
                hospitalRecyclerViewAdapter = new HospitalRecyclerViewAdapter(getActivity().getApplicationContext(), hospitalList, this);
                hospitalsRecyclerView.setAdapter(hospitalRecyclerViewAdapter);
                if (hospitalList.size() == 0) {
                    hospitalsFragmentView.findViewById(R.id.txt_no_hospitals).setVisibility(View.VISIBLE);
                } else {
                    hospitalsFragmentView.findViewById(R.id.txt_no_hospitals).setVisibility(View.INVISIBLE);
                }
            }
        }

        return hospitalsFragmentView;
    }

    @Override
    public void onHospitalClicked(Hospital hospital) { // callback HospitalRecyclerViewAdapter
        hospitalViewModel.setSelectedHospital(hospital);
//        hospitalsRecyclerView.setVisibility(View.INVISIBLE); // fixes bug when recyclerview of hospitals is still accessible in the hospital details page; don't use this line, hard to turn back to visible when going back (backstack problem)
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_home_frame_layout_fragment, new HospitalDetailsFragment()).addToBackStack(null).commit(); // to be able to come back to HospitalsFragment
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.hospitals_fragment, new HospitalDetailsFragment()).commit();
    }
}

//    public void setHospitalRecyclerViewVisibility(boolean visible) {
//        if (hospitalsRecyclerView == null) {
//            return;
//        }
//
//        if (visible) {
//            hospitalsRecyclerView.setVisibility(View.VISIBLE);
//        }
//        else {
//            hospitalsRecyclerView.setVisibility(View.INVISIBLE);
//        }
//    }
