package kishore.kannan.cse.emergencyapp;

import static kishore.kannan.cse.emergencyapp.MainActivity2.TAG;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationSharingService extends Service {
    public LocationSharingService() {
    }

    public static final String EXTRA_LOCATION = "extra_location";

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Location lastKnownLocation;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Set up location callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                lastKnownLocation = locationResult.getLastLocation();
            }
        };

        // Start location updates
        startLocationUpdates();

        return START_STICKY;
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "Location sharing");
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(10000); // 10 seconds
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

        } else {
            // Handle the case where location permission is not granted
            // You might want to request permission or show a message to the user
            Log.e(TAG, "Location permission not granted");
        }
    }

    @Override
    public void onDestroy() {
        // Stop location updates when the service is destroyed
        stopLocationUpdates();
        super.onDestroy();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}