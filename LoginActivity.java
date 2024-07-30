package kishore.kannan.cse.emergencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button buttonLogin;
    TextView mLoginBtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    String Rname, Remail, Rpass, Rmobile;

    public static final String TAG = MainActivity.class.getSimpleName();


 /*   @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

        //    Toast.makeText(this, Rname, Toast.LENGTH_SHORT).show();

            Intent intent2 = new Intent(getApplicationContext(),MainActivity2.class);
           // intent2.putExtra("Rname", Rname);
            startActivity(intent2);
            finish();

        }
    }

  */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        mEmail = findViewById(R.id.Email2);
        mPassword = findViewById(R.id.Password2);
        buttonLogin = findViewById(R.id.btn_Login);
        mLoginBtn = findViewById(R.id.registernow);

        Intent i = getIntent();
        if(i!=null)
        {
            Rname = i.getStringExtra("Rname");
            Remail = i.getStringExtra("Remail");
            Rpass = i.getStringExtra("Rpass");
            Rmobile = i.getStringExtra("Rmobile");

            if (Rname != null) {
                Log.i(TAG, "Rname received: " + Rname);
            } else {
                Log.i(TAG, "Rname is null");
            }
        }
        else
        {
            Log.i(TAG, "intent null");
        }


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                progressBar.setVisibility(View.VISIBLE);


                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();

                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");

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

                                                    Log.i(TAG, "Name = "+Rname);

                                                    Intent intent = new Intent(LoginActivity.this,MainActivityHome.class);
                                                    intent.putExtra("Rname", Rname);
                                                    intent.putExtra("Remail", Remail);
                                                    intent.putExtra("Rpass", Rpass);
                                                    intent.putExtra("Rmobile", Rmobile);
                                                    startActivity(intent);


                                                }
                                            }
                                        }
                                    });


                               /*     Intent intent2 = new Intent(LoginActivity.this,MainActivity2.class);
                                    intent2.putExtra("Rname", Rname);
                                    intent2.putExtra("Remail", Remail);
                                    intent2.putExtra("Rpass", Rpass);
                                    intent2.putExtra("Rmobile", Rmobile);
                                    startActivity(intent2);

                                */
                                 //   finish();


                                } else {

                                    Toast.makeText(LoginActivity.this, "Authentication failed register an account",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });
    }
}