package kishore.kannan.cse.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivitymedical1 extends AppCompatActivity {

    Button button1, button2, button3;
    private GpsReceiver gpsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activitymedical1);

        button1 = (Button) findViewById(R.id.buttonmedical1);
        button2 = (Button) findViewById(R.id.buttonmedical2);
        button3 = (Button) findViewById(R.id.call);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivitymedical1.this, MainActivitymedical2.class);
                startActivity(i);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivitymedical1.this, MainActivity9.class);
                startActivity(i);
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gpsReceiver = new GpsReceiver();
                registerGpsReciever();


                // Register the GPS enabled receiver
              /*  gpsEnabledReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        // GPS is enabled, navigate to the next activity
                        startActivity(new Intent(MainActivitymedical1.this, MainActivitymedical3.class));
                        finish(); // Optionally finish the current activity
                    }
                };
                registerReceiver(gpsEnabledReceiver, new IntentFilter(GpsReceiver.GPS_ENABLED_ACTION));


               */


            }
        });
    }


    public void registerGpsReciever() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            IntentFilter filter = new IntentFilter();
            filter.addAction("android.location.PROVIDERS_CHANGED");
            registerReceiver(gpsReceiver, filter);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the receivers when the activity is destroyed
        if (gpsReceiver != null) {
            unregisterReceiver(gpsReceiver);
        }
     /*   if (gpsEnabledReceiver != null) {
            unregisterReceiver(gpsEnabledReceiver);
        }

      */
    }
}


