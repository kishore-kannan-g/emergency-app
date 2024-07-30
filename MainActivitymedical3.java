package kishore.kannan.cse.emergencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivitymedical3 extends AppCompatActivity {


    public static final String TAG = MainActivitymedical3.class.getSimpleName();


    SQLiteDatabase database;
    EmergencyContactsHelper databaseHelper;
    private ArrayList<String> ContactNumbers = new ArrayList<>();


    FragmentManager manager = getSupportFragmentManager();

    Button b1, b2, b3, b4;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    MapsFragment mapsFragment;
 //   private static final int SHARE_LOCATION_REQUEST_CODE = 123;

    private LocationManager locationManager;
    private int REQUEST_LOCATION_2 = 108;
    private boolean isLocationUpdatesStarted = false;

    private final long LOCATION_INTERVAL = 1 * 60 * 1000;
    private Handler locationUpdateHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activitymedical3);

        b1 = findViewById(R.id.buttonMap1);
        b2 = findViewById(R.id.buttonMap2);
        b3 = findViewById(R.id.buttonShare);
      //  b4 = findViewById(R.id.buttonStopShare);


        databaseHelper = new EmergencyContactsHelper(this);
        database = databaseHelper.getWritableDatabase();


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        MapsFragment mapsFragment = new MapsFragment();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.add(R.id.containerMap,mapsFragment,"fragmentMap");
//                transaction.commit();

                if (ContextCompat.checkSelfPermission(MainActivitymedical3.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivitymedical3.this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                } else {
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.containerMap, mapsFragment, "fragmentMap");
                    transaction.commit();
                }

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(MainActivitymedical3.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(MainActivitymedical3.this,
//                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                            MY_PERMISSIONS_REQUEST_LOCATION);
//                } else {
//                    // Get the reference to the existing MapsFragment
//                    MapsFragment mapsFragment = (MapsFragment) getSupportFragmentManager().findFragmentByTag("fragmentMap");
//
//                    if (mapsFragment != null) {
//                        // Call the method to show nearby hospitals asynchronously
//                        LatLng currentLocation = getCurrentLocation();
//                        mapsFragment.getNearbyHospitalsAsync(currentLocation);
//                    }
//                }

                if (mapsFragment != null) {
                    mapsFragment.shareLocation();
                }
            }
        });


        if (checkLocationPermission()) {
            startLocationUpdates();
        }


     /*   b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   stopLocationUpdates();
            }
        });         */


    }

    //   @Override
 /*   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, perform your map-related operations
                // You may want to recreate the fragment or update the current location here
            } else {
                // Permission denied
                // You may want to handle this case, show a message, or request permission again
            }
        }
    }

    private LatLng getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
        }

        return new LatLng(0, 0); // Default to (0, 0) if location not available
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SHARE_LOCATION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // The location was successfully shared
                Toast.makeText(this, "Location shared successfully", Toast.LENGTH_SHORT).show();
            } else {
                // The user canceled the sharing operation or an error occurred
                Toast.makeText(this, "Location sharing canceled or failed", Toast.LENGTH_SHORT).show();
            }
        }
    }     */




    public void EmergencyContactNumbers()
    {
        String[] projection = {
                EmergencyContactsContract.ContactsInner.CONTACT_NUMBER
        };
        Cursor cursor = database.query(EmergencyContactsContract.ContactsInner.TABLE_NAME, projection,null, null,null,null, null);
        if(cursor!=null && cursor.moveToFirst())
        {
            do{
                String str = "";
                for(int i=0;i<cursor.getColumnCount();i++)
                {
                    str = cursor.getString(i)+"\n";
                }
                Log.i(TAG,str);
                ContactNumbers.add(str);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }



    //SHARE BUTTON CLICK

    public void onShareLocationClick(View view) {
        // Button click handler for sharing location via SMS
        if (checkLocationPermission()) {
            Location lastKnownLocation = getLastKnownLocation();
            if (lastKnownLocation != null) {
                sendLocationViaSMS(lastKnownLocation);
            } else {
                Toast.makeText(this, "Location not available in onShareLocationClick()", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            // Request location and SMS permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS}, REQUEST_LOCATION_2);
            return false;
        } else {
            return true;
        }
    }


    private void startLocationUpdates() {
        if (!isLocationUpdatesStarted) {
            // Check if location providers are enabled
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                // Request location updates
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // If permissions are not granted, request them
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_2);
                } else {
                    // Permissions are already granted, start location updates
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            5000,
                            10,
                            new LocationListener() {
                                @Override
                                public void onLocationChanged(@NonNull Location location) {
                                    // Handle location change
                                    // This method can be left empty since we are using the button to trigger location sharing
                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle extras) {
                                    // Handle status changes if needed
                                }

                                @Override
                                public void onProviderEnabled(@NonNull String provider) {
                                    // Handle provider enabled event if needed
                                }

                                @Override
                                public void onProviderDisabled(@NonNull String provider) {
                                    // Handle provider disabled event if needed
                                }
                            }
                    );
                    isLocationUpdatesStarted = true;
                }
            } else {
                Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private Location getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            return null;
        }
    }

    private void sendLocationViaSMS(Location location) {
        // Implement your logic for sending the location via SMS
      //  String phoneNumber = "9566907349"; // Replace with the recipient's phone number

        EmergencyContactNumbers();

        String message = "My current location: https://maps.google.com/?q=" +
                location.getLatitude() + "," + location.getLongitude();

        SmsManager smsManager = SmsManager.getDefault();

        for(String num : ContactNumbers)
        {
            smsManager.sendTextMessage(num, null, message, null, null);
        }

        Toast.makeText(this, "Location sent via SMS", Toast.LENGTH_SHORT).show();
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_2 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, start location updates
            startLocationUpdates();
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }


    //location update with time interval

  /*  private void startLocationUpdates() {
        if (!isLocationUpdatesStarted) {
            // Check if location providers are enabled
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            {
                // Request location updates
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    // If permissions are not granted, request them
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_2);
                } else
                {
                    locationUpdateHandler.postDelayed(locationUpdateRunnable, LOCATION_INTERVAL);

                    isLocationUpdatesStarted = true;

                }
            }
            else {
                Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void stopLocationUpdates() {
        if (isLocationUpdatesStarted) {
            locationUpdateHandler.removeCallbacks(locationUpdateRunnable);
            isLocationUpdatesStarted = false;
            Toast.makeText(this, "Location updates stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private Runnable locationUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (checkLocationPermission()) {
                Location lastKnownLocation = getLastKnownLocation();
                if (lastKnownLocation != null) {
                    Log.i(TAG, "this is location update runnable1;");
                    sendLocationViaSMS(lastKnownLocation);
                } else {
                    Toast.makeText(MainActivitymedical3.this, "Location not available", Toast.LENGTH_SHORT).show();
                }

                // Schedule the next location update
                locationUpdateHandler.postDelayed(this, LOCATION_INTERVAL);
            } else {
                // If permission is not granted, stop the updates
                stopLocationUpdates();
            }
        }
    };         */
}