package kishore.kannan.cse.emergencyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class FragmentProfile extends Fragment {

    Activity context;

    Button b1,b2;

    TextView Ename, Eemail, Emobile, Epass , tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = getActivity();



       // tv = view.findViewById(R.id.textViewprofile);



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        b1 = context.findViewById(R.id.buttonProfileContacts);

        Ename = getView().findViewById(R.id.pname);
        Eemail = getView().findViewById(R.id.pemail);
        Emobile = getView().findViewById(R.id.pphone);
        b2 = getView().findViewById(R.id.logout);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent( context ,LoginActivity.class);
                startActivity(intent);
                context.finish();
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MainActivityProfileContacts.class);
                startActivity(i);
            }
        });

        Bundle bundle = getArguments();
        if(bundle != null)
        {
            String name = bundle.getString("name");
            String email = bundle.getString("email");
            String mobile = bundle.getString("mobile");
           // String pass = bundle.getString("pass");

            Ename.setText(name);
            Eemail.setText(email);
           // Epass.setText(pass);
            Emobile.setText(mobile);

        }
        else
        {
            Toast.makeText(context, "No bundle", Toast.LENGTH_SHORT).show();
        }

    }
}