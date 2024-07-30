package kishore.kannan.cse.emergencyapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.telephony.SmsManager;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class IntentServiceSMS extends IntentService {

    public static final String ACTION_SEND_SMS = "action.SEND_SMS";
    public static final String EXTRA_PHONE_NUMBERS = "extra.PHONE_NUMBERS";
    public static final String EXTRA_MESSAGE = "extra.MESSAGE";
    public static final String EXTRA_RESULT_RECEIVER = "extra.RESULT_RECEIVER";

    public IntentServiceSMS() {
        super("IntentServiceSMS");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEND_SMS.equals(action)) {
                ArrayList<String> phoneNumbers = intent.getStringArrayListExtra(EXTRA_PHONE_NUMBERS);
                String message = intent.getStringExtra(EXTRA_MESSAGE);
                ResultReceiver resultReceiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);

                sendSMSToMultipleNumbers(phoneNumbers, message, resultReceiver);
            }
        }
    }


    private void sendSMSToMultipleNumbers(ArrayList<String> phoneNumbers, String message, ResultReceiver resultReceiver) {
        SmsManager smsManager = SmsManager.getDefault();
        int messagesSentCount = 0;

        for (String phoneNumber : phoneNumbers) {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            messagesSentCount++;
        }

        // Notify the result using the ResultReceiver
        if (resultReceiver != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("messagesSentCount", messagesSentCount);
            resultReceiver.send(0, bundle);
        }
    }
}