package kishore.kannan.cse.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity4 extends AppCompatActivity {

    Button b1,b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        b1= (Button) findViewById(R.id.buttontheftalert);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity4.this,"Alert Message sent",Toast.LENGTH_SHORT).show();
                Intent i =new Intent(MainActivity4.this,MainActivity5.class);
                startActivity(i);
            }
        });
    }
}