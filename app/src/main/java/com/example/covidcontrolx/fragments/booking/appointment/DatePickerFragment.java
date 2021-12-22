package com.example.covidcontrolx.fragments.booking.appointment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.covidcontrolx.HomeActivity;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private FragmentListener fragmentListener;

    public DatePickerFragment() {
    }
    public DatePickerFragment(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    public interface FragmentListener {
        public void processDatePickerResult(int year, int month, int dayOfMonth);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        HomeActivity activity = (HomeActivity) getActivity();
//        activity.processDatePickerResult(year, month, dayOfMonth);
        fragmentListener.processDatePickerResult(year, month, dayOfMonth);
    }
}