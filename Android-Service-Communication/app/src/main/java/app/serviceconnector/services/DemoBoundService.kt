package app.serviceconnector.services

import android.os.Message
import android.widget.Toast
import lib.connector.BoundService
import lib.connector.MessageReceiver

class DemoBoundService : BoundService() {

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
        sendMessageToUI("Message Sent from BoundService to UI")
    }
}
