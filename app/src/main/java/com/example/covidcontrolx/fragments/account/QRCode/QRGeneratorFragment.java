package com.example.covidcontrolx.fragments.account.QRCode;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.covidcontrolx.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import androidx.fragment.app.Fragment;

public class QRGeneratorFragment extends Fragment {
    EditText etInput;
    Button btnGenerate;
    ImageView imageViewOutput;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userCollectionRef = db.collection("user");

    String inputS = "";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public QRGeneratorFragment() {
    }

    public static QRGeneratorFragment newInstance(String param1, String param2) {
        QRGeneratorFragment fragment = new QRGeneratorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_q_r_code_generator, container, false);

        etInput = view.findViewById(R.id.et_input);
        btnGenerate = view.findViewById(R.id.btn_generate);
        imageViewOutput = view.findViewById(R.id.output);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot eachDoc : queryDocumentSnapshots) {
                            User user = eachDoc.toObject(User.class);

                            //takes in user from the database and put values to inputS.THis will have values of the last user in list of users
                            inputS = user.getfName() + " " + user.getDob() + " " + user.getNationalId() + "\n";
                        }

                        MultiFormatWriter mfWriter = new MultiFormatWriter();

                        try {
                            BitMatrix bitMatrix = mfWriter.encode(inputS, BarcodeFormat.QR_CODE, 350, 350);

                            BarcodeEncoder encoder = new BarcodeEncoder();

                            Bitmap bitmap = encoder.createBitmap(bitMatrix);

                            imageViewOutput.setImageBitmap(bitmap);

                            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                            manager.hideSoftInputFromWindow(etInput.getApplicationWindowToken(), 0);


                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }

                });


            }
        });

        return view;
    }
}


























