package kishore.kannan.cse.emergencyapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity5 extends AppCompatActivity implements TheftDetails{

    EditText e1;
    Button b1,b2,b3;
    ImageView iv;



    private final int GALLERY_REQ_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        e1=(EditText) findViewById(R.id.editTexttheftmessage);
        b1=(Button) findViewById(R.id.buttontheftmessage);
        b2=(Button) findViewById(R.id.buttontheftimage);
        b3=(Button) findViewById(R.id.buttontheftimage2);
        iv=(ImageView) findViewById(R.id.theftimage);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = e1.getText().toString();
                Fragment1 fragment1=new Fragment1();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container1,fragment1,"frag1");
                transaction.commit();
                Bundle b =new Bundle();
                b.putString("text",text);
                fragment1.setArguments(b);
                e1.setText("");
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent igallery = new Intent(Intent.ACTION_PICK);
                igallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(igallery, GALLERY_REQ_CODE);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iv.getDrawable()!=null)
                {
                    Toast.makeText(MainActivity5.this,"IMAGE SENT",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity5.this,"PLEASE UPLOAD IMAGE",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){

            if(requestCode==GALLERY_REQ_CODE)
            {
                iv.setImageURI(data.getData());
            }
        }

    }
    @Override
    public void details(String message)
    {
        e1.setText(message);
    }

}