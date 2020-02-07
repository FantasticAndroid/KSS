package app.serviceconnector.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Message
import android.widget.Toast
import androidx.core.app.NotificationCompat
import app.serviceconnector.R
import lib.connector.BoundService
import lib.connector.MessageReceiver

class DemoBoundForegroundService : BoundService() {

    private val TAG = DemoBoundForegroundService::class.java.simpleName
    private val mNotificationId = 21001
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val mBuilder = NotificationCompat.Builder(this)

        mBuilder.setContentText("Text $TAG")
        mBuilder.setContentTitle("Title $TAG")
        mBuilder.setAutoCancel(false)
        mBuilder.color = resources.getColor(R.color.material_deep_teal_500)
        mBuilder.setProgress(1000, 0, true)
        mBuilder.setTicker("Ticker $TAG")
        mBuilder.setUsesChronometer(true)
        mBuilder.setSmallIcon(R.drawable.ic_launcher)

        val resultIntent = Intent()

        val resultPendingIntent = PendingIntent.getActivity(
            this,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        mBuilder.setContentIntent(resultPendingIntent)

        // Gets an instance of the NotificationManager service

        // Builds the notification and issues it.
        val notification = mBuilder.build()
        notificationManager.notify(mNotificationId, mBuilder.build())
        startForeground(mNotificationId, notification)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onMessageReceivedFromUI(message: Message) {

        when {
            message.what == MessageReceiver.SERVICE_CONNECTED -> Toast.makeText(
                applicationContext,
                "SERVICE_CONNECTED",
                Toast.LENGTH_SHORT
            ).show()
            message.what == MessageReceiver.SERVICE_DISCONNECT -> Toast.makeText(
                applicationContext,
                "SERVICE_DISCONNECT",
                Toast.LENGTH_SHORT
            ).show()
            message.what == MessageReceiver.SERVICE_STOP -> Toast.makeText(
                applicationContext,
                "SERVICE_STOP",
                Toast.LENGTH_SHORT
            ).show()
            else -> Toast.makeText(
                applicationContext,
                message.obj?.toString(),
                Toast.LENGTH_SHORT
            )
                .show()
        }
        sendMessageToUI("Message Sent from Bound Forground Service to UI")
    }

    override fun onDestroy() {
        notificationManager.cancel(mNotificationId)
        super.onDestroy()
    }
}
