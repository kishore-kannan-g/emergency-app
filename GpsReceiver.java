package kishore.kannan.cse.emergencyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

public class GpsReceiver extends BroadcastReceiver
{
    // public static final String GPS_ENABLED_ACTION = "com.example.gps_enabled";

    //   private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        //  this.context = context;

        if (isGpsEnabled(context)) {
            // GPS is enabled, navigate to the next activity
            Intent intent1 = new Intent(context, MainActivitymedical3.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent1);
            Toast.makeText(context, "GPS is enabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "GPS is disabled", Toast.LENGTH_SHORT).show();
        }


    }

    public boolean isGpsEnabled(Context context)
    {
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
