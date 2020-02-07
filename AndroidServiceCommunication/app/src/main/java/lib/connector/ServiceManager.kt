package lib.connector

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.widget.Toast
import java.lang.ref.WeakReference

/**
 * Manage the whole Activity-Service Communication
 * @property activity Activity
 * @property messageReceiver MessageReceiver?
 * @property appServiceConnection WeakReference<AppServiceConnection>?
 * @property serviceIntent Intent?
 * @property senderToService Messenger?
 * @property messengerReceiver Messenger
 * @constructor
 */
class ServiceManager(
    private val activity: Activity,
    private val messageReceiver: MessageReceiver?
) : HandleMessageCallback {

    private var appServiceConnection: WeakReference<AppServiceConnection>? = null
    private var serviceIntent: Intent? = null
    private var senderToService: Messenger? = null
    private var messengerReceiver: Messenger = Messenger(ServiceHandler(this))// = Messenger(this)

    /***
     * @param className
     */
    fun startService(className: Class<*>) {
        appServiceConnection = WeakReference(AppServiceConnection())
        serviceIntent = Intent(activity, className)
        activity.startService(serviceIntent)
        activity.bindService(serviceIntent, appServiceConnection?.get()!!, Context.BIND_AUTO_CREATE)
    }

    /***
     * @param message
     */
    fun sendMessageToService(message: String) {
        try {
            senderToService?.let {
                val msg = Message()
                msg.obj = message
                msg.replyTo = messengerReceiver
                it.send(msg)
            } ?: run {
                Toast.makeText(
                    activity.applicationContext,
                    "Sender is undefined", Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: RemoteException) {
            e.printStackTrace()
            Toast.makeText(activity.applicationContext, e.message, Toast.LENGTH_LONG).show()
        }
    }

    /***
     * @param message
     */
    fun sendMessageToService(message: Message) {
        try {
            senderToService?.let {
                message.replyTo = messengerReceiver
                it.send(message)
            } ?: run {
                Toast.makeText(
                    activity.applicationContext,
                    "Sender is undefined", Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: RemoteException) {
            e.printStackTrace()
            Toast.makeText(activity.applicationContext, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun disconnectedToService() {
        try {
            sendMessageToService(Message.obtain(null, MessageReceiver.SERVICE_DISCONNECT))
            activity.unbindService(appServiceConnection?.get()!!)
            appServiceConnection?.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun stopService() {
        try {
            sendMessageToService(Message.obtain(null, MessageReceiver.SERVICE_STOP))
            activity.unbindService(appServiceConnection?.get()!!)
            activity.stopService(serviceIntent)
            appServiceConnection?.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onMessageReceivedFomHandler(message: Message) {
        when (message.what) {
            MessageReceiver.SERVICE_SENT_DATA -> {
                messageReceiver?.onMessageReceivedFromService(message.obj)
            }
            MessageReceiver.SERVICE_DISCONNECT -> {
                disconnectedToService()
            }
            MessageReceiver.SERVICE_STOP -> {
                stopService()
            }
            else -> {
                messageReceiver?.onMessageReceivedFromService(message)
            }
        }
    }

    private inner class AppServiceConnection : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            senderToService = Messenger(service)
            val msg = Message.obtain(null, MessageReceiver.SERVICE_CONNECTED)
            sendMessageToService(msg)
        }
    }
}

