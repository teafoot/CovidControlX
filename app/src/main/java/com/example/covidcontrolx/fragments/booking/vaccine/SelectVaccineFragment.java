package com.example.covidcontrolx.fragments.booking.vaccine;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidcontrolx.R;
import com.example.covidcontrolx.fragments.booking.hospital.HospitalViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class SelectVaccineFragment extends Fragment {
    private static final String TAG = "SelectVaccineFragment";

    private SelectVaccineViewModel selectVaccineViewModel;
    private HospitalViewModel hospitalViewModel;
    FirebaseFirestore db;
    CollectionReference collectionReference;
    long astrazenecaDoses, modernaDoses, pfizerDoses, jjDoses;

    CardView astraZaneca, moderna, pfizer, jj;
    Button continue_btn;
    TextView vaccine_type;
    TextView dosesAstraZeneca, dosesModerna, dosesPfizer, dosesJj;

    public SelectVaccineFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_select_vaccine, container, false);
        View rootView = inflater.inflate(R.layout.fragment_select_vaccine, container, false);

        selectVaccineViewModel = new ViewModelProvider(requireActivity()).get(SelectVaccineViewModel.class);
        hospitalViewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);
        Log.d(TAG, "hospitalViewModel: " + hospitalViewModel.getSelectedHospital().getValue().getName());

        dosesAstraZeneca = rootView.findViewById(R.id.dosesAstraZeneca);
        dosesModerna = rootView.findViewById(R.id.dosesModerna);
        dosesPfizer = rootView.findViewById(R.id.dosesPfizer);
        dosesJj = rootView.findViewById(R.id.dosesJj);

        astraZaneca = rootView.findViewById(R.id.astraZaneca);
        moderna = rootView.findViewById(R.id.moderna);
        pfizer = rootView.findViewById(R.id.pfizer);
        jj = rootView.findViewById(R.id.jj);

        continue_btn = rootView.findViewById(R.id.continue_btn);
        vaccine_type = rootView.findViewById(R.id.vaccine_type);

        vaccine_type.setText(selectVaccineViewModel.getVaccineTypeSelected());

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("hospitals");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot hospitalDocument : task.getResult()) { // hospital
                        if (hospitalDocument.getString("name") != null)
                        if (hospitalDocument.getString("name").equals(hospitalViewModel.getSelectedHospital().getValue().getName())) {
                            Map<String, Object> servicesMap = (Map<String, Object>) hospitalDocument.getData().get("services");
                            if (servicesMap.containsKey("vaccination")) {
                                ArrayList<HashMap<String, Object>> vaccinationArray = (ArrayList<HashMap<String, Object>>) servicesMap.get("vaccination");
                                for (HashMap<String, Object> vaccineMap : vaccinationArray) {
                                    if (vaccineMap.get("name").toString().equals("Astrazeneca")) {
                                        astrazenecaDoses = (long) vaccineMap.get("qty_available");
                                    }
                                    if (vaccineMap.get("name").toString().equals("Moderna")) {
                                        modernaDoses = (long) vaccineMap.get("qty_available");
                                    }
                                    if (vaccineMap.get("name").toString().equals("Pfizer")) {
                                        pfizerDoses = (long) vaccineMap.get("qty_available");
                                    }
                                    if (vaccineMap.get("name").toString().equals("J&J")) {
                                        jjDoses = (long) vaccineMap.get("qty_available");
                                    }
                                }
                            }
                            break;
                        }
                    }

                    dosesAstraZeneca.setText(String.valueOf(astrazenecaDoses));
                    dosesModerna.setText(String.valueOf(modernaDoses));
                    dosesPfizer.setText(String.valueOf(pfizerDoses));
                    dosesJj.setText(String.valueOf(jjDoses));
                }
            }
        });

        astraZaneca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVaccineViewModel.setVaccineTypeSelected(getResources().getString(R.string.astraZeneca));
                selectVaccineViewModel.setVaccineQuantity(astrazenecaDoses);
                vaccine_type.setText(selectVaccineViewModel.getVaccineTypeSelected());
            }
        });
        moderna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVaccineViewModel.setVaccineTypeSelected(getResources().getString(R.string.moderna));
                selectVaccineViewModel.setVaccineQuantity(modernaDoses);
                vaccine_type.setText(selectVaccineViewModel.getVaccineTypeSelected());
            }
        });
        pfizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVaccineViewModel.setVaccineTypeSelected(getResources().getString(R.string.pfizer));
                selectVaccineViewModel.setVaccineQuantity(pfizerDoses);
                vaccine_type.setText(selectVaccineViewModel.getVaccineTypeSelected());
            }
        });
        jj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVaccineViewModel.setVaccineTypeSelected(getResources().getString(R.string.jj));
                selectVaccineViewModel.setVaccineQuantity(jjDoses);
                vaccine_type.setText(selectVaccineViewModel.getVaccineTypeSelected());
            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vaccineType = selectVaccineViewModel.getVaccineTypeSelected();
                long vaccineQty = selectVaccineViewModel.getVaccineQuantity();
                if (vaccineType == null) {
                    Toast.makeText(getActivity(),"Please select a vaccine type first!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (vaccineQty < 1) {
                    Toast.makeText(getActivity(),"Vaccine quantity ran out, please select another one!",Toast.LENGTH_SHORT).show();
                    return;
                }

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_home_frame_layout_fragment, new VaccineDetailsFragment()).addToBackStack(null).commit(); // to be able to come back to SelectVaccineFragment
//                getActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.activity_home_frame_layout_fragment, new VaccineDetailsFragment()).commit();
            }
        });

        return rootView;
    }
}