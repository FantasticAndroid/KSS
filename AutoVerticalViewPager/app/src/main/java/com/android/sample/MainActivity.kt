package com.android.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.autopager.AutoVerticalPageProvider
import com.android.autopager.R
import com.android.autopager.Util
import com.android.autopager.VerticalPagerAdapter
import com.android.autopager.callback.AutoVerticalListener
import com.android.autopager.model.PagerModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pagerModelList = readAndGetAdapterList()

        val autoVerticalListener = AutoVerticalPageProvider(autoPager)
        val verticalPagerAdapter = VerticalPagerAdapter(this, autoVerticalListener, pagerModelList)
        autoPager.adapter = verticalPagerAdapter
        autoPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        autoPager.isUserInputEnabled = false
    }

    private fun readAndGetAdapterList(): List<PagerModel> {

        val jsonArray = Util.readFileFromJson(
            applicationContext,
            "pages.json"
        )
        val typeToken = object : TypeToken<List<PagerModel>>() {}.type
        return GsonBuilder().create().fromJson<List<PagerModel>>(jsonArray, typeToken)
    }
}
