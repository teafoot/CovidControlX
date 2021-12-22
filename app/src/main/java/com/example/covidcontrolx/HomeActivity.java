package com.example.covidcontrolx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Process;
import android.view.MenuItem;

import com.example.covidcontrolx.fragments.account.AccountSettingsFragment;
import com.example.covidcontrolx.fragments.booking.hospital.HospitalViewModel;
import com.example.covidcontrolx.fragments.home.HomeFragment;
import com.example.covidcontrolx.fragments.booking.BookingFragment;
import com.example.covidcontrolx.utils.NetworkUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
//        , FragmentManager.OnBackStackChangedListener
{
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    BookingFragment bookingFragment = new BookingFragment();
    AccountSettingsFragment accountSettingsFragment = new AccountSettingsFragment();

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        getSupportFragmentManager().addOnBackStackChangedListener(this);

        bottomNavigationView = findViewById(R.id.activity_home_bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!NetworkUtils.getConnection(getApplicationContext())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Please connect to the internet to use the app.");
            final AlertDialog dialog = builder.create();
            dialog.setButton(Dialog.BUTTON_POSITIVE, "Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.dismiss();
                }
            });
            dialog.setButton(Dialog.BUTTON_NEGATIVE, "Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.dismiss();
                    Process.killProcess(android.os.Process.myPid());
                }
            });
            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) { // no more fragments on the stack
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Log out?");
            builder.setMessage("Do you want to log out?");
            final AlertDialog dialog = builder.create();
            dialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    firebaseAuth.signOut();
                    finish();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
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
        } else {
            getSupportFragmentManager().popBackStack(); // pop the fragment stack
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame_layout_fragment, homeFragment).commit();
                return true;
            case R.id.bottom_nav_booking:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame_layout_fragment, bookingFragment).commit();
                return true;
            case R.id.bottom_nav_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame_layout_fragment, accountSettingsFragment).commit();
                return true;
        }
        return false;
    }
}

//    @Override
//    public void onBackStackChanged() {
//        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
//        Fragment fragment = fragmentList.get(fragmentList.size() - 1); // get the top fragment
//        Log.d(TAG, "onBackStackChanged: " + fragmentList.size());
//        if (fragment instanceof HospitalsFragment) {
////            FragmentUtils.setHospitalRecyclerViewVisibility(this, true);
//            ((HospitalsFragment) fragment).setHospitalRecyclerViewVisibility(true);
//        } else if (fragment instanceof HospitalDetailsFragment) {
////            FragmentUtils.setHospitalRecyclerViewVisibility(this, false);
//        }
//    }