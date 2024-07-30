package kishore.kannan.cse.emergencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivitymedical2 extends AppCompatActivity {

    Button b1 , b2;
    TextView t1;

    SQLiteDatabase database;
    EmergencyContactsHelper databaseHelper;

    private ArrayList<String> contacts = new ArrayList<>();

    private static final int SMS_REQUEST_CODE = 101;

    private ArrayList<String> ContactNumbers = new ArrayList<>();


    public static final String TAG = MainActivitymedical2.class.getSimpleName();

    private int messagesSentCount = 0;

    private smsSentReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activitymedical2);


        b1 = (Button) findViewById(R.id.button3);
        b2 = (Button) findViewById(R.id.buttonSendsms);
        t1 = findViewById(R.id.textView36);

        databaseHelper = new EmergencyContactsHelper(this);
        database = databaseHelper.getWritableDatabase();


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayContactTable();
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmergencyContactNumbers();
                String message = "Alert!!! I am in an Emergency Situation!!! Need Your Help!!";

                if(ContextCompat.checkSelfPermission(MainActivitymedical2.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
                {
                    smsReceiver = new smsSentReceiver(MainActivitymedical2.this);

                    // Register the BroadcastReceiver with an IntentFilter for "SMS_SENT"
                    registerReceiver(smsReceiver, new IntentFilter("android.telephony.SMS_SENT"));
                    sendSMSToMultipleNumbers(ContactNumbers, message);
                }
                else
                {
                    ActivityCompat.requestPermissions(MainActivitymedical2.this, new String[]{Manifest.permission.SEND_SMS},
                            SMS_REQUEST_CODE);
                }
            }
        });
    }

    public void displayContactTable()
    {
        String[] projection = {
              //  EmergencyContactsContract.ContactsInner.ID,
                EmergencyContactsContract.ContactsInner.CONTACT_NAME,
                EmergencyContactsContract.ContactsInner.CONTACT_NUMBER
        };

        Cursor cursor = database.query(EmergencyContactsContract.ContactsInner.TABLE_NAME,projection,null,null,null,null,null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String str="";
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    str += cursor.getString(i)+"    ";
                }

                str += "\n";
                Log.i(TAG, str);
                contacts.add(str);
            } while (cursor.moveToNext());

            updateTextView();
            cursor.close();
        }

    }

    public void updateTextView()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(String str: contacts)
        {
            stringBuilder.append(str).append("\n");
        }
        t1.setText(stringBuilder.toString());
    }


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


 /*   private void sendSMS(String phone, String message)
    {
        SmsManager smsManager = SmsManager.getDefault();

        messagesSentCount = 0; // Reset the count

        // Register the BroadcastReceiver dynamically
        registerReceiver(smsReceiver, new IntentFilter("SMS_SENT"));


        for(String num : ContactNumbers)
        {
            smsManager.sendTextMessage(num, null, message, null, null);
        }

        Toast.makeText(this, "SMS sent successfully", Toast.LENGTH_SHORT).show();
    }     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==SMS_REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                sendSMSToMultipleNumbers(getContactNumbers(), "Hello I am KK");
            }
            else
            {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList<String> getContactNumbers()
    {
        EmergencyContactNumbers();
        return ContactNumbers;
    }

    void showNotification(String message) {
        // Create a NotificationManager
        NotificationManager notificationManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            notificationManager = getSystemService(NotificationManager.class);
        }

        // Check if the notification channel exists, create it if not
        String channelId = "my_channel_id";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
            if (channel == null) {
                channel = new NotificationChannel(
                        channelId,
                        "My Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_baseline_emergency_share_24)
                .setContentTitle("SMS Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        notificationManager.notify(1, builder.build());
    }


    private void sendSMSToMultipleNumbers(ArrayList<String> phoneNumbers, String message) {

        ResultReceiver resultReceiver = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                int messagesSentCount = resultData.getInt("messagesSentCount", 0);
                if (messagesSentCount == ContactNumbers.size()) {
                    // Display a single notification for all numbers
                    showNotification("SMS sent successfully to all numbers");
                } else {
                    // Display a notification indicating that not all messages were sent
                    showNotification("SMS sending failed for some numbers");
                }
            }
        };

        // Start the SmsIntentService to handle SMS sending in the background
        Intent serviceIntent = new Intent(this, IntentServiceSMS.class);
        serviceIntent.setAction(IntentServiceSMS.ACTION_SEND_SMS);
        serviceIntent.putStringArrayListExtra(IntentServiceSMS.EXTRA_PHONE_NUMBERS, new ArrayList<>(phoneNumbers));
        serviceIntent.putExtra(IntentServiceSMS.EXTRA_MESSAGE, message);
        serviceIntent.putExtra(IntentServiceSMS.EXTRA_RESULT_RECEIVER, resultReceiver);
        Log.i(TAG, "service not started");
        startService(serviceIntent);
        Log.i(TAG, "Service started");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
        if(smsReceiver!=null) {
            unregisterReceiver(smsReceiver);
        }
    }



    public void incrementMessagesSentCount() {
        messagesSentCount++;
    }

    public boolean allMessagesSent() {
        return messagesSentCount == ContactNumbers.size();
    }

}