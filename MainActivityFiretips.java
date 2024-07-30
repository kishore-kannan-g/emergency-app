package kishore.kannan.cse.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivityFiretips extends AppCompatActivity {

    Button b1,b2,b3,b4;
    FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_firetips);
        b1=(Button) findViewById(R.id.buttonresidential);
        b2=(Button) findViewById(R.id.buttonwork);
        b3=(Button) findViewById(R.id.buttonhighbuild);
        b4=(Button) findViewById(R.id.buttonedu);

        Fragmentfiretip1 fragmentfiretip1 = new Fragmentfiretip1();
        FragmentFiretip2 fragmentfiretip2 = new FragmentFiretip2();
        FragmentFiretip3 fragmentfiretip3 = new FragmentFiretip3();
        FragmentFiretip4 fragmentfiretip4 = new FragmentFiretip4();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.containertips,fragmentfiretip1,"fragtip1");
                transaction.commit();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.containertips,fragmentfiretip2,"fragtip2");
                transaction.commit();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.containertips,fragmentfiretip3,"fragtip3");
                transaction.commit();
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.containertips,fragmentfiretip4,"fragtip4");
                transaction.commit();
            }
        });
    }
}