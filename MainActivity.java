package kishore.kannan.cse.emergencyapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar2;

    String Rname, Remail, Rpass, Rmobile;


    public static final String TAG = MainActivity.class.getSimpleName();


  /*  @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();

        }
    }     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        progressBar2 = findViewById(R.id.progressBar2);
        mFullName = findViewById(R.id.fullname);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mLoginBtn = findViewById(R.id.createText);


        // Check if user is already authenticated
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, navigate to MainActivity2

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");

            reference.child(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {
                            DataSnapshot snap = task.getResult();
                            Rname = String.valueOf(snap.child("Uname").getValue());
                            Remail = String.valueOf(snap.child("Uemail").getValue());
                            Rpass = String.valueOf(snap.child("Unpassword").getValue());
                            Rmobile = String.valueOf(snap.child("Umobile").getValue());

                            Log.i(TAG, "Name = "+Rname);

                            Intent intent = new Intent(MainActivity.this,MainActivityHome.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("Rname", Rname);
                            intent.putExtra("Remail", Remail);
                            intent.putExtra("Rpass", Rpass);
                            intent.putExtra("Rmobile", Rmobile);
                            startActivity(intent);


                        }
                    }
                }
            });


          /*  Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
            finish();

           */
        }



        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });



        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Uname = mFullName.getText().toString().trim();
                // String Uemail = mEmail.getText().toString();
                // String Upass = mPassword.getText().toString();
                String Umobile = mPhone.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }

                if(password.length()< 6){
                    mPassword.setError("password must contain atleast six characters");
                    return;
                }

                progressBar2.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar2.setVisibility(View.GONE);
                                if (task.isSuccessful()) {

                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                    //User register details adding
                                    RegisterUserDetails userProfile = new RegisterUserDetails(Uname, email, password, Umobile);

                                    //User Reference
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");

                                    reference.child(firebaseUser.getUid()).setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                           // progressBar2.setVisibility(View.GONE);

                                            if(task.isSuccessful())
                                            {


                                                reference.child(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            if(task.getResult().exists())
                                                            {
                                                                DataSnapshot snap = task.getResult();
                                                                Rname = String.valueOf(snap.child("Uname").getValue());
                                                                Remail = String.valueOf(snap.child("Uemail").getValue());
                                                                Rpass = String.valueOf(snap.child("Unpassword").getValue());
                                                                Rmobile = String.valueOf(snap.child("Umobile").getValue());

                                                                Toast.makeText(MainActivity.this, "Account created for "+Rname,Toast.LENGTH_SHORT).show();


                                                                Log.i(TAG, "Name = "+Rname);

                                                                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                intent.putExtra("Rname", Rname);
                                                                intent.putExtra("Remail", Remail);
                                                                intent.putExtra("Rpass", Rpass);
                                                                intent.putExtra("Rmobile", Rmobile);
                                                                startActivity(intent);


                                                            }
                                                        }
                                                    }
                                                });



                                              //  Toast.makeText(MainActivity.this, "Account created for "+Rname,Toast.LENGTH_SHORT).show();

                                                // Toast.makeText(MainActivity.this, ,Toast.LENGTH_SHORT).show();

                                               // finish();

                                            }
                                            else
                                            {
                                                Toast.makeText(MainActivity.this, "Profile not created",Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });




                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(MainActivity.this, "Authentication failed or account might exists",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });



    }


    private void readUserFirebase()
    {

    }

}