package lib.connector

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.widget.Toast

/**
 * Service bounded with Activity to send message between activity-service
 * @property receivingMessenger Messenger
 * @property senderMessenger Messenger?
 * @property isServiceBounded Boolean
 */
abstract class BoundService:Service(), HandleMessageCallback {

    // Target we publish for clients to send messages to IncomingHandler.
    private lateinit var receivingMessenger : Messenger

    private var senderMessenger: Messenger?=null

    protected abstract fun onMessageReceivedFromUI(message: Message)

    protected var isServiceBounded = false

    override fun onCreate() {
        super.onCreate()
        receivingMessenger = Messenger(ServiceHandler(this))
    }

    override fun onMessageReceivedFomHandler(message: Message) {
        if (message.what == MessageReceiver.SERVICE_CONNECTED) {
            senderMessenger = message.replyTo
        }
        onMessageReceivedFromUI(message)
    }

    /***
     * @param message
     */
    protected fun sendMessageToUI(message: String) {
        try {
            senderMessenger?.send(Message.obtain(null, MessageReceiver.SERVICE_SENT_DATA, message))
        } catch (e: RemoteException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    /***
     * @param message
     */
    protected fun sendMessageToUI(message: Message) {
        try {
            senderMessenger?.send(message)
        } catch (e: RemoteException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        isServiceBounded = true
        Toast.makeText(application, "onBind", Toast.LENGTH_SHORT).show()
        return receivingMessenger.binder
    }

    override fun onUnbind(intent: Intent): Boolean {
        Toast.makeText(application, "onUnbind", Toast.LENGTH_SHORT).show()
        isServiceBounded = false
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Toast.makeText(application, "onDestroy", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }
}