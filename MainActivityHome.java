package kishore.kannan.cse.emergencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivityHome extends AppCompatActivity {

    BottomNavigationView bnv;
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction;

    String Rname, Remail, Rpass, Rmobile;

    public static final String TAG = MainActivityHome.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        bnv = (BottomNavigationView) findViewById(R.id.bottomnavigation);

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
                Log.i(TAG, "Remail is null");
            }
        }
        else
        {
            Log.i(TAG, "intent null");
        }


        FragmentHome fragmentHome = new FragmentHome();
        FragmentnearEmergency fragmentnearEmergency = new FragmentnearEmergency();
        FragmentSearch fragmentSearch = new FragmentSearch();
        FragmentProfile fragmentProfile = new FragmentProfile();

        transaction = manager.beginTransaction();
        transaction.add(R.id.containerhome,fragmentHome,"fraghome");
        transaction.commit();

        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_home:

                        Bundle b = new Bundle();
                        b.putString("name",Rname);
                        b.putString("email",Remail);
                        b.putString("pass",Rpass);
                        b.putString("mobile",Rmobile);

                        fragmentHome.setArguments(b);


                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.containerhome,fragmentHome,"fraghome");
                        transaction.commit();
                        break;

                    case R.id.nav_nearemergency:
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.containerhome,fragmentnearEmergency,"fragnearemergency");
                        transaction.commit();
                        break;

                    case R.id.nav_search:
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.containerhome,fragmentSearch,"fragsearch");
                        transaction.commit();
                        break;

                    case R.id.nav_profile:

                        Bundle bundle = new Bundle();
                        bundle.putString("name",Rname);
                        bundle.putString("email",Remail);
                        bundle.putString("pass",Rpass);
                        bundle.putString("mobile",Rmobile);

                        fragmentProfile.setArguments(bundle);

                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.containerhome,fragmentProfile,"fragprofile");
                        transaction.commit();
                        break;
                }
                return true;
            }
        });
    }
}