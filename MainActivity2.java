package kishore.kannan.cse.emergencyapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    TextView tvcontact;
    Button b1,b2;
    CheckBox c1;
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    FirebaseAuth mAuth;
    String Rname,Remail, Rpass, Rmobile;

    private SQLiteDatabase database;
    private EmergencyContactsHelper databaseHelper;

    public static final String TAG = MainActivity2.class.getSimpleName();

    static final int RESULT_PICK_CONTACT=1;
    private ArrayList<String> selectedContacts = new ArrayList<>();

    AlertDialog.Builder adb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
     /*   if (user == null){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            textView.setText(user.getEmail());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });


      */


        Intent i = getIntent();
        if(i!=null)
        {
            Rname = i.getStringExtra("Rname");
            Remail = i.getStringExtra("Remail");
            Rpass = i.getStringExtra("Rpass");
            Rmobile = i.getStringExtra("Rmobile");
            if (Remail != null) {
                Log.i(TAG, "Remail received: " + Remail);
            } else {
                Log.i(TAG, "Remail is null");
            }
        }
        else
        {
            Log.i(TAG, "intent null");
        }


        b1=(Button) findViewById(R.id.buttonfinish);
        b2=(Button) findViewById(R.id.buttoncontact);
        tvcontact=(TextView) findViewById(R.id.textViewcontact);


        //database
        databaseHelper = new EmergencyContactsHelper(this);
        database = databaseHelper.getWritableDatabase();


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity2.this,MainActivityHome.class);
                i.putExtra("Rname", Rname);
                i.putExtra("Remail", Remail);
                i.putExtra("Rpass", Rpass);
                i.putExtra("Rmobile", Rmobile);
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent,RESULT_PICK_CONTACT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_PICK_CONTACT && resultCode==RESULT_OK)
        {
            if(data!=null)
            {
                Cursor cursor = null;
                try{
                    String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER};
                    cursor = getContentResolver().query(data.getData(),projection,null,null,null);
                    if(cursor!=null && cursor.moveToFirst())
                    {
                        do{
                            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            String phoneNum =  cursor.getString(phoneIndex);
                            int nameIndex =  cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                            String phoneName = cursor.getString(nameIndex);

                            String contactsinfo = phoneName + " - " + phoneNum;
                            selectedContacts.add(contactsinfo);

                            //INSERT INTO CONTACTS TABLE
                            insertContact(phoneName,phoneNum);


                        }while(cursor.moveToNext());
                        updateTextView();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateTextView()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(String str: selectedContacts)
        {
            stringBuilder.append(str).append("\n");
        }
        tvcontact.setText(stringBuilder.toString());
    }

    public void insertContact(String name, String num)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EmergencyContactsContract.ContactsInner.CONTACT_NAME,name);
        contentValues.put(EmergencyContactsContract.ContactsInner.CONTACT_NUMBER,num);
        long rowId = database.insert(EmergencyContactsContract.ContactsInner.TABLE_NAME,null,contentValues);
        Log.i(TAG, "Item is inserted at row ID = "+rowId);
    }

    public void displayContactTable()
    {
        String[] projection = {
                EmergencyContactsContract.ContactsInner.ID,
                EmergencyContactsContract.ContactsInner.CONTACT_NAME,
                EmergencyContactsContract.ContactsInner.CONTACT_NUMBER
        };

        Cursor cursor = database.query(EmergencyContactsContract.ContactsInner.TABLE_NAME,projection,null,null,null,null,null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String str = "";
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    str += "\t" + cursor.getString(i);
                }
                str += "\n";
                Log.i(TAG, str);
            } while (cursor.moveToNext());

            cursor.close();
        }

    }
}