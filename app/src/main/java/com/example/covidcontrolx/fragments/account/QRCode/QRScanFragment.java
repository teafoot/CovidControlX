package com.example.covidcontrolx.fragments.account.QRCode;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.example.covidcontrolx.R;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class QRScanFragment extends Fragment {
    private CodeScanner codeScanner;
    private CodeScannerView codeScannerView;
    private TextView textView;
    private Button btnScan;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public QRScanFragment() {
    }

    public static QRScanFragment newInstance(String param1, String param2) {
        QRScanFragment fragment = new QRScanFragment();
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
        View view = inflater.inflate(R.layout.fragment_q_r_scan, container, false);

        codeScannerView = view.findViewById(R.id.scanner_view);
        textView = view.findViewById(R.id.textView);
        btnScan = view.findViewById(R.id.btnScan);

        codeScanner = new CodeScanner(getContext(), codeScannerView);

        codeScanner.setDecodeCallback(result -> getActivity().runOnUiThread(() -> {
            String vaccinationStatus = result.getText();

            textView.setText(vaccinationStatus);
        }));

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("Vaccination Status");
                textView.setTextColor(getResources().getColor(R.color.black));
                checkPermission(Manifest.permission.CAMERA, 112);
                codeScanner.releaseResources();
                codeScanner.startPreview();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkPermission(Manifest.permission.CAMERA, 112);
//        codeScanner.releaseResources();
        codeScanner.startPreview();
    }

    @Override
    public void onStop() {
        super.onStop();
        codeScanner.stopPreview();
        codeScanner.releaseResources();
    }

    public void checkPermission(String permission, int requestCode){
        if(ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        }
        else{
            Toast.makeText(getContext(), "Permission provided", Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if(requestCode == 112){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//
//            }
//        }
//    }
}




























