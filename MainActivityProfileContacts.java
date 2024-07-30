package kishore.kannan.cse.emergencyapp;

import static kishore.kannan.cse.emergencyapp.MainActivity2.RESULT_PICK_CONTACT;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivityProfileContacts extends AppCompatActivity {

    Button b1 ,b2, b3;
    ListView lv1;


    SQLiteDatabase database;
    EmergencyContactsHelper databaseHelper;


    private ArrayList<String> contacts = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private String selectedPhoneNumber;

    public static final String TAG = MainActivitymedical2.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile_contacts);

        b1 = (Button) findViewById(R.id.buttonViewContacts);
        b2 = (Button) findViewById(R.id.buttonAddContacts);
        b3 = (Button) findViewById(R.id.buttonDeleteContacts);
        lv1 = (ListView) findViewById(R.id.listView1);

        databaseHelper = new EmergencyContactsHelper(this);
        database = databaseHelper.getWritableDatabase();


        adapter = new ArrayAdapter<>(this, R.layout.activity_main_list_view, R.id.textViewlist, contacts);

        lv1.setAdapter(adapter);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayContactTable();
            }
        });


        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item
                String selectedItem = contacts.get(position);

                // Parse the contact information
                String[] contactInfo = selectedItem.split("    "); // Adjust this based on your actual data structure

                // Check if there are at least two parts (name and number)
                if (contactInfo.length >= 2) {
                    selectedPhoneNumber = contactInfo[1];

                    // Perform further operations with the phone number
                    //  Toast.makeText(MainActivityProfileContacts.this, "Selected Phone Number: " + phoneNumber, Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the case where the contact information is not in the expected format
                    //  Toast.makeText(MainActivityProfileContacts.this, "Invalid contact information", Toast.LENGTH_SHORT).show();
                }
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent,RESULT_PICK_CONTACT);

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPhoneNumber != null) {
                    deleteContactNumber(selectedPhoneNumber);
                }
                else
                {
                    Toast.makeText(MainActivityProfileContacts.this, "Please select a contact to delete", Toast.LENGTH_SHORT).show();
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
            contacts.clear();
            do {
                String str="";
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    str += cursor.getString(i)+"    ";
                }

                str += "\n";
                Log.i(TAG, str);
                contacts.add(str);
            } while (cursor.moveToNext());

            cursor.close();

            adapter.notifyDataSetChanged();
        }

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
                            contacts.add(contactsinfo);

                            //INSERT INTO CONTACTS TABLE
                            addContactNumber(phoneName,phoneNum);


                        }while(cursor.moveToNext());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }



    public void addContactNumber(String name, String num)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EmergencyContactsContract.ContactsInner.CONTACT_NAME, name);
        contentValues.put(EmergencyContactsContract.ContactsInner.CONTACT_NUMBER, num);

        long rowId = database.insert(EmergencyContactsContract.ContactsInner.TABLE_NAME,null,contentValues);
        Log.i(TAG, "Item is inserted at row ID = "+rowId);

        Toast.makeText(this, "Emergency Contacts Updated", Toast.LENGTH_SHORT).show();

    }


    public void deleteContactNumber(String phoneNumber)
    {
        String selection = EmergencyContactsContract.ContactsInner.CONTACT_NUMBER + " = ? ";
        String[] selectionArgs = { phoneNumber };

        long rowDeleted = database.delete(EmergencyContactsContract.ContactsInner.TABLE_NAME, selection, selectionArgs);
        Log.i(TAG, "Rows deleted = "+rowDeleted);

        Toast.makeText(this, "Emergency Contacts Updated", Toast.LENGTH_SHORT).show();
    }


}