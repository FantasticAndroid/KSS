package com.android.betterstetho

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import com.android.betterstetho.retro.ApiClient
import com.android.betterstetho.retro.ApiService
import kotlinx.android.synthetic.main.activity_demo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitActivity : CoreActivity() {
companion object{
    val TAG = RetrofitActivity::class.java.simpleName
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        runRetrofitApi()
    }

    private  fun runRetrofitApi(){

        val progressDialog = ProgressDialog.show(this,"","",false,true)
        progressDialog.show()

        val apiClient = ApiClient(Constant.BASE_URL)

        val call = apiClient.getClient().create(ApiService::class.java).getRetroResponse()

        call.enqueue(object: Callback<BaseModel>{

            override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                progressDialog.dismiss()
                Log.e(TAG,"onFailure() "+t.message)
                tv.text = t.message
            }

            override fun onResponse(call: Call<BaseModel>, response: Response<BaseModel>) {
                progressDialog.dismiss()
                Log.d(TAG,"onResponse() "+ response.body()?.toString())
                tv.text = response.body()?.toString()
            }
        })
    }
}