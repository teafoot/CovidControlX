package com.example.covidcontrolx.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import com.example.covidcontrolx.R;
import com.example.covidcontrolx.fragments.booking.hospital.HospitalsFragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class FragmentUtils {
//    public static void displayFragment(Context context, Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        BlankFragment blankFragment = BlankFragment.newInstance();
//        fragmentTransaction.add(R.id.fragment_container, blankFragment).addToBackStack(null).commit();
//    }
//
//    public static void closeFragment(Context context, Fragment fragment) {
//        FragmentManager fragmentManager = (context.getApplicationContext()).getSupportFragmentManager();
////        BlankFragment blankFragment = (BlankFragment) fragmentManager.findFragmentById(R.id.fragment_container);
//        if (blankFragment != null) {
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.remove(fragment).commit();
//        }
//    }

//    public static void setHospitalRecyclerViewVisibility(Context c, boolean visible) {
//        List<Fragment> fragmentList = getFragmentActivity(c).getSupportFragmentManager().getFragments();
//        for (Fragment f: fragmentList) {
//            if (f instanceof HospitalsFragment) {
//                ((HospitalsFragment) f).setHospitalRecyclerViewVisibility(visible);
//                break;
//            }
//        }
//    }

    private static FragmentActivity getFragmentActivity(Context context)
    {
        if (context == null) {
            return null;
        } else if (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (FragmentActivity) context;
            } else {
                return getFragmentActivity(((ContextWrapper) context).getBaseContext());
            }
        }
        return null;
    }
}
