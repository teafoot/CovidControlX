package com.example.covidcontrolx.fragments.account;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidcontrolx.R;
import com.example.covidcontrolx.fragments.booking.models.Patient_certificate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownloadCertificateFragment extends Fragment {
    private static final String TAG = "DownloadCertificateFragment";
    ImageView imageViewOutput;
    TextView nameEditTxt, idEditTxt, birthDateEditTxt, vaccineDateEditTxt, vaccineLocationEditTxt, vaccineTypeEditTxt, dosesEditTxt;
    Button downloadBtn;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    CollectionReference collectionReference;

    String email;
    Patient_certificate patientCertificate;
    Bitmap bitmap;

    private DownloadCertificateViewModel downloadCertificateViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_download_certificate, container, false);
        rootView.setVisibility(View.INVISIBLE);

        downloadCertificateViewModel = new ViewModelProvider(requireActivity()).get(DownloadCertificateViewModel.class);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        imageViewOutput = rootView.findViewById(R.id.output);
        nameEditTxt = rootView.findViewById(R.id.nameEditTxt);
        idEditTxt = rootView.findViewById(R.id.idEditTxt);
        birthDateEditTxt = rootView.findViewById(R.id.birthDateEditTxt);
        vaccineDateEditTxt = rootView.findViewById(R.id.vaccineDateEditTxt);
        vaccineLocationEditTxt = rootView.findViewById(R.id.vaccineLocationEditTxt);
        vaccineTypeEditTxt = rootView.findViewById(R.id.vaccineType);
        dosesEditTxt = rootView.findViewById(R.id.numDosesTxt);

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("hospitals");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            boolean found = false;

            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseUser = firebaseAuth.getCurrentUser();
                    email = firebaseUser.getEmail();

                    for (QueryDocumentSnapshot hospitalDocument : task.getResult()) { // hospital
                        if (hospitalDocument.getString("name") != null) {
                            try {
                                List<HashMap<String, Object>> bookingsMap = (ArrayList<HashMap<String, Object>>) hospitalDocument.get("bookings");
                                for (HashMap<String, Object> bookingMap : bookingsMap) {
                                    for (HashMap<String, Object> timeslotMap : (ArrayList<HashMap<String, Object>>) bookingMap.get("timeslots")) {
                                        HashMap<String, Object> patientCertificateMap = (HashMap<String, Object>) timeslotMap.get("patient_certificate");
                                        if (patientCertificateMap!=null && patientCertificateMap.get("email")!=null) {
                                            if (patientCertificateMap.get("email").equals(email)) {
                                                patientCertificate = new Patient_certificate();
                                                patientCertificate.setNational_id((String) patientCertificateMap.get("national_id"));
                                                patientCertificate.setFirst_name((String) patientCertificateMap.get("first_name"));
                                                patientCertificate.setLast_name((String) patientCertificateMap.get("last_name"));
                                                patientCertificate.setDate_of_birth((String) patientCertificateMap.get("date_of_birth"));
                                                patientCertificate.setPhone_number((String) patientCertificateMap.get("phone_number"));
                                                patientCertificate.setEmail((String) patientCertificateMap.get("email"));
                                                patientCertificate.setVaccine_type((String) patientCertificateMap.get("vaccine_type"));
                                                patientCertificate.setVaccine_shot((String) patientCertificateMap.get("vaccine_shot"));
                                                patientCertificate.setVaccination_location((String) patientCertificateMap.get("vaccination_location"));
                                                patientCertificate.setVaccination_date((String) patientCertificateMap.get("vaccination_date"));
                                                downloadCertificateViewModel.setPatientCertificate(patientCertificate);
                                                found = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (found)
                                        break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (found)
                                break;
                        }
                    }

                    if (!found) { // user email hasn't created an appointment yet, return
                        AccountSettingsFragment accountSettingsFragment = new AccountSettingsFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame_layout_fragment, accountSettingsFragment).commit();
                        Toast.makeText(getActivity(), "Create an appointment first!", Toast.LENGTH_SHORT).show();
                    } else {
                        rootView.setVisibility(View.VISIBLE);

                        //the qr code will be generated (based on the vaccine shot count that is updated through the admin app)
                        String encodedInput = patientCertificate.getNational_id() + ", " + patientCertificate.getFirst_name() + ", " + patientCertificate.getLast_name() + ", " + patientCertificate.getDate_of_birth() + ", " + patientCertificate.getVaccine_shot();
                        MultiFormatWriter mfWriter = new MultiFormatWriter();
                        try {
                            BitMatrix bitMatrix = mfWriter.encode(encodedInput, BarcodeFormat.QR_CODE, 350, 350);
                            BarcodeEncoder encoder = new BarcodeEncoder();
                            bitmap = encoder.createBitmap(bitMatrix);
                            imageViewOutput.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                        nameEditTxt.setText(patientCertificate.getFirst_name() + " " + patientCertificate.getLast_name());
                        idEditTxt.setText(patientCertificate.getNational_id());
                        birthDateEditTxt.setText(patientCertificate.getDate_of_birth());
                        vaccineDateEditTxt.setText(patientCertificate.getVaccination_date());
                        vaccineLocationEditTxt.setText(patientCertificate.getVaccination_location());
                        vaccineTypeEditTxt.setText(patientCertificate.getVaccine_type());
                        dosesEditTxt.setText(patientCertificate.getVaccine_shot());

                        downloadBtn = rootView.findViewById(R.id.downloadBtn);
                        downloadBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                createCertificate();
                            }
                        });
                    }
                }
            }
        });

        return rootView;
    }

    public void createCertificate() {
        String doseStatus = "";
        if (dosesEditTxt.getText().toString().equals("1")) {
            doseStatus += "partially";
        } else if (dosesEditTxt.getText().toString().equals("2")) {
            doseStatus += "fully";
        }

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200, 1200, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint title = new Paint();
        title.setTextAlign(Paint.Align.CENTER);
        title.setTextSize(80f);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setColor(Color.BLACK);
        canvas.drawText("Vaccination Certificate", 1200 / 2, 300, title);
        canvas.drawBitmap(bitmap, 450, 750, title);

        TextPaint textPaint = new TextPaint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(50f);
        textPaint.setColor(Color.BLACK);

        String text = "We certify that " + nameEditTxt.getText().toString() +
                ", whose date of birth is " + birthDateEditTxt.getText().toString() +
                " and ID number is " + idEditTxt.getText().toString().trim() +
                ", is " + doseStatus + " vaccinated on " +
                vaccineDateEditTxt.getText().toString()
                + " from " + vaccineLocationEditTxt.getText().toString().trim() + " with " +
                vaccineTypeEditTxt.getText().toString() + " vaccine.";
        DynamicLayout dynamicLayout = DynamicLayout.Builder.obtain(text, textPaint, 1000).setAlignment(Layout.Alignment.ALIGN_NORMAL).build();
        canvas.translate(1200 / 2, 400);
        dynamicLayout.draw(canvas);
        pdfDocument.finishPage(page);

        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, "CovidVaccineCertificate.pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(getActivity(), "Certificate is saved to your device", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pdfDocument.close();
        }
    }
}

