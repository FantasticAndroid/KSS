package com.ak.ta.dainikbhaskar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.ak.ta.dainikbhaskar.activity.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String type = intent.getStringExtra("type");
            if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("deletion")) {
                Log.v("TAG", "NotificationDeletion");
            } else if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("action")) {
                int notificationId = intent.getIntExtra("id", 0);
                PendingIntent pendingIntent = intent.getParcelableExtra(Intent.EXTRA_INTENT);

                try {
                    pendingIntent.send();
                    NotificationManager mNotificationManager = (NotificationManager)
                            context.getSystemService(NOTIFICATION_SERVICE);
                    mNotificationManager.cancel(notificationId);
                    Intent closeIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    context.sendBroadcast(closeIntent);
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }

            } else {
                /*int count = AppPreferences.getInstance(context).getIntValue(QuickPreferences.NOTIFICATION_COUNT, 0);
                if (count > 0) {
                    --count;
                    AppPreferences.getInstance(context).setIntValue(QuickPreferences.NOTIFICATION_COUNT, count);
                }*/

                try {
                    Intent intent1 = new Intent(context, MainActivity.class);
                    intent1.putExtras(intent);
                    ////intent1.putExtra(Constants.KeyPair.IN_BACKGROUND, InitApplication.getInstance().isAppOpen());
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent1);
                } catch (ActivityNotFoundException e) {
                }
            }
        }
    }
}
