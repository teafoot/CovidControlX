package com.example.covidcontrolx.fragments.account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.covidcontrolx.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordUpdateFragment extends Fragment {
    ImageView btnBackArrow;
    EditText txtNewPassword, txtNewPasswordRepeat;
    Button btnUpdatePass;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    public PasswordUpdateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_password_update, container, false);
        final View fragmentPasswordUpdateView = inflater.inflate(R.layout.fragment_password_update, container, false);

        txtNewPassword = fragmentPasswordUpdateView.findViewById(R.id.txtPasswordNew);
        txtNewPasswordRepeat = fragmentPasswordUpdateView.findViewById(R.id.txtPasswordRepeatNew);

        btnBackArrow = fragmentPasswordUpdateView.findViewById(R.id.btnBackArrow);
        btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                getActivity().onBackPressed(); // HomeActivity (to pop the fragment stack)

//                AccountSettingsFragment accountSettingsFragment = new AccountSettingsFragment();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame_layout_fragment, accountSettingsFragment).commit();
            }
        });

        btnUpdatePass = fragmentPasswordUpdateView.findViewById(R.id.btnPasswordUpdate);
        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = txtNewPassword.getText().toString().trim();
                String newPasswordRepeat = txtNewPasswordRepeat.getText().toString().trim();
                if(newPassword.equals(newPasswordRepeat)){
                    firebaseUser.updatePassword(newPassword);
                    Toast.makeText(getActivity(), "Password Updated.", Toast.LENGTH_SHORT).show();
//                    finish();
                    AccountSettingsFragment accountSettingsFragment = new AccountSettingsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame_layout_fragment, accountSettingsFragment).commit();
                }else{
                    Toast.makeText(getActivity(), "Passwords need to match.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        return fragmentPasswordUpdateView;
    }
}