//Doesn't work:
//                                List<HashMap<String, Object>> bookingsList = (ArrayList<HashMap<String, Object>>) hospitalDocument.get("bookings");
//                                JSONArray bookingsArray = new JSONArray(bookingsList);
//                                for (int i = 0; i < bookingsArray.length(); i++) {
//                                    JSONObject bookingObj = (JSONObject) bookingsArray.get(i);
//                                    JSONArray timeslots = bookingObj.getJSONArray("timeslots");
//                                    for (int j = 0; j < timeslots.length(); j++) {
//                                        JSONObject timeslotObj = (JSONObject) timeslots.get(j);
//                                        JSONObject patientCertificateObj = (JSONObject) timeslotObj.get("patient_certificate");
//                                        if (patientCertificateObj.has("email")) {
//                                            if (patientCertificateObj.getString("email").equals(email)) {
////                                            if (timeslotObj.getString("patient_certificate.email").equals(email)) {
//                                                patientCertificate = new Patient_certificate();
//                                                patientCertificate.setNational_id(timeslotObj.getString("national_id"));
//                                                patientCertificate.setFirst_name(timeslotObj.getString("first_name"));
//                                                patientCertificate.setLast_name(timeslotObj.getString("last_name"));
//                                                patientCertificate.setDate_of_birth(timeslotObj.getString("date_of_birth"));
//                                                patientCertificate.setPhone_number(timeslotObj.getString("phone_number"));
//                                                patientCertificate.setEmail(timeslotObj.getString("email"));
//                                                patientCertificate.setVaccine_type(timeslotObj.getString("vaccine_type"));
//                                                patientCertificate.setVaccine_shot(timeslotObj.getInt("vaccine_shot"));
//                                                patientCertificate.setVaccination_location(timeslotObj.getString("vaccinationLocation"));
//                                                patientCertificate.setVaccination_date(timeslotObj.getString("vaccinationDate"));
//                                                downloadCertificateViewModel.setPatientCertificate(patientCertificate);
//                                                found = true;
//                                                break;
//                                            }
//                                        }
//                                    }
//                                    if (found)
//                                        break;
//                                }

////        File source = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), "CovidVaccineCertificate.pdf");
//        File source = new File(getActivity().getExternalFilesDir(null).getAbsolutePath(), "CovidVaccineCertificate.pdf");
////        File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/CovidVaccineCertificate.pdf");
////        File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/CovidVaccineCertificate.pdf");
////        File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/CovidControlX/CovidVaccineCertificate.pdf");
//        try {
//            pdfDocument.writeTo(new FileOutputStream(source)); // /storage/emulated/0/Android/data/com.example.covidcontrolx/files/CovidVaccineCertificate.pdf
////            FileUtils.copyFile(source, destination); // no permissions to save under '/storage/emulated/0/'
//            Toast.makeText(getActivity(), "Certificate is saved to your device", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            pdfDocument.close();
//        }