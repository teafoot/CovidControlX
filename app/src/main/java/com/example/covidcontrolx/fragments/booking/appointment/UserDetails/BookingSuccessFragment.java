package com.example.covidcontrolx.fragments.booking.appointment.UserDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidcontrolx.R;
import com.example.covidcontrolx.fragments.booking.appointment.AppointmentViewModel;
import com.example.covidcontrolx.fragments.booking.hospital.HospitalViewModel;
import com.example.covidcontrolx.fragments.booking.models.Hospital;
import com.example.covidcontrolx.fragments.booking.vaccine.SelectVaccineViewModel;
import com.example.covidcontrolx.fragments.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class BookingSuccessFragment extends Fragment {
    private TextView fullNameEditTxt, idNumEditTxt, dateOfBirthEditTxt, phoneNumEditTxt, emailTxt,
            vaccineTypeEditTxt, vaccineDateEditTxt, vaccineTimeEditText, vaccineLocationEditTxt, doseNumberEditText;
    private Button doneBtn;

    UserInfoViewModel userInfoViewModel;
    SelectVaccineViewModel selectVaccineViewModel;
    HospitalViewModel hospitalViewModel;
    AppointmentViewModel appointmentViewModel;

    public BookingSuccessFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_booking_success, container, false);

        userInfoViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        selectVaccineViewModel = new ViewModelProvider(requireActivity()).get(SelectVaccineViewModel.class);
        hospitalViewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);
        appointmentViewModel = new ViewModelProvider(requireActivity()).get(AppointmentViewModel.class);

        fullNameEditTxt = rootView.findViewById(R.id.fullNameEditText);
        idNumEditTxt = rootView.findViewById(R.id.idNumEditText);
        dateOfBirthEditTxt = rootView.findViewById(R.id.dateOfBirthEditText);
        phoneNumEditTxt = rootView.findViewById(R.id.phoneNumEditText);
        emailTxt = rootView.findViewById(R.id.emailEditText);
        doseNumberEditText = rootView.findViewById(R.id.doseNumberEditText);
        vaccineTypeEditTxt = rootView.findViewById(R.id.vaccineTypeEditText);
        vaccineDateEditTxt = rootView.findViewById(R.id.vaccineDateEditText);
        vaccineTimeEditText = rootView.findViewById(R.id.vaccineTimeEditText);
        vaccineLocationEditTxt = rootView.findViewById(R.id.vaccineLocationEditText);


        fullNameEditTxt.setText(userInfoViewModel.getFullName());
        idNumEditTxt.setText(userInfoViewModel.getIdNum());
        dateOfBirthEditTxt.setText(userInfoViewModel.getDateOfBirth());
        phoneNumEditTxt.setText(userInfoViewModel.getPhoneNum());
        emailTxt.setText(userInfoViewModel.getEmail());
        doseNumberEditText.setText(userInfoViewModel.getDoseNum());
        vaccineTypeEditTxt.setText(selectVaccineViewModel.getVaccineTypeSelected());
        idNumEditTxt.setText(userInfoViewModel.getIdNum());
        Hospital hospital = hospitalViewModel.getSelectedHospital().getValue();
        vaccineLocationEditTxt.setText(hospital.getName() + "\n" + hospital.getLocation().toString());
        vaccineDateEditTxt.setText(appointmentViewModel.getDate());
        vaccineTimeEditText.setText(appointmentViewModel.getStartTime());

        doneBtn = rootView.findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment homeFragment = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame_layout_fragment, homeFragment).commit();
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.activity_home_bottom_nav);
                bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home);
            }
        });

        return rootView;
    }
}