package app.serviceconnector.activities

import android.app.Activity
import android.os.Bundle
import android.os.Message
import android.view.View
import android.widget.Toast

import app.serviceconnector.R
import kotlinx.android.synthetic.main.activity_demo.*
import lib.connector.MessageReceiver
import lib.connector.ServiceManager

class ServiceLauncherActivity : Activity(), View.OnClickListener {

    private var serviceManager: ServiceManager? = null
    private var service: Class<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        serviceManager = ServiceManager(this, object : MessageReceiver {

            override fun onMessageReceivedFromService(anyObject: Any?) {
                anyObject?.let {
                    Toast.makeText(applicationContext, it.toString() + "", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onMessageReceivedFromService(message: Message?) {
                message?.let {
                    Toast.makeText(applicationContext, message.toString() + "", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        sendBtn.setOnClickListener(this)
        stopBtn.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        val bundle = intent.extras
        if (bundle != null) {
            val serviceName = bundle.getString("SERVICE")
            try {
                service = Class.forName(serviceName!!)
                serviceManager?.startService(service!!)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }

        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.sendBtn -> {
                serviceManager?.sendMessageToService("Message sent from UI to " + service!!.simpleName)
            }
            R.id.stopBtn -> {
                serviceManager?.stopService()
            }
        }
    }

    override fun onDestroy() {
        serviceManager?.disconnectedToService()
        super.onDestroy()
    }
}
