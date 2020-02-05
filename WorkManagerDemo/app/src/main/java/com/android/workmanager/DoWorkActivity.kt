package com.android.workmanager

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.workmanager.utils.Constants
import kotlinx.android.synthetic.main.activity_do_work.*

class DoWorkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_do_work)

        val imageUri = intent.extras?.getString(Constants.KEY_IMAGE_URI)

        imageUri?.let {
            imageView.setImageURI(Uri.parse(it))
        }?:run {
            val resId = intent.extras?.getInt(Constants.KEY_IMAGE_RES,R.drawable.test)
            imageView.setImageResource(resId!!)
        }
    }
}
