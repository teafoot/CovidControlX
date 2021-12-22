package com.example.covidcontrolx.fragments.account;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidcontrolx.LoginActivity;
import com.example.covidcontrolx.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountSettingsFragment extends Fragment {
    Button btnUpdateEmail, btnUpdatePassword, btnSendVerificationEmail, btnVaccinationCertificate, btnLogout;
    TextView txtEmail, txtID, txtVerifiedAccount;

    String userEmail, userID;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    public AccountSettingsFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_account, container, false);
        final View fragmentAccountView = inflater.inflate(R.layout.fragment_account, container, false);

        txtID = fragmentAccountView.findViewById(R.id.txtUserID);
        txtEmail = fragmentAccountView.findViewById(R.id.txtUserEmail);
        txtVerifiedAccount = fragmentAccountView.findViewById(R.id.txtAccountVerification);

        btnSendVerificationEmail = fragmentAccountView.findViewById(R.id.btnSendVerificationEmail);
        btnSendVerificationEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseUser.isEmailVerified()) {
                    Toast.makeText(getActivity(), "Email was already verified", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(getActivity(), "Verification email sent. Please re-login after verifying.", Toast.LENGTH_SHORT).show();
                }
                verifyEmail();
            }
        });
        btnVaccinationCertificate = fragmentAccountView.findViewById(R.id.btnCertificate);
        btnVaccinationCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadCertificateFragment downloadCertificateFragment = new DownloadCertificateFragment();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_home_frame_layout_fragment, downloadCertificateFragment).addToBackStack(null).commit(); // to be able to come back to AccountSettingsFragment
            }
        });
        btnUpdateEmail = fragmentAccountView.findViewById(R.id.btnUpdateEmail);
        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailUpdateFragment emailUpdateFragment = new EmailUpdateFragment();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_home_frame_layout_fragment, emailUpdateFragment).addToBackStack(null).commit(); // to be able to come back to AccountSettingsFragment
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame_layout_fragment, emailUpdateFragment).commit();
            }
        });
        btnUpdatePassword = fragmentAccountView.findViewById(R.id.btnUpdatePassword);
        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordUpdateFragment passwordUpdateFragment = new PasswordUpdateFragment();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_home_frame_layout_fragment, passwordUpdateFragment).addToBackStack(null).commit(); // to be able to come back to AccountSettingsFragment
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame_layout_fragment, passwordUpdateFragment).commit();
            }
        });
        btnLogout = fragmentAccountView.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Log out?");
                builder.setMessage("Are you sure you want to log out?");
                final AlertDialog dialog = builder.create();
                dialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseAuth.signOut();
                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            userEmail = firebaseUser.getEmail();
            txtEmail.setText(userEmail);
            userID = firebaseUser.getUid();
            txtID.setText(userID);
            verifyEmail();
//            Log.d("test", firebaseUser.getUid());
        }

        return fragmentAccountView;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            userEmail = firebaseUser.getEmail();
            txtEmail.setText(userEmail);
            userID = firebaseUser.getUid();
            txtID.setText(userID);
            verifyEmail();
        }
    }

    private void verifyEmail(){
        if(firebaseUser.isEmailVerified()){
            txtVerifiedAccount.setText("Account Verified");
            txtVerifiedAccount.setTextColor(getResources().getColor(R.color.green));
            btnSendVerificationEmail.setEnabled(false);
            btnSendVerificationEmail.setVisibility(View.GONE);
        }
    }
}