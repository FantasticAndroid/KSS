package com.app.databinding.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.databinding.R
import com.app.databinding.adapter.LocationItemAdapter
import com.app.databinding.databinding.MainActivityBinding
import com.app.databinding.listener.DataBindingListener
import com.app.databinding.model.StateKt
import com.app.databinding.model.StateListKtResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val binding = DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.activity_main)
            val listener = DataBindingListener(this)
            binding.listener = listener
            initRecyclerView()
        } catch (e: Exception) {
            Log.e(TAG, "onCreate: " + e.message)
        }
    }

    private fun initRecyclerView() {
        try {
            rvLocation.layoutManager = LinearLayoutManager(this)
            rvLocation.itemAnimator = DefaultItemAnimator()
            rvLocation.adapter = LocationItemAdapter(this, getLocationData())
        } catch (e: Exception) {
            Log.e(TAG, "initRecyclerView: " + e.message)
        }
    }

    private fun getLocationData(): List<StateKt>? {
        try {
            val inputStream = this.assets.open("Location.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charsets.UTF_8)
            val stateKtResponse = Gson().fromJson<StateListKtResponse>(json, StateListKtResponse::class.java)
            if (stateKtResponse?.stateListData != null && stateKtResponse.stateListData?.stateList?.isNotEmpty()!!) {
                return stateKtResponse.stateListData?.stateList!!
            }
        } catch (e: Exception) {
            Log.e(TAG, "getLocationData: " + e.message)
        }
        return null
    }
}
