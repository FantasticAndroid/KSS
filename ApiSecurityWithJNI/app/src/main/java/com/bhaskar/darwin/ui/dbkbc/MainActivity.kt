package com.bhaskar.darwin.ui.dbkbc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bhaskar.util.DddGameAppSecret
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        /*sample_text.text = KbcApiConstants.getApiKeyForEncryptionJNI()
        sample_text.append("\n" + KbcApiConstants.getApiKeyForDecryptionJNI())*/

        sample_text.text = DddGameAppSecret.getApiKeyForEncryptionJNI()
        sample_text.append("\n" + DddGameAppSecret.getApiKeyForDecryptionJNI())
    }

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
