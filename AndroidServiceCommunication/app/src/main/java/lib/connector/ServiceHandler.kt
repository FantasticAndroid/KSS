package lib.connector

import android.os.Handler
import android.os.Message

class ServiceHandler(private val handleMessageCallback: HandleMessageCallback) : Handler() {

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        handleMessageCallback.onMessageReceivedFomHandler(msg)
    }
}