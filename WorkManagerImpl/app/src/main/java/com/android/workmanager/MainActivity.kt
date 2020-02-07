//package com.android.workmanager
//
//import android.app.Activity
//import android.content.Intent
//import android.net.Uri
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.provider.MediaStore
//import android.util.Log
//import com.android.workmanager.utils.Constants
//import kotlinx.android.synthetic.main.activity_main.*
//import java.lang.Exception
//
//class MainActivity : AppCompatActivity() {
//
//    companion object {
//        private val TAG = MainActivity::class.java.simpleName
//        private const val REQUEST_CODE_IMAGE = 100
//        private const val REQUEST_CODE_PERMISSIONS = 101
//
//        private const val KEY_PERMISSIONS_REQUEST_COUNT = "KEY_PERMISSIONS_REQUEST_COUNT"
//        private const val MAX_NUMBER_REQUEST_PERMISSIONS = 2
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        // Create request to get image from filesystem when button clicked
//        selectGalleryBtn.setOnClickListener {
//            val chooseIntent = Intent(
//                    Intent.ACTION_PICK,
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(chooseIntent, REQUEST_CODE_IMAGE)
//        }
//
//        selectResBtn.setOnClickListener {
//            val filterIntent = Intent(this, DoWorkActivity::class.java)
//            filterIntent.putExtra(Constants.KEY_INPUT_IMAGE_RES, R.drawable.test)
//            startActivity(filterIntent)
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//                REQUEST_CODE_IMAGE ->
//                    readImageWithResult(data)
//            }
//        }
//    }
//
//    private fun readImageWithResult(intent: Intent?) {
//        try {
//            var imgUri: Uri? = null
//
//            intent?.clipData?.let {
//                imgUri = it.getItemAt(0).uri
//            } ?: run { imgUri = intent?.data }
//
//            imgUri?.let {
//                val filterIntent = Intent(this, DoWorkActivity::class.java)
//                filterIntent.putExtra(Constants.KEY_OUTPUT_IMAGE_URI, imgUri.toString())
//                startActivity(filterIntent)
//            }
//        }catch (e:Exception){
//            Log.e(TAG,"readImageWithResult() "+e.message)
//        }
//    }
//}
