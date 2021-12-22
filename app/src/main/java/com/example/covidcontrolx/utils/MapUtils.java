package com.example.covidcontrolx.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidcontrolx.fragments.booking.models.Booking;
import com.example.covidcontrolx.fragments.booking.models.Hospital;
import com.example.covidcontrolx.fragments.booking.models.Location;
import com.example.covidcontrolx.fragments.booking.models.Pcr_test;
import com.example.covidcontrolx.fragments.booking.models.Rapid_test;
import com.example.covidcontrolx.fragments.booking.models.Services;
import com.example.covidcontrolx.fragments.booking.models.Timeslot;
import com.example.covidcontrolx.fragments.booking.models.Vaccination;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapUtils {
    private static final String TAG = "MapUtils";

    public interface VolleyCallback {
        void onVolleyResponseSuccess(Hospital hospital);
    }

//    public static void geoCode(String address, Context context, VolleyCallback callback) { // getLatLngFromAPI
    public static void geoCode(QueryDocumentSnapshot hospitalDocument, Context context, VolleyCallback callback) throws JSONException { // getLatLngFromAPI
        Hospital hospital = new Hospital();

        // Hospital Details
        hospital.setHospital_id(hospitalDocument.getString("hospital_id"));
        String hospitalName = hospitalDocument.getString("name");
        hospital.setName(hospitalName);
        hospital.setPhone(hospitalDocument.getString("phone"));
        hospital.setImage(hospitalDocument.getString("image"));

        // Hospital Location
        String streetAddress = hospitalDocument.getString("location.street_address");
        String city = hospitalDocument.getString("location.city");
        String state = hospitalDocument.getString("location.state");
        String zipCode = hospitalDocument.getString("location.zip_code");
        String countryCode = hospitalDocument.getString("location.country_code");
        hospital.setLocation(new Location(streetAddress, city, "", state, zipCode, countryCode, 0, 0));

        setHospitalServices(hospitalDocument, hospital);

        String URL = "https://maps.googleapis.com/maps/api/geocode/json?address=" + String.join(", ", new String[]{hospitalName, streetAddress, city, state, zipCode, countryCode}) + "&key=AIzaSyAnCLLwfwOHU9YnMTzSsckMT5_zDv3dkr4";
        StringRequest request = new StringRequest(Request.Method.GET, URL,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject hospitalJSON = new JSONObject(response);
                    JSONObject location = hospitalJSON.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                    double lat = location.getDouble("lat");
                    double lng = location.getDouble("lng");
                    hospital.getLocation().setLat(lat);
                    hospital.getLocation().setLng(lng);

                    callback.onVolleyResponseSuccess(hospital);
                } catch (JSONException e) {
                    Log.d(TAG, "onResponse: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        if (context != null) { // add safety
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        }
    }

    //TODO: use this query later on, at this point we just need all hospital names and addresses
    private static void setHospitalServices(QueryDocumentSnapshot hospitalDocument, Hospital hospital) throws JSONException {
        List<Vaccination> vaccinationList = null;
        Pcr_test pcrTest = null;
        Rapid_test rapidTest = null;

        if (hospitalDocument.get("services.vaccination") != null) {
            JSONArray vaccineTypes = JSONUtils.listToJSONArray((ArrayList<Object>) hospitalDocument.get("services.vaccination"));
            vaccinationList = new ArrayList<>();
            for (int i = 0; i < vaccineTypes.length(); i++) {
                Vaccination vaccination = new Vaccination();
                JSONObject vaccine = vaccineTypes.getJSONObject(i);

                vaccination.setVaccine_id(vaccine.getInt("vaccine_id"));
                vaccination.setName(vaccine.getString("name"));
                vaccination.setDescription(vaccine.getString("description"));
                vaccination.setDosage_amount(vaccine.getString("dosage_amount"));
                vaccination.setEfficacy(vaccine.getDouble("efficacy"));
                vaccination.setSide_effects(JSONUtils.jsonArrayToList(vaccine.getJSONArray("side_effects")));
                vaccination.setQty_available(vaccine.getInt("qty_available"));

                vaccinationList.add(vaccination);
            }
        }

        if (hospitalDocument.get("services.pcr_test") != null) {
//            JSONObject pcrTestObj = (JSONObject) hospitalDocument.get("services.pcr_test");
            JSONObject pcrTestObj = JSONUtils.mapToJSONObject((Map<String, Object>) hospitalDocument.get("services.pcr_test"));

            pcrTest = new Pcr_test();
            pcrTest.setSwab_qty(pcrTestObj.getInt("swab_qty"));
        }

        if (hospitalDocument.get("services.rapid_test") != null) {
//            JSONObject rapidTestObj = (JSONObject) hospitalDocument.get("services.rapid_test");
            JSONObject rapidTestObj = JSONUtils.mapToJSONObject((Map<String, Object>) hospitalDocument.get("services.rapid_test"));

            rapidTest = new Rapid_test();
            rapidTest.setSwab_qty(rapidTestObj.getInt("swab_qty"));
        }

        hospital.setServices(new Services(vaccinationList, pcrTest, rapidTest));

//        JSONObject obj = JSONUtils.mapToJSONObject(hospitalDocument.getData());
//        Log.d(TAG, "test: " + obj);
    }
}
