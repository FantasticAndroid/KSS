package lib.connector

import android.os.Handler
import android.os.Message

/**
 * A Custom Handler extends Android Handler
 * @property handleMessageCallback HandleMessageCallback
 * @constructor
 */
class ServiceHandler(private val handleMessageCallback: HandleMessageCallback) : Handler() {

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        handleMessageCallback.onMessageReceivedFomHandler(msg)
    }
}