package com.example.covidcontrolx.fragments.booking.vaccine;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.covidcontrolx.R;
import com.example.covidcontrolx.fragments.booking.appointment.AppointmentFragment;
import com.example.covidcontrolx.fragments.booking.hospital.HospitalViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class VaccineDetailsFragment extends Fragment {
    private SelectVaccineViewModel selectVaccineViewModel;
    private HospitalViewModel hospitalViewModel;

    TextView txtVaccineType;
    ImageView vaccineImage;
    TextView vaccineInfoTxt, dosageAmount, vaccineEfficacy, vaccineSideEffects;
    Button get_vaccinated_btn;

    String astraZenecaDoseAmount, modernaDoseAmount, pfizerDoseAmount, jjDoseAmount;
    double astraZenecaEfficacy, modernaEfficacy, pfizerEfficacy, jjEfficacy;
    List<String> astraZenecaSideEffects, modernaSideEffects, pfizerSideEffects, jjSideEffects;
    String side_effects = "";

    FirebaseFirestore db;
    CollectionReference collectionReference;

    public VaccineDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vaccine_details, container, false);

        selectVaccineViewModel = new ViewModelProvider(requireActivity()).get(SelectVaccineViewModel.class);
        hospitalViewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);

        txtVaccineType = rootView.findViewById(R.id.vaccine_type_selected);
        vaccineImage = rootView.findViewById(R.id.vaccineInfoImage);
        vaccineInfoTxt = rootView.findViewById(R.id.vaccineInfoTxtVw);
        dosageAmount = rootView.findViewById(R.id.dosageAmountTxtVw);
        vaccineEfficacy = rootView.findViewById(R.id.vaccineEfficacyTxtVw);
        vaccineSideEffects = rootView.findViewById(R.id.sideEffectsTxt);

        get_vaccinated_btn = rootView.findViewById(R.id.get_vaccinated_btn);

        String vaccineType = selectVaccineViewModel.getVaccineTypeSelected();
        txtVaccineType.setText(vaccineType);

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("hospitals");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot hospitalDocument : task.getResult()) {
                        if (hospitalDocument.getString("name") != null)
                            if (hospitalDocument.getString("name").equals(hospitalViewModel.getSelectedHospital().getValue().getName())) {
                            Map<String, Object> servicesMap = (Map<String, Object>) hospitalDocument.getData().get("services");
                            if (servicesMap.containsKey("vaccination")) {
                                ArrayList<HashMap<String, Object>> vaccinationArray = (ArrayList<HashMap<String, Object>>) servicesMap.get("vaccination");
                                for (HashMap<String, Object> vaccineMap : vaccinationArray) {
                                    if (vaccineMap.get("name").toString().equals("Astrazeneca")) {
                                        astraZenecaSideEffects = (List<String>) vaccineMap.get("side_effects");
                                        astraZenecaDoseAmount = (String) vaccineMap.get("dosage_amount");
                                        astraZenecaEfficacy = (double) vaccineMap.get("efficacy") * 100;
                                    }
                                    if (vaccineMap.get("name").toString().equals("Moderna")) {
                                        modernaSideEffects = (List<String>) vaccineMap.get("side_effects");
                                        modernaDoseAmount = (String) vaccineMap.get("dosage_amount");
                                        modernaEfficacy = (double) vaccineMap.get("efficacy") * 100;
                                    }
                                    if (vaccineMap.get("name").toString().equals("Pfizer")) {
                                        pfizerSideEffects = (List<String>) vaccineMap.get("side_effects");
                                        pfizerDoseAmount = (String) vaccineMap.get("dosage_amount");
                                        pfizerEfficacy = (double) vaccineMap.get("efficacy") * 100;
                                    }
                                    if (vaccineMap.get("name").toString().equals("J&J")) {
                                        jjSideEffects = (List<String>) vaccineMap.get("side_effects");
                                        jjDoseAmount = (String) vaccineMap.get("dosage_amount");
                                        jjEfficacy = (double) vaccineMap.get("efficacy") * 100;
                                    }
                                }

                                switch (vaccineType) {
                                    case "Astra Zeneca":
                                        vaccineImage.setImageResource(R.drawable.astra_zeneca);
                                        vaccineInfoTxt.setText(R.string.astraZenecaTxt);
                                        dosageAmount.setText("2*(" + astraZenecaDoseAmount + ")");
                                        vaccineEfficacy.setText(String.valueOf(astraZenecaEfficacy) + "%");
                                        for (int i = 0; i < astraZenecaSideEffects.size(); i++) {
                                            side_effects += (i + 1) + ". " + astraZenecaSideEffects.get(i)
                                                    + "\n";
                                        }
                                        vaccineSideEffects.setText(side_effects);
                                        break;
                                    case "Moderna":
                                        vaccineImage.setImageResource(R.drawable.moderna);
                                        vaccineInfoTxt.setText(R.string.modernaTxt);
                                        dosageAmount.setText("2*(" + modernaDoseAmount + ")");
                                        vaccineEfficacy.setText(String.valueOf(modernaEfficacy) + "%");
                                        for (int i = 0; i < modernaSideEffects.size(); i++) {
                                            side_effects += (i + 1) + ". " + modernaSideEffects.get(i)
                                                    + "\n";
                                        }
                                        vaccineSideEffects.setText(side_effects);
                                        break;
                                    case "Pfizer":
                                        vaccineImage.setImageResource(R.drawable.pfizer);
                                        vaccineInfoTxt.setText(R.string.pfizerTxt);
                                        dosageAmount.setText("2*(" + pfizerDoseAmount + ")");
                                        vaccineEfficacy.setText(String.valueOf(pfizerEfficacy) + "%");
                                        for (int i = 0; i < pfizerSideEffects.size(); i++) {
                                            side_effects += (i + 1) + ". " + pfizerSideEffects.get(i)
                                                    + "\n";
                                        }
                                        vaccineSideEffects.setText(side_effects);
                                        break;
                                    case "J&J":
                                        vaccineImage.setImageResource(R.drawable.jj);
                                        vaccineInfoTxt.setText(R.string.jjTxt);
                                        dosageAmount.setText("1*(" + jjDoseAmount + ")");
                                        vaccineEfficacy.setText(String.valueOf(jjEfficacy) + "%");
                                        for (int i = 0; i < jjSideEffects.size(); i++) {
                                            side_effects += (i + 1) + ". " + jjSideEffects.get(i)
                                                    + "\n";
                                        }
                                        vaccineSideEffects.setText(side_effects);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        });

        get_vaccinated_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_home_frame_layout_fragment, new AppointmentFragment()).addToBackStack(null).commit(); // to be able to come back to VaccineDetailsFragment
            }
        });

        return rootView;
    }
}
