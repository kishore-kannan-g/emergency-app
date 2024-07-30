package kishore.kannan.cse.emergencyapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Fragment1 extends Fragment {

    TextView t1;
    Button b1;

    String message;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_1, container, false);

        t1=v.findViewById(R.id.textViewfrag1);
        b1=v.findViewById(R.id.buttonfrag1);

        Bundle b =getArguments();
        String text = b.getString("text");
        t1.setText(text);
        message=t1.getText().toString();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TheftDetails td = (TheftDetails) getActivity();
                td.details(message);
            }
        });

        return v;
    }
}