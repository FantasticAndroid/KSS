package com.android.autopager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
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

        val verticalPagerAdapter = VerticalPagerAdapter(this, arrayOf("Page1", "Page2"))
        autoPager.adapter = verticalPagerAdapter
        autoPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        autoPager.isUserInputEnabled = false
        autoPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d(TAG, "onPageSelected: ".plus(position))
            }
        })
    }
}
