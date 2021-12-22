package com.example.covidcontrolx.fragments.booking.hospital;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.covidcontrolx.R;
//import com.example.covidcontrolx.databinding.ActivityMapsBinding;
import com.example.covidcontrolx.fragments.booking.BookingFragment;
import com.example.covidcontrolx.fragments.booking.models.Hospital;
import com.example.covidcontrolx.utils.MapUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
//public class MapsFragment extends Fragment implements OnMapReadyCallback {
public class MapsFragment extends Fragment implements MapUtils.VolleyCallback, ServicesListener {
    private static final String TAG = "MapsFragment";
    private GoogleMap googleMap;
    MapView mapView;
    FirebaseFirestore db;
    //
    private HospitalViewModel hospitalViewModel;
    private List<Hospital> hospitalList;

    ExpandableListView expandableListView;
    TextView txtServices;
    Map<Hospital, Marker> markerMap = new HashMap<>();
    List<String> filterServices;

    public MapsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        hospitalViewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);

        View fragmentMaps = inflater.inflate(R.layout.fragment_maps, container, false);

        mapView = fragmentMaps.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume(); // to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(getOnMapReadyCallback(fragmentMaps));

        // filter by services
        txtServices = (TextView) fragmentMaps.findViewById(R.id.txt_services_selected);
        expandableListView = (ExpandableListView) fragmentMaps.findViewById(R.id.expandable_list);
        ServicesExpandListAdapter adapter = new ServicesExpandListAdapter(getActivity());
        adapter.setListener(this);
        expandableListView.setAdapter(adapter);
        setGroupIndicatorToRight();

        BottomNavigationView mapsBottomNav = fragmentMaps.findViewById(R.id.bottom_navigation_maps);
        mapsBottomNav.setOnNavigationItemSelectedListener(navItem -> {
            int id = navItem.getItemId();
            if (id == R.id.maps_nav_button) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame_layout_fragment, new BookingFragment()).addToBackStack(null).commit();
//                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_home_frame_layout_fragment, new BookingFragment()).addToBackStack(null).commit(); // to be able to come back to MapsFragment
                return true;
            } else if (id == R.id.hospitals_nav_btn) {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_home_frame_layout_fragment, HospitalsFragment.newInstance()).addToBackStack(null).commit(); // to be able to come back to MapsFragment
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_frame_layout_fragment, HospitalsFragment.newInstance()).addToBackStack(null).commit();
                return true;
            }
            return true;
        });

        return fragmentMaps;
    }

    private OnMapReadyCallback getOnMapReadyCallback(View rootView) {
        return new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapsFragment.this.googleMap = googleMap;
                try {
                    MapsFragment.this.googleMap.setMyLocationEnabled(true);
                } catch (SecurityException e) {
                    Log.d(TAG, "onMapReady: " + e.getMessage());
                }
                MapsFragment.this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                MapsFragment.this.googleMap.getUiSettings().setZoomControlsEnabled(true);
                MapsFragment.this.googleMap.getUiSettings().setZoomGesturesEnabled(true);
                MapsFragment.this.googleMap.getUiSettings().setCompassEnabled(true);
                MapsFragment.this.googleMap.setPadding(0,0,0,rootView.findViewById(R.id.bottom_navigation_maps).getHeight()); // for the bottom nav bar to not block the zoom controls

                LatLng cameraStartingPosition = new LatLng(40.844378, -99.058637); // NA Region
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(cameraStartingPosition));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(cameraStartingPosition).zoom(3).build();
                MapsFragment.this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                db = FirebaseFirestore.getInstance();
                hospitalList = new ArrayList<>();
                populateHospitals();
            }
        };
    }

    private void populateHospitals() {
        googleMap.clear();// clears the map

        db.collection("hospitals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot hospitalDocument : task.getResult()) {
//                        Log.d(TAG, hospitalDocument.getId() + " => " + hospitalDocument.getData());
                        try {
                            MapUtils.geoCode(hospitalDocument, getActivity(), MapsFragment.this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    hospitalViewModel.setFilterActive(false);
                    hospitalViewModel.setHospitals(hospitalList);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                goToHospitalDetailsFragment(marker);
            }
        });
    }

    @Override
    public void onVolleyResponseSuccess(Hospital hospital) { // callback from MapUtils.geoCode
        LatLng latLng = new LatLng(hospital.getLocation().getLat(), hospital.getLocation().getLng());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(hospital.getName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .snippet(hospital.getLocation().toString());
        Marker marker = googleMap.addMarker(markerOptions);
        marker.setTag(hospital);

        markerMap.put(hospital, marker);
        hospitalList.add(hospital);
    }

    private void goToHospitalDetailsFragment(Marker marker) {
        hospitalViewModel.setSelectedHospital((Hospital) marker.getTag());

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_home_frame_layout_fragment, new HospitalDetailsFragment()).addToBackStack(null).commit(); // to be able to come back to MapsFragment
//        getActivity().getSupportFragmentManager()
//            .beginTransaction()
//            .replace(R.id.activity_home_frame_layout_fragment, new HospitalDetailsFragment()).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onServicesChanged(List<String> services) {
//        txtServices.setText("Services selected = " + services);
        hospitalViewModel.setFilterActive(true);
        filterServices = new ArrayList<>(services);
        hideMarkers();
    }

    private void hideMarkers() {
        for (Map.Entry<Hospital, Marker> row: markerMap.entrySet()) {
            Hospital hospital = row.getKey();
            Marker marker = row.getValue();

            boolean includeHospital = false;
            if (filterServices.contains("Vaccination") && (hospital.getServices().getVaccination() != null)) {
                includeHospital = true;
            } else if (filterServices.contains("PCR Test") && (hospital.getServices().getPcr_test() != null)) {
                includeHospital = true;
            } else if (filterServices.contains("Rapid Test") && (hospital.getServices().getRapid_test() != null)) {
                includeHospital = true;
            }
            marker.setVisible(includeHospital);
        }

        List<Hospital> visibleHospitals = new ArrayList<>();
        for (Map.Entry<Hospital, Marker> row: markerMap.entrySet()) {
            Hospital hospital = row.getKey();
            Marker marker = row.getValue();
            if (marker.isVisible()) {
                visibleHospitals.add(hospital);
            }
        }
        hospitalViewModel.setFilterHospitals(visibleHospitals);
    }

    @Override
    public void expandGroupEvent(int groupPosition, boolean isExpanded) {
        if(isExpanded)
            expandableListView.collapseGroup(groupPosition);
        else
            expandableListView.expandGroup(groupPosition);
    }

    private void setGroupIndicatorToRight() { // only works when screen is vertical
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        expandableListView.setIndicatorBounds(width - getDPFromPixel(35), width - getDPFromPixel(5));
    }
    private int getDPFromPixel(float pixels) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pixels * scale + 250.5f);// Convert dps to pxs
    }
}

//@Override
//public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//    View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
//
//    FragmentManager manager = getFragmentManager();
//    FragmentTransaction transaction = manager.beginTransaction();
//    SupportMapFragment fragment = new SupportMapFragment();
//    transaction.add(R.id.map, fragment);
//    transaction.commit();
//    fragment.getMapAsync(new OnMapReadyCallback() {
//        @Override
//        public void onMapReady(GoogleMap mMap) {
//            googleMap = mMap;
//
//            // For showing a move to my location button
//            try {
//                googleMap.setMyLocationEnabled(true);
//            } catch (SecurityException e) {
//                Log.d(TAG, "onMapReady: " + e.getMessage());
//            }
//
//            // For dropping a marker at a point on the Map
//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
//
//            // For zooming automatically to the location of the marker
//            CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
//            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        }
//    });
//
//    return rootView;
//}