package app.serviceconnector.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Message;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import app.serviceconnector.R;
import lib.serviceconnector.BoundService;

import static lib.serviceconnector.ServiceManager.MessageReceiver.SERVICE_CONNECTED;
import static lib.serviceconnector.ServiceManager.MessageReceiver.SERVICE_DISCONNECT;
import static lib.serviceconnector.ServiceManager.MessageReceiver.SERVICE_STOP;

public final class DemoBoundForgroundService extends BoundService {

    private final String TAG = DemoBoundForgroundService.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentText("Text " + TAG);
        mBuilder.setContentTitle("Title " + TAG);
        mBuilder.setAutoCancel(false);
        mBuilder.setColor(getResources().getColor(R.color.material_deep_teal_500));
        mBuilder.setProgress(1000, 0, true);
        mBuilder.setTicker("Ticker " + TAG);
        mBuilder.setUsesChronometer(true);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);

        Intent resultIntent = new Intent();

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = 21001;

// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        Notification notification = mBuilder.build();
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        startForeground(mNotificationId, notification);
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
        sendMessageToUI("Message Sent from Bound Forground Service to UI");
    }
}
