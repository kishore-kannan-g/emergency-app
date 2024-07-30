package kishore.kannan.cse.emergencyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import api2.GooglePlacesApiService;
import api2.PlacesApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MapsFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private static final int SHARE_LOCATION_REQUEST_CODE = 123;

    private GoogleMap googleMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            MapsFragment.this.googleMap = googleMap;

            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                // Add a marker at the user's current location
                LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, true);
                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                }
            } else {
                // Request location permission
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }

    };


    public void shareLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Get the current location
            LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                // Pass the location to the service for sharing
                Intent intent = new Intent(requireContext(), LocationSharingService.class);
                intent.putExtra(LocationSharingService.EXTRA_LOCATION, location);
                requireContext().startService(intent);
            }
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


















    private void showNearbyHospitals(LatLng currentLocation, List<Hospital> nearbyHospitals) {
        if (googleMap != null) {
            // Clear existing markers
            googleMap.clear();

            // Add a marker for the user's current location
            googleMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location"));

            // Add markers for nearby hospitals
            for (Hospital hospital : nearbyHospitals) {
                googleMap.addMarker(new MarkerOptions().position(hospital.getLocation()).title(hospital.getName()));
            }

            // Move camera to the user's current location
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        } else {
            Log.e("MapsFragment", "showNearbyHospitals: GoogleMap is null");
        }
    }


    void getNearbyHospitalsAsync(LatLng currentLocation) {
        GooglePlacesApiService apiService = createApiService();
        Call<PlacesApiResponse> call = apiService.getNearbyHospitals(
                currentLocation.latitude + "," + currentLocation.longitude,
                10000, // Radius in meters (adjust as needed)
                "hospital",
                "@string/MAPS_API_KEY"
        );

        call.enqueue(new Callback<PlacesApiResponse>() {
            @Override
            public void onResponse(Call<PlacesApiResponse> call, Response<PlacesApiResponse> response) {
                if (response.isSuccessful()) {
                    PlacesApiResponse placesApiResponse = response.body();
                    if (placesApiResponse != null && placesApiResponse.getPlaces() != null) {
                        Log.d("MapsFragment", "Number of places received: " + placesApiResponse.getPlaces().size());

                        // Process the nearby hospitals data
                        List<Hospital> nearbyHospitals = new ArrayList<>();
                        for (PlacesApiResponse.Place place : placesApiResponse.getPlaces()) {
                            LatLng hospitalLocation = place.getLocation();
                            nearbyHospitals.add(new Hospital(place.getName(), hospitalLocation));
                        }

                        // Update the map with nearby hospitals
                        showNearbyHospitals(currentLocation, nearbyHospitals);
                    }
                } else {
                    Log.e("MapsFragment", "API request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PlacesApiResponse> call, Throwable t) {
                Log.e("MapsFragment", "API request failed", t);
            }
        });
    }


    private GooglePlacesApiService createApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(GooglePlacesApiService.class);
    }


    public class Hospital {
        private String name;
        private LatLng location;

        public Hospital(String name, LatLng location) {
            this.name = name;
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public LatLng getLocation() {
            return location;
        }
    }


}
