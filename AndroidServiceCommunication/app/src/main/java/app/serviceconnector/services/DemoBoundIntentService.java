package app.serviceconnector.services;

import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import lib.serviceconnector.BoundIntentService;

import static lib.serviceconnector.ServiceManager.MessageReceiver.*;

public final class DemoBoundIntentService extends BoundIntentService {

    private boolean run = true;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DemoBoundIntentService() {
        super(DemoBoundIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int i = 0;
        try {
            while (run) {
                i++;
                Log.d("onHandleIntent", "i: " + i);
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMessageReceivedFromUI(Message message) {

        if (message.what == SERVICE_CONNECTED) {
            Toast.makeText(getApplicationContext(), "SERVICE_CONNECTED", Toast.LENGTH_SHORT).show();
        } else if (message.what == SERVICE_STOP) {
            Toast.makeText(getApplicationContext(), "SERVICE_STOP", Toast.LENGTH_SHORT).show();
            run = false;
        } else {
            Toast.makeText(getApplicationContext(), message.obj + "", Toast.LENGTH_SHORT).show();
        }
        sendMessageToUI("Message Sent from Bound IntentService to UI");
    }
}
