package com.android.betterstetho

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import com.android.betterstetho.volley.ApiClient
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.activity_demo.*

class VolleyActivity : CoreActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        runVolleyApi()
    }

    private fun runVolleyApi() {
        val progressDialog = ProgressDialog.show(this, "", "", false, true)
        progressDialog.show()

        val stringRequest =
            StringRequest(Request.Method.GET, Constant.BASE_URL + Constant.METHOD_URL,
                Response.Listener<String> { response ->
                    progressDialog.dismiss()
                    Log.d(RetrofitActivity.TAG, "onResponse() ".plus(response))
                    tv.text = response
                },
                Response.ErrorListener { error ->
                    progressDialog.dismiss()
                    Log.e(RetrofitActivity.TAG, "onFailure() " + error.message)
                    tv.text = error.message
                })

        stringRequest.setShouldCache(false)
        ApiClient(coreApp).addRequestInQueue(stringRequest)
    }

}