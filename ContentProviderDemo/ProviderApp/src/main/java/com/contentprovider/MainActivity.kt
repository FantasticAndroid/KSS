package com.contentprovider

import android.app.Activity
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(), View.OnClickListener {
    private var mEtInsertValue: EditText? = null
    private var mEtUpdateId: EditText? = null
    private var mEtUpdateValue: EditText? = null
    private var mEtDeleteId: EditText? = null
    private var mTvGetData: TextView? = null
    private var mButtonInsert: Button? = null
    private var mButtonUpdate: Button? = null
    private var mButtonDelete: Button? = null
    private var mButtonGetData: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViews()
    }

    private fun initializeViews() {
        mEtInsertValue = findViewById(R.id.et_insert_value)
        mEtUpdateId = findViewById(R.id.et_update_id)
        mEtUpdateValue = findViewById(R.id.et_update_value)
        mEtDeleteId = findViewById(R.id.et_delete_id)

        mTvGetData = findViewById(R.id.tv_get_data)

        mButtonInsert = findViewById(R.id.button_insert)
        mButtonUpdate = findViewById(R.id.button_update)
        mButtonDelete = findViewById(R.id.button_delete)
        mButtonGetData = findViewById(R.id.button_get)

        mButtonInsert!!.setOnClickListener(this)
        mButtonUpdate!!.setOnClickListener(this)
        mButtonDelete!!.setOnClickListener(this)
        mButtonGetData!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_insert -> {
                val insertName = et_insert_value.text.toString()
                //calling method to insert new value into database
                try {
                    if (insertNewData(insertName)) {
                        showToast("Successfully added new value.")
                    } else {
                        showToast("Value not inserted.")
                    }
                } catch (e: IllegalArgumentException) {
                    showToast(e.message)
                }
            }

            R.id.button_update -> {
                val updateId = mEtUpdateId!!.text.toString()
                val updateName = mEtUpdateValue!!.text.toString()
                //calling method updateData()
                try {
                    if (updateData(updateId, updateName)) {
                        showToast("Successfully updated the value.")
                    } else {
                        showToast("Entered Id does not exist in database.")
                    }
                } catch (e: IllegalArgumentException) {
                    showToast(e.message)
                }

            }

            R.id.button_delete -> {
                val deleteId = mEtDeleteId!!.text.toString()
                //calling method to insert new value into database
                try {
                    if (deleteData(deleteId)) {
                        showToast("Successfully deleted.")
                    } else {
                        showToast("Entered id does not exist in database.")
                    }
                } catch (e: IllegalArgumentException) {
                    showToast(e.message)
                }
            }

            R.id.button_get -> {
                val records = getAllRecords()
                if (records != null && records != "") {
                    mTvGetData!!.text = records
                } else {
                    mTvGetData!!.text = ""
                    showToast("No record available.")
                }
            }
        }
    }

    private fun insertNewData(name: String?): Boolean {

        return if (!name.isNullOrEmpty() && !name.isNullOrBlank()) {
            val values = ContentValues()
            values.put(MainProvider.name, name)
            val uri = contentResolver.insert(MainProvider.CONTENT_URI, values)
            uri != null
        } else {
            Toast.makeText(applicationContext, "Name should be valid value.", Toast.LENGTH_LONG)
                .show()
            false
        }
    }

    /*
    method to update the name of a particular id
    @param id
    @param name
     */
    private fun updateData(id: String?, name: String?): Boolean {

        return if ((!id.isNullOrEmpty() && !id.isNullOrBlank()) || (!name.isNullOrEmpty() && !name.isNullOrBlank())) {
            val values = ContentValues()
            values.put(MainProvider.name, name)
            val arr = arrayOf(id)
            val count = contentResolver.update(MainProvider.CONTENT_URI, values, "id=?", arr)
            return count != 0
        } else {
            Toast.makeText(applicationContext, "Name should be valid value.", Toast.LENGTH_LONG)
                .show()
            false
        }
    }

    /*
    method to delete particular row
    @param id
     */
    private fun deleteData(id: String?): Boolean {

        return if (!id.isNullOrEmpty() && !id.isNullOrBlank()) {
            val count = contentResolver.delete(MainProvider.CONTENT_URI, "id = " + id, null)
            return count != 0
        } else {
            Toast.makeText(applicationContext, "Name should be valid value.", Toast.LENGTH_LONG)
                .show()
            false
        }
    }

    private fun getAllRecords(): String? {
        /*val arr = arrayOf("id", "name")
        val selection = arrayOf("0", "iiiiii1111")
        val cursor = contentResolver.query(
            MainProvider.CONTENT_URI,
            arr,
            "id>? and name!=?",
            selection,
            "id DESC"
        )*/

        val cursor = contentResolver.query(MainProvider.CONTENT_URI, null, null, null, "id DESC")

        cursor?.let {

            it.moveToFirst()
            val res = StringBuilder()
            while (!it.isAfterLast) {
                res.append(
                    "\n" + it.getString(it.getColumnIndex("id")) + "-" + it.getString(
                        it.getColumnIndex("name")
                    )
                )
                it.moveToNext()
            }
            it.close()
            return "" + res
        } ?: run { return null }
    }

    /*
    method to show the toast
    @param message
     */
    private fun showToast(message: String?) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}
