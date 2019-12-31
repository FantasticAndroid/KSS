package com.android.videogallery.activities

import android.os.Bundle
import com.android.videogallery.R
import com.android.videogallery.Utils
import kotlinx.android.synthetic.main.activity_main.*

class DashboardActivity : CoreActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playBtn.setOnClickListener {
            val bundle = Bundle()
            val videoUrl = "https://www.radiantmediaplayer.com/media/bbb-360p.mp4"
            bundle.putString(Utils.VIDEO_URL, videoUrl)
            ExoVideoPlayerActivity.startExoPlayerActivity(this, bundle)
        }
    }
}
