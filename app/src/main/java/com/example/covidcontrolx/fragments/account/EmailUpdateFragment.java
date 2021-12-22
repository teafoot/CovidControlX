package com.example.covidcontrolx.fragments.account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.covidcontrolx.R;
import com.example.covidcontrolx.utils.FormValidation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailUpdateFragment extends Fragment {
    ImageView btnBackArrow;
    EditText txtOldEmail, txtNewEmail;
    Button btnUpdateEmail;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    public EmailUpdateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_email_update, container, false);
        final View fragmentEmailUpdateView = inflater.inflate(R.layout.fragment_email_update, container, false);

        txtOldEmail = fragmentEmailUpdateView.findViewById(R.id.txtOldEmail);
        txtNewEmail = fragmentEmailUpdateView.findViewById(R.id.txtNewEmail);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        setCurrentEmail();

        btnBackArrow = fragmentEmailUpdateView.findViewById(R.id.back_arrow2);
        btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                getActivity().onBackPressed(); // HomeActivity (to pop the fragment stack)

//                AccountSettingsFragment accountSettingsFragment = new AccountSettingsFragment();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame_layout_fragment, accountSettingsFragment).commit();
            }
        });

        btnUpdateEmail = fragmentEmailUpdateView.findViewById(R.id.btnEmailUpdate);
        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean emailVerified = FormValidation.validateEmail(txtNewEmail.getText().toString());
                if(emailVerified && firebaseUser != null){
                    String myNewEmail = txtNewEmail.getText().toString().trim();
                    firebaseUser.updateEmail(myNewEmail);
                    Toast.makeText(getActivity(), "Email Address Updated Successfully", Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {//Wait 2 Second to read new Email from Firebase and then update EditText
                        @Override
                        public void run() {
                            setCurrentEmail();
                        }
                    }, 3000);
                }else{
                    Toast.makeText(getActivity(), "Invalid Email Address.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return fragmentEmailUpdateView;
    }

    public void setCurrentEmail(){
        if (firebaseUser != null){
            txtOldEmail.setEnabled(true);
            txtOldEmail.setText(firebaseUser.getEmail());
            txtOldEmail.setEnabled(false);
        }
    }
}