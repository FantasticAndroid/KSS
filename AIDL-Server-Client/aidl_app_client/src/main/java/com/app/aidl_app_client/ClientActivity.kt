package com.app.aidl_app_client

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.aidl_lib_server.IMyAidlInterface
import com.app.aidl_lib_server.IResponseCallback
import com.app.aidl_lib_server.MyRect
import kotlinx.android.synthetic.main.activity_client.*

class ClientActivity : AppCompatActivity() {

    companion object {
        private val TAG = ClientActivity::class.java.simpleName
    }

    private var mIMyAidlInterface: IMyAidlInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)
        // Set up the login form.

        //val mEmailSignInButton = findViewById<View>(R.id.btn_add) as Button
        addBtn.setOnClickListener { add() }
    }

    override fun onStart() {
        super.onStart()
        val it = Intent()
        it.action = "com.app.aidl_lib_server.AdditionService"
        it.setPackage("com.app.aidl_lib_server")
        bindService(it, mConnection, Service.BIND_AUTO_CREATE)
    }

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mIMyAidlInterface = IMyAidlInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    private fun add() {
        try {
            // Store values at the time of the login attempt.
            val n1 = Integer.parseInt(num1Et.text.toString())
            val n2 = Integer.parseInt(num2Et.text.toString())
            val result = mIMyAidlInterface!!.add(n1, n2)

            Log.d(TAG, "Add Result " + result)

            //////Toast.makeText(ClientActivity.this, "RESULT:" + result, Toast.LENGTH_LONG).show();

            val myRect = mIMyAidlInterface!!.getNewRect(n1, n2, n1, n2)

            Log.d(TAG, "MyRect Normal " + myRect.toString())

            mIMyAidlInterface!!.getNewRectangle(n1, n2, n1, n2, object : IResponseCallback.Stub() {
                @Throws(RemoteException::class)
                override fun newRect(myRect: MyRect) {
                    Log.d(TAG, "MyRect Interface " + myRect.toString())
                    Toast.makeText(applicationContext, myRect.toString() + "", Toast.LENGTH_LONG)
                        .show()
                }
            })
        } catch (e: RemoteException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        try {
            unbindService(mConnection)
        } finally {
            super.onDestroy()
        }
    }
}

