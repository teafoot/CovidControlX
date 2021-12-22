package com.example.covidcontrolx.fragments.booking.appointment.UserDetails;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import android.widget.EditText;
import android.widget.Toast;

import com.example.covidcontrolx.R;
import com.example.covidcontrolx.fragments.booking.appointment.AppointmentViewModel;
import com.example.covidcontrolx.fragments.booking.hospital.HospitalViewModel;
import com.example.covidcontrolx.fragments.booking.vaccine.SelectVaccineViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class UserPersonalInfoFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "UserPersonalInfoFragment";
    private EditText fullNameEditTxt, idNumEditTxt, dateOfBirthEditTxt, phoneNumEditTxt, doseNumEditTxt;
    private DatePickerDialog dpd;
    private CheckBox confirmChkBox;
    private Button continueButton;
    private boolean confirmCheckBx;

    UserInfoViewModel userInfoViewModel;
    private HospitalViewModel hospitalViewModel;
    private AppointmentViewModel appointmentViewModel;
    SelectVaccineViewModel selectVaccineViewModel;

    FirebaseFirestore db;
    CollectionReference collectionReference;
    //
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    public UserPersonalInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_personal_info, container, false);

        userInfoViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        hospitalViewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);
        appointmentViewModel = new ViewModelProvider(requireActivity()).get(AppointmentViewModel.class);
        selectVaccineViewModel = new ViewModelProvider(requireActivity()).get(SelectVaccineViewModel.class);

        fullNameEditTxt = rootView.findViewById(R.id.fullNameEditTxt);
        idNumEditTxt = rootView.findViewById(R.id.idNumEditTxt);
        dateOfBirthEditTxt = rootView.findViewById(R.id.dateOfBirthEditTxt);
        phoneNumEditTxt = rootView.findViewById(R.id.phoneNumEditTxt);
        doseNumEditTxt = rootView.findViewById(R.id.doseNumEditTxt);
        confirmChkBox = rootView.findViewById(R.id.confirmChkBox);
        continueButton = rootView.findViewById(R.id.continueBtn);

        fullNameEditTxt.setText(userInfoViewModel.getFullName());
        idNumEditTxt.setText(userInfoViewModel.getIdNum());
        dateOfBirthEditTxt.setText(userInfoViewModel.getDateOfBirth());
        phoneNumEditTxt.setText(userInfoViewModel.getPhoneNum());
        doseNumEditTxt.setText(userInfoViewModel.getDoseNum());
        confirmChkBox.setSelected(userInfoViewModel.isConfirmCheckBox());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        dateOfBirthEditTxt.requestFocus();
        dateOfBirthEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar now = Calendar.getInstance();
                if (dpd == null) {
                    dpd = DatePickerDialog.newInstance(
                        UserPersonalInfoFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                    );
                } else {
                    dpd.initialize(
                        UserPersonalInfoFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                    );
                }
                dpd.showYearPickerFirst(true);
                dpd.setOnCancelListener(dialog -> {
                    Log.d("DatePickerDialog", "Dialog was cancelled");
                    dpd = null;
                });
                dpd.show(requireFragmentManager(), "Datepickerdialog");
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfoViewModel.setConfirmCheckBox(confirmChkBox.isChecked());
                confirmCheckBx = userInfoViewModel.isConfirmCheckBox();

                if (fullNameEditTxt.getText().length() == 0 || idNumEditTxt.getText().length() == 0
                        || dateOfBirthEditTxt.getText().length() == 0 || phoneNumEditTxt.getText().length() == 0
                        || doseNumEditTxt.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Please fill all the fields", Toast.LENGTH_LONG).show();
                } else if (!fullNameEditTxt.getText().toString().contains(" ")) {
                    Toast.makeText(getActivity(), "Please enter your first and last name (separated by spaces)", Toast.LENGTH_LONG).show();
                }else if (phoneNumEditTxt.getText().length() < 10 ||
                        phoneNumEditTxt.getText().length() > 10) {
                    Toast.makeText(getActivity(), "Please enter valid phone number (10 digits)",
                            Toast.LENGTH_LONG).show();
                } else {
                    if (confirmCheckBx) {
                        userInfoViewModel.setFullName(fullNameEditTxt.getText().toString());
                        userInfoViewModel.setIdNum(idNumEditTxt.getText().toString());
                        userInfoViewModel.setDateOfBirth(dateOfBirthEditTxt.getText().toString());
                        userInfoViewModel.setPhoneNum(phoneNumEditTxt.getText().toString());
                        userInfoViewModel.setEmail(firebaseUser.getEmail());
                        userInfoViewModel.setDoseNum(doseNumEditTxt.getText().toString());
                        userInfoViewModel.setConfirmCheckBox(confirmChkBox.isChecked());

                        updatePatientCertificate();// update in firestore

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.activity_home_frame_layout_fragment, new BookingSuccessFragment())
                                .addToBackStack(null).commit();
                    } else {
                        Toast.makeText(getActivity(), "Please confirm that the provided information is accurate.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        dateOfBirthEditTxt.setText(date);
    }

    public void updatePatientCertificate() {
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("hospitals");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot hospitalDocument : task.getResult()) { // hospital
                        if (hospitalDocument.getString("name") != null) {
                            if (hospitalDocument.getString("name").equals(hospitalViewModel.getSelectedHospital().getValue().getName())) {
                                try {
                                    String hospitalId = hospitalDocument.getId();
                                    boolean foundTimeslot = false;

                                    List<HashMap<String, Object>> bookingsMap = (ArrayList<HashMap<String, Object>>) hospitalDocument.get("bookings");
                                    for (HashMap<String, Object> bookingMap : bookingsMap) {
                                        if (bookingMap.get("date").equals(appointmentViewModel.getDate())) {
//                                            Log.d(TAG, "mapDate: " + bookingMap.get("date"));
                                            for (HashMap<String, Object> timeslotMap : (ArrayList<HashMap<String, Object>>) bookingMap.get("timeslots")) {
                                                if (timeslotMap.get("timestart").equals(appointmentViewModel.getStartTime())) {
//                                                    Log.d(TAG, "mapTime: " + timeslotMap.get("timestart"));
                                                    HashMap<String, Object> patientCertMap = new HashMap<>();
                                                    String name = userInfoViewModel.getFullName();
                                                    int space = name.indexOf(" ");
                                                    String first_name = name.substring(0, space);
                                                    String last_name = name.substring(space + 1, name.length());
                                                    patientCertMap.put("first_name", first_name);
                                                    patientCertMap.put("last_name", last_name);
                                                    patientCertMap.put("national_id", userInfoViewModel.getIdNum());
                                                    patientCertMap.put("date_of_birth", userInfoViewModel.getDateOfBirth());
                                                    patientCertMap.put("phone_number", userInfoViewModel.getPhoneNum());
                                                    patientCertMap.put("email", firebaseUser.getEmail());
                                                    patientCertMap.put("vaccine_type", selectVaccineViewModel.getVaccineTypeSelected());
                                                    patientCertMap.put("vaccination_date", appointmentViewModel.getDate());
                                                    patientCertMap.put("vaccine_shot", userInfoViewModel.getDoseNum());
                                                    patientCertMap.put("vaccination_location", hospitalViewModel.getSelectedHospital().getValue().getLocation().toString());
                                                    timeslotMap.put("patient_certificate", patientCertMap);

                                                    timeslotMap.put("status", "occupied");
                                                    timeslotMap.put("service_type", "vaccination");
                                                    foundTimeslot = true;
                                                    break;
                                                }
                                            }
                                            if (foundTimeslot)
                                                break;
                                        }
                                    }

                                    db.collection("hospitals").document(hospitalId).update("bookings", bookingsMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(), "Success on booking an appointment.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Failed to book an appointment.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }
                }
            }
        });
    }
}

