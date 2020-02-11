package com.android.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.ndk.AppSecret
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv.append(AppSecret.getKeyForEncryptionJNI())
        tv.append("\n")
        tv.append(AppSecret.getKeyForDecryptionJNI())
    }

    /*companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }*/
}
