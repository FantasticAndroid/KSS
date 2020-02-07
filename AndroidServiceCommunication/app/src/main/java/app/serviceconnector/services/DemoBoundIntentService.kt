package app.serviceconnector.services

import android.content.Intent
import android.os.Message
import android.util.Log
import android.widget.Toast
import lib.connector.BoundIntentService
import lib.connector.MessageReceiver

/**
 * Creates an IntentService.  Invoked by your subclass's constructor.
 */
class DemoBoundIntentService : BoundIntentService(DemoBoundIntentService::class.java.name) {

    private var run = true

    override fun onHandleIntent(intent: Intent?) {
        var i = 0
        try {
            while (run) {
                i++
                Log.d("onHandleIntent", "i: $i")
                Thread.sleep(2000)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onMessageReceivedFromUI(message: Message) {

        when {
            message.what == MessageReceiver.SERVICE_CONNECTED -> Toast.makeText(
                applicationContext,
                "SERVICE_CONNECTED",
                Toast.LENGTH_SHORT
            ).show()
            message.what == MessageReceiver.SERVICE_STOP -> {
                Toast.makeText(applicationContext, "SERVICE_STOP", Toast.LENGTH_SHORT).show()
                run = false
            }
            else -> Toast.makeText(
                applicationContext,
                message.obj?.toString(),
                Toast.LENGTH_SHORT
            )
                .show()
        }
        sendMessageToUI("Message Sent from Bound IntentService to UI")
    }
}
