package kishore.kannan.cse.emergencyapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

public class Fragment2 extends Fragment {
    CheckBox c1,c2;
    Button b1,b2;

    FragmentManager manager;
    FragmentTransaction transaction;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_2, container, false);

        c1=view.findViewById(R.id.checkBox1);
        c2=view.findViewById(R.id.checkBox2);
        b1=view.findViewById(R.id.buttonfrag2);
        b2=view.findViewById(R.id.button2frag2);

        Fragment3 fragment3 = new Fragment3();
        FragmentPims fragmentPims=new FragmentPims();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c1.isChecked()) {
                    manager = getParentFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.add(R.id.container2, fragment3, "frag3");
                    transaction.commit();
                }
                if(c2.isChecked())
                {
                    manager = getParentFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.add(R.id.container2, fragmentPims, "fragpims");
                    transaction.commit();
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Fragment3 fragment3 = (Fragment3) manager.findFragmentByTag("frag3");
                if(fragment3!=null)
                {
                    transaction=manager.beginTransaction();
                    transaction.remove(fragment3);
                    transaction.commit();
                }   */

                Fragment2 fragment2 = new Fragment2();

                transaction=manager.beginTransaction();
                transaction.replace(R.id.container2,fragment2,"frag2");
                transaction.commit();
            }
        });

        return view;
    }
}