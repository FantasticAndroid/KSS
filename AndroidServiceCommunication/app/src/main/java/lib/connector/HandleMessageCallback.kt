package lib.connector

import android.os.Message

interface HandleMessageCallback {

    fun onMessageReceivedFomHandler(message:Message)
}