package com.android.betterstetho

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class DashboardActivity : CoreActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        volleyBtn.setOnClickListener(this)
        retrofitBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.volleyBtn -> {
                startActivity(Intent(this@DashboardActivity,VolleyActivity::class.java))
            }
            R.id.retrofitBtn -> {
                startActivity(Intent(this@DashboardActivity,RetrofitActivity::class.java))
            }
        }
    }
}
