package kishore.kannan.cse.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity3 extends AppCompatActivity {

    CardView cv1,cv2,cv3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        cv1=(CardView) findViewById(R.id.cardmedical);
        cv2=(CardView) findViewById(R.id.cardtheft);
        cv3=(CardView) findViewById(R.id.cardfire);

        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity3.this,MainActivity4.class);
                startActivity(i);
            }
        });

        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity3.this,MainActivity6.class);
                startActivity(i);
            }
        });

        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity3.this,MainActivity7.class);
                startActivity(i);
            }
        });
    }
}