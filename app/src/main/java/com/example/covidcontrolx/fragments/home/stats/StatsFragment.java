package com.example.covidcontrolx.fragments.home.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidcontrolx.R;
import com.example.covidcontrolx.utils.CountryUtils;
import com.example.covidcontrolx.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StatsFragment extends Fragment {
    private static final String TAG = "StatsFragment";
    RecyclerView rvCountries;
    ProgressBar progressBar;
    CountryAdapter countryAdapter;
    List<Country> countryList = new ArrayList<>();

    public static StatsFragment newInstance() {
        return new StatsFragment();
    }
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View statsFragmentView = inflater.inflate(R.layout.fragment_stats, container, false);
//        TextView textView = statsFragmentView.findViewById(R.id.txtNews);
//        textView.setText(""+getArguments().getInt("position")); // from DynamicFragmentAdapter
//        textView.setText("StatsFragment");

        progressBar = statsFragmentView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        rvCountries = statsFragmentView.findViewById(R.id.rvCountries);
        rvCountries.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCountries.setAdapter(countryAdapter);
        getStatsFromAPI();

        return statsFragmentView;
    }

    private void getStatsFromAPI() {
//        String URL = "https://disease.sh/v3/covid-19/historical/all?lastdays=all";
        String URL = "https://api.apify.com/v2/key-value-stores/tVaYRsPHLjNdNBu7S/records/LATEST?disableRedirect=true";

        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<String> flagURLs = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String countryCode = CountryUtils.getCountryCode(obj.getString("country"));
                        String flagURL = "https://www.countryflags.io/"+countryCode+"/shiny/64.png";
                        flagURLs.add(flagURL);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        String infected = obj.getString("infected");
                        String tested = obj.getString("tested");
                        String recovered = obj.getString("recovered");
                        String deceased = obj.getString("deceased");
                        String theCountry = obj.getString("country");
                        String moreData = obj.getString("moreData");
                        String historyData = obj.getString("historyData");
                        String sourceUrl = ""; // obj.getString("sourceUrl"); doesn't work
                        String lastUpdatedApify = DateUtils.parseISODate(obj.getString("lastUpdatedApify"), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                        Country country = new Country(infected, tested, recovered, deceased, theCountry, moreData, historyData, sourceUrl, lastUpdatedApify);
                        country.setFlag(flagURLs.get(i));
                        countryList.add(country);
                    }

                    progressBar.setVisibility(View.GONE);
                    countryAdapter = new CountryAdapter(getActivity().getApplicationContext(), countryList);
                    rvCountries.setAdapter(countryAdapter);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}