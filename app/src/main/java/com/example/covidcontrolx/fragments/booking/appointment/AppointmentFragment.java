package com.example.covidcontrolx.fragments.booking.appointment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.covidcontrolx.HomeActivity;
import com.example.covidcontrolx.R;
import com.example.covidcontrolx.fragments.booking.appointment.UserDetails.UserPersonalInfoFragment;
import com.example.covidcontrolx.fragments.booking.hospital.HospitalViewModel;
import com.example.covidcontrolx.fragments.booking.models.Booking;
import com.example.covidcontrolx.fragments.booking.models.Timeslot;
import com.example.covidcontrolx.fragments.booking.vaccine.SelectVaccineViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AppointmentFragment extends Fragment
//    implements DatePickerFragment.FragmentListener, DatePickerFragment2.FragmentListener
implements DatePickerDialog.OnDateSetListener,
        AdapterView.OnItemSelectedListener {
    private static final String TAG = "AppointmentFragment";

    private HospitalViewModel hospitalViewModel;
    private SelectVaccineViewModel selectVaccineViewModel;
    private AppointmentViewModel appointmentViewModel;

    private ImageView imgHospital;
    private TextView txtHospitalName, txtHospitalAddress;
    private Button btnRequirements, btnVaccineDetails, btnBookAppointment;

    private EditText txtDatePicker;
    private DatePickerDialog dpd;
    String year, month, day;
    String date;
    Spinner spinnerTime;
    Button spinnerTimeBtn;
    List<String> timeslots;
    String startTime;
    List<Integer> disabledItemsPos;

    FirebaseFirestore db;
    CollectionReference collectionReference;
    List<Booking> bookingList = new ArrayList<>();

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    public AppointmentFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointment, container, false);

        hospitalViewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);
        selectVaccineViewModel = new ViewModelProvider(requireActivity()).get(SelectVaccineViewModel.class);
        appointmentViewModel = new ViewModelProvider(requireActivity()).get(AppointmentViewModel.class);

        imgHospital = rootView.findViewById(R.id.img_appointment_hospital);
        Glide.with(this).load(hospitalViewModel.getSelectedHospital().getValue().getImage()).override(1400, 280).into(imgHospital);

        txtHospitalName = rootView.findViewById(R.id.txtHospitalName);
        txtHospitalName.setText(hospitalViewModel.getSelectedHospital().getValue().getName());

        txtHospitalAddress = rootView.findViewById(R.id.txtHospitalAddress);
        txtHospitalAddress.setText(hospitalViewModel.getSelectedHospital().getValue().getLocation().toString());


        btnBookAppointment = rootView.findViewById(R.id.btnBookAppointment);
        txtDatePicker = rootView.findViewById(R.id.txtDatePicker);

        spinnerTime = rootView.findViewById(R.id.spinTime);
        spinnerTime.setOnItemSelectedListener(this);
//        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) view).setTextColor(Color.parseColor("#00ff00"));//Change the spinner's current item's text color
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        });
        spinnerTime.setPrompt("Select vaccination time");
        spinnerTime.setGravity(Gravity.CENTER);
        spinnerTime.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        btnRequirements = rootView.findViewById(R.id.btnRequirements);
        btnRequirements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertRequirements = new AlertDialog.Builder(getActivity());
                alertRequirements.setTitle("Requirements")
                    .setMessage("1. Wear mask when coming for vaccination.\n2. Bring health card.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                alertRequirements.show();
            }
        });

        btnVaccineDetails = rootView.findViewById(R.id.appointment_btnVaccineDetail);
        btnVaccineDetails.setText(selectVaccineViewModel.getVaccineTypeSelected());
        btnVaccineDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed(); // keep as long as vaccine detail fragment is behind this current fragment
            }
        });

//        DialogFragment datePickerFragment = new DatePickerFragment(AppointmentFragment.this);
//        DatePickerFragment2 datePickerFragment2 = new DatePickerFragment2(AppointmentFragment.this);
        txtDatePicker.requestFocus(); // edittext requires 2 clicks
        txtDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                datePickerFragment.show(getActivity().getSupportFragmentManager(),"datePicker");
