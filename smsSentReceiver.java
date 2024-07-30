package kishore.kannan.cse.emergencyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class smsSentReceiver extends BroadcastReceiver {

    private final MainActivitymedical2 mainActivity;

    public smsSentReceiver(MainActivitymedical2 mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mainActivity != null) {
            if (getResultCode() == MainActivity.RESULT_OK) {
                mainActivity.incrementMessagesSentCount();

                if (mainActivity.allMessagesSent()) {
                    mainActivity.showNotification("SMS sent successfully to all numbers");
                }
            } else {
                Toast.makeText(context, "SMS sending failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
