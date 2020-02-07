package lib.connector

import android.os.Message

interface MessageReceiver {

    companion object {
        const val SERVICE_CONNECTED = 1
        const val SERVICE_DISCONNECT = 2
        const val SERVICE_STOP = 3
        const val SERVICE_SENT_DATA = 10
    }

    fun onMessageReceivedFromService(message: Message?)

    fun onMessageReceivedFromService(anyObject: Any?)
}