//                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_home_frame_layout_fragment, datePickerFragment2).addToBackStack(null).commit(); // to be able to come back to AppointmentFragment

                Calendar now = Calendar.getInstance();
                now.add(Calendar.DATE, 1); // set tomorrow as default date
                if (dpd == null) {
                    dpd = DatePickerDialog.newInstance(
                        AppointmentFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                    );
                } else {
                    dpd.initialize(
                        AppointmentFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                    );
                }

                // set min date to tomorrow
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis() + 86400000);
                dpd.setMinDate(cal);

                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.setTitle("Pick a vaccination date");
                // only selectable dates
                Calendar calendar;
                Date date = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                for (int i = 0; i < bookingList.size(); i++) {
                    try {
                        date = sdf.parse(bookingList.get(i).getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    calendar = dateToCalendar(date);
                    List<Calendar> dates = new ArrayList<>();
                    dates.add(calendar);
                    Calendar[] selectableDates = dates.toArray(new Calendar[dates.size()]);
                    dpd.setSelectableDays(selectableDates);
                }
                //
                dpd.setOnCancelListener(dialog -> {
                    Log.d("DatePickerDialog", "Dialog was cancelled");
                    dpd = null;
                });
                dpd.show(requireFragmentManager(), "Datepickerdialog");
            }
        });

        btnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtDatePicker.getText().toString().equals("Select date")) {
                    Toast.makeText(getActivity(), "Select a valid vaccination date!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (startTime.equals("")) {
                    Toast.makeText(getActivity(), "Select a valid vaccination time!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check if user email is verified
//                firebaseAuth = FirebaseAuth.getInstance();
//                firebaseUser = firebaseAuth.getCurrentUser();
//                if(firebaseUser != null){
//                    if(!firebaseUser.isEmailVerified()){
//                        Toast.makeText(getActivity(), "Verify your email before booking an appointment!", Toast.LENGTH_SHORT).show();
//                    } else {
                        appointmentViewModel.setDate(txtDatePicker.getText().toString());
                        appointmentViewModel.setStartTime(startTime);
                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_home_frame_layout_fragment, new UserPersonalInfoFragment()).addToBackStack(null).commit(); // to be able to come back to AppointmentFragment
//                    }
//                }
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("hospitals");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot hospitalDocument : task.getResult()) { // hospital
                        if (hospitalDocument.getString("name") != null)
                            if (hospitalDocument.getString("name").equals(hospitalViewModel.getSelectedHospital().getValue().getName())) {
                            try {
                                List<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) hospitalDocument.get("bookings");
                                JSONArray bookingsArray = new JSONArray(list);
                                for (int i = 0; i < bookingsArray.length(); i++) {
                                    Booking booking = new Booking();
                                    JSONObject bookingObj = (JSONObject) bookingsArray.get(i);
                                    booking.setDate(bookingObj.getString("date"));

                                    List<Timeslot> timeslotList = new ArrayList<>();
                                    JSONArray timeslots = bookingObj.getJSONArray("timeslots");
                                    for (int j = 0; j < timeslots.length(); j++) {
                                        JSONObject timeslotObj = (JSONObject) timeslots.get(j);
//                                        if (timeslotObj.getString("status").equals("occupied")) {
//                                            continue; // skip fetching occupied timeslot
//                                        }

                                        Timeslot timeslot = new Timeslot();
                                        timeslot.setTimeslot_id(timeslotObj.getInt("timeslot_id"));
                                        timeslot.setTimestart(timeslotObj.getString("timestart"));
                                        timeslot.setTimeend(timeslotObj.getString("timeend"));
                                        timeslot.setStatus(timeslotObj.getString("status"));

                                        timeslotList.add(timeslot);
                                    }

                                    booking.setTimeslotList(timeslotList);
                                    if (booking.getTimeslotList().size() > 0) { // at least 1 unoccupied timeslot for the booking date
                                        bookingList.add(booking);
                                    }
                                }

                                AppointmentFragment.this.bookingList = bookingList;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }
        });
    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

//    @Override
//    public void processDatePickerResult(int year, int month, int day) {
//        String month_string = Integer.toString(month+1);
//        String day_string = Integer.toString(day);
//        String year_string = Integer.toString(year);
//        String dateMessage = (month_string + "/" + day_string + "/" + year_string);
//
////        Toast.makeText(getActivity(), "Date: " + dateMessage, Toast.LENGTH_SHORT).show();
//        txtDatePicker.setText(dateMessage);
//    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.year = Integer.toString(year);
        this.month = String.format("%02d", monthOfYear+1); // pad a zero if single digit
        this.day = String.format("%02d", dayOfMonth); // pad a zero if single digit
        String date = this.year + "-" + this.month + "-" + this.day;
        txtDatePicker.setText(date);

        //load spinner from time ranges of specific date
        this.timeslots = getTimeslotsForDate(date);
        ArrayAdapter<String> aa = new ArrayAdapter(getActivity(), R.layout.spinner_item, this.timeslots) {
            @Override
            public boolean isEnabled(int position){ // disable item at index
                return !disabledItemsPos.contains(position);
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(disabledItemsPos.contains(position)) { // set disabled color
                    tv.setTextColor(Color.RED);
                    tv.setBackgroundColor(Color.LTGRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                    tv.setBackgroundColor(Color.WHITE);
                }
                return view;
            }
        };
        spinnerTime.setAdapter(aa);
        // set default selection to an unoccupied timeslot for the selected date
        for (int i = 0; i < this.timeslots.size(); i++) {
            if (!disabledItemsPos.contains(i)) {
                spinnerTime.setSelection(i);
                break;
            }
        }
    }

    private List<String> getTimeslotsForDate(String date) {
        List<String> timeslots = new ArrayList<>();
        disabledItemsPos = new ArrayList<>();

        for (int i = 0; i < this.bookingList.size(); i++) {
            if (date.equals(this.bookingList.get(i).getDate())) {
                List<Timeslot> timeslotList = this.bookingList.get(i).getTimeslotList();
                for (int j = 0; j < timeslotList.size(); j++) {
                    Timeslot timeslot = timeslotList.get(j);
                    if (timeslot.getStatus().equals("occupied")) {
                        timeslots.add("Occupied:\n" + timeslot.getTimestart() + " - " + timeslot.getTimeend());
                        disabledItemsPos.add(j);
                    } else {
                        timeslots.add(timeslot.getTimestart() + " - " + timeslot.getTimeend());
                    }
                }
                break;
            }
        }
        return timeslots;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getActivity(), this.timeslots.get(position), Toast.LENGTH_SHORT).show();
        startTime = this.timeslots.get(position).split(" - ")[0];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dpd = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) requireFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null)
            dpd.setOnDateSetListener(this);
    }
}