//                                JSONArray bookingsArray = new JSONArray(bookingList);
//                                for (int i = 0; i < bookingsArray.length(); i++) {
////                                    Log.d(TAG, "bookingObj: " + bookingsArray.get(i));
//                                    JSONObject bookingObj = (JSONObject) bookingsArray.get(i);
//                                    if (bookingObj.getString("date").equals(appointmentViewModel.getDate())) {
//                                        JSONArray timeslots = bookingObj.getJSONArray("timeslots");
//                                        for (int j = 0; j < timeslots.length(); j++) {
//                                            JSONObject timeslotObj = (JSONObject) timeslots.get(j);
//                                            if (timeslotObj.getString("timestart").equals(appointmentViewModel.getStartTime())) {
////                                                Log.d(TAG, "timeslotObj: " + timeslotObj);
//
//                                                HashMap<String, String> hashMap = new HashMap<>();
//                                                String name = userInfoViewModel.getFullName();
////                                                int space = name.indexOf(" ");
////                                                String first_name = name.substring(0, space);
////                                                String last_name = name.substring(space + 1, name.length());
//                                                hashMap.put("first_name", name);
//                                                hashMap.put("last_name", name);
//                                                //
//                                                hashMap.put("national_id", userInfoViewModel.getIdNum());
//                                                hashMap.put("date_of_birth", userInfoViewModel.getDateOfBirth());
//                                                hashMap.put("phone_num", userInfoViewModel.getPhoneNum());
//                                                hashMap.put("vaccine_type", selectVaccineViewModel.getVaccineTypeSelected());
//                                                hashMap.put("vaccination_date", appointmentViewModel.getDate());
//                                                hashMap.put("vaccine_shot", userInfoViewModel.getDoseNum());
//                                                hashMap.put("vaccination_location", hospitalViewModel.getSelectedHospital().getValue().getLocation().toString());
//
//                                                timeslotObj.put("patient_cert", hashMap);
//                                            }
//                                        }
//                                    }
//                                }