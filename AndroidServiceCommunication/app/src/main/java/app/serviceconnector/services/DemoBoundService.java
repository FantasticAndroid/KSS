package app.serviceconnector.services;

import android.content.Intent;
import android.os.Message;
import android.widget.Toast;

import static lib.serviceconnector.ServiceManager.MessageReceiver.*;

import lib.serviceconnector.BoundService;

public final class DemoBoundService extends BoundService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onMessageReceivedFromUI(Message message) {

        if (message.what == SERVICE_CONNECTED) {
            Toast.makeText(getApplicationContext(), "SERVICE_CONNECTED", Toast.LENGTH_SHORT).show();
        } else if (message.what == SERVICE_DISCONNECT) {
            Toast.makeText(getApplicationContext(), "SERVICE_DISCONNECT", Toast.LENGTH_SHORT).show();
        } else if (message.what == SERVICE_STOP) {
            Toast.makeText(getApplicationContext(), "SERVICE_STOP", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), message.obj + "", Toast.LENGTH_SHORT).show();
        }
        sendMessageToUI("Message Sent from BoundService to UI");
    }
}
