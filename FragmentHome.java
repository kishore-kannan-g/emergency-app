package kishore.kannan.cse.emergencyapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.ResultReceiver;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    Activity context;

    Button b;

    DatabaseReference emergencyRef;
    String Rmobile,Rname,Remail;


    private static final int SMS_REQUEST_CODE = 101;

    CardView cv1,cv2,cv3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        context=getActivity();

        return view;
    }

    public void onStart(){
        super.onStart();

        cv1=context.findViewById(R.id.cardmedical);
        cv2=context.findViewById(R.id.cardtheft);
        cv3=context.findViewById(R.id.cardfire);


        b = context.findViewById(R.id.alertMain);

        Bundle bundle = getArguments();
        if(bundle != null)
        {
            Rname = bundle.getString("name");
            Remail = bundle.getString("email");
            Rmobile = bundle.getString("mobile");
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmergencySMSToAllUsers();
            }
        });


        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,MainActivitymedical1.class);
                startActivity(i);
            }
        });

        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(context,MainActivity4.class);
                startActivity(i);
            }
        });

        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,MainActivity7.class);
                startActivity(i);
            }
        });
    }


    private void sendEmergencySMSToAllUsers() {
        // Retrieve all registered user phone numbers from Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Registered Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> phoneNumbers = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Get the phone number of each user and add it to the list
                    String phoneNumber = userSnapshot.child("Umobile").getValue(String.class);
                    if (phoneNumber != null && !phoneNumber.equals(Rmobile)) {
                        phoneNumbers.add(phoneNumber);
                    }
                }

                if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
                {
                    sendSMSToMultipleNumbers(phoneNumbers, Rname+" is in an Emergency");
                }
                else
                {
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.SEND_SMS},
                            SMS_REQUEST_CODE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void sendSMSToMultipleNumbers(ArrayList<String> phoneNumbers, String message) {
        SmsManager smsManager = SmsManager.getDefault();

        for (String phoneNumber : phoneNumbers) {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        }

        Toast.makeText(context, "Emergency Message sent", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==SMS_REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                sendEmergencySMSToAllUsers();
            }
            else
            {
                Toast.makeText(context, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}