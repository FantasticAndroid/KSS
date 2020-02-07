package lib.connector

import android.os.Message

interface HandleMessageCallback {
    /**
     * Provide this message to its implementor
     * @param message Message
     */
    fun onMessageReceivedFomHandler(message:Message)
}