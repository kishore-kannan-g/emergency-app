package kishore.kannan.cse.emergencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

    public class MainActivity9 extends AppCompatActivity {

        private static final int REQUEST_CALL_PHONE = 1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main9);

            // Find buttons by their IDs
            Button button103 = findViewById(R.id.button103);
            Button button108 = findViewById(R.id.button108);
            Button button100 = findViewById(R.id.button100);
            Button button181 = findViewById(R.id.button181);
            Button buttonnmc = findViewById(R.id.buttonnmc);
            Button buttonvmc = findViewById(R.id.buttonvmc);

            // Set click listeners for each button
            button103.setOnClickListener(v -> makePhoneCall("tel: 0413 265 1111")); // piims

            button108.setOnClickListener(v -> makePhoneCall("tel: 0413 233 7070")); // indira gandhi government hospital

            button100.setOnClickListener(v -> makePhoneCall("tel: 0413 229 6562")); // jipmer

            button181.setOnClickListener(v -> makePhoneCall("tel: 0413 224 3160"));// eastcoast

            buttonnmc.setOnClickListener(v -> makePhoneCall("tel:  0413 226 1200")); // new medical center

            buttonvmc.setOnClickListener(v -> makePhoneCall("tel:  0413 226 0601")); // venkateshwara
        }

        private void makePhoneCall(String phoneNumber) {
            // Check if the CALL_PHONE permission is granted
            if (ContextCompat.checkSelfPermission(kishore.kannan.cse.emergencyapp.MainActivity9.this, android.Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request the permission if not granted
                ActivityCompat.requestPermissions(kishore.kannan.cse.emergencyapp.MainActivity9.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
            } else {
                // Permission already granted, initiate the phone call
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(phoneNumber));
                startActivity(intent);
            }
        }

        // Handle the result of the permission request
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == REQUEST_CALL_PHONE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, initiate the phone call
                    makePhoneCall("tel:+1234567890");
                }
            }
        }
    }

