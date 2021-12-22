package com.example.covidcontrolx.fragments.booking.hospital;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.covidcontrolx.R;
import com.example.covidcontrolx.fragments.booking.BookingFragment;
import com.example.covidcontrolx.fragments.booking.models.Hospital;
import com.example.covidcontrolx.fragments.booking.models.Pcr_test;
import com.example.covidcontrolx.fragments.booking.models.Rapid_test;
import com.example.covidcontrolx.fragments.booking.models.Vaccination;
import com.example.covidcontrolx.fragments.booking.vaccine.SelectVaccineFragment;
import com.example.covidcontrolx.utils.FragmentUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class HospitalDetailsFragment extends Fragment {
    private static final String TAG = "HospitalDetailsFragment";
    HospitalViewModel hospitalViewModel;
    private ImageView btnBackArrow;
    private Button btnVaccination;
    private Button btnPCRTest;
    private Button btnRapidTest;
    private TextView txtVaccinationQty, txtPcrTestQty, txtRapidTestQty;

    public HospitalDetailsFragment(){
    }

    public static HospitalDetailsFragment newInstance() {
        return new HospitalDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_hospital_details, container, false);
        final View fragmentHospitalDetailsView = inflater.inflate(R.layout.fragment_hospital_details, container, false);

        hospitalViewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);

        btnBackArrow = fragmentHospitalDetailsView.findViewById(R.id.back_arrow2);
        btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame_layout_fragment, new BookingFragment()).commit();
                getActivity().onBackPressed(); // HomeActivity (to pop the fragment stack)
            }
        });

        return fragmentHospitalDetailsView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Hospital hospital = hospitalViewModel.getSelectedHospital().getValue();

        if (hospital != null) {
            ImageView imgHospital = view.findViewById(R.id.img_hospital_details);
            Glide.with(this).load(hospital.getImage()).override(1400, 280).into(imgHospital);
            TextView txtHospitalName = view.findViewById(R.id.txt_hospital_details_name);
            txtHospitalName.setText(hospital.getName());
            TextView txtHospitalAddress = view.findViewById(R.id.txt_hospital_details_address);
            txtHospitalAddress.setText(hospital.getLocation().toString());
        }

        if (hospital.getServices().getVaccination() != null) {
            int totalQty = 0;
            for (Vaccination vac: hospital.getServices().getVaccination()) {
                totalQty += vac.getQty_available();
            }
            txtVaccinationQty = view.findViewById(R.id.service_vaccination_qty);
            txtVaccinationQty.setText(totalQty + " doses left.");

            btnVaccination = view.findViewById(R.id.btn_vaccination);
            btnVaccination.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hospitalViewModel.setSelectedService("vaccination"); // optional

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_home_frame_layout_fragment, new SelectVaccineFragment()).addToBackStack(null).commit(); // to be able to come back to HospitalDetailsFragment
//                    getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.activity_home_frame_layout_fragment, new SelectVaccineFragment()).commit();
                }
            });
        } else {
            view.findViewById(R.id.service_vaccination).setVisibility(View.GONE);
        }

        if (hospital.getServices().getPcr_test() != null) {
            Pcr_test pcr_test = hospital.getServices().getPcr_test();

            txtPcrTestQty = view.findViewById(R.id.service_pcr_test_qty);
            txtPcrTestQty.setText(pcr_test.getSwab_qty() + " swabs left.");

            btnPCRTest = view.findViewById(R.id.btn_pcr_test);
            btnPCRTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Service to be implemented...", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            view.findViewById(R.id.service_pcr_test).setVisibility(View.GONE);
        }

        if (hospital.getServices().getRapid_test() != null) {
            Rapid_test rapid_test = hospital.getServices().getRapid_test();

            txtRapidTestQty = view.findViewById(R.id.service_rapid_test_qty);
            txtRapidTestQty.setText(rapid_test.getSwab_qty() + " swabs left.");

            btnRapidTest = view.findViewById(R.id.btn_rapid_test);
            btnRapidTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Service to be implemented...", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            view.findViewById(R.id.service_rapid_test).setVisibility(View.GONE);
        }
    }
}
