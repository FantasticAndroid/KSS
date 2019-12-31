package com.android.videogallery

import com.android.videogallery.providers.ExoVideoPlayerProvider

class Utils {
    companion object{
        val KEY_AUDIO_PLAYBACK_EVENT = "key_audio_playback_event"
        //String KEY_IS_COMING_FROM_HAMBURGER_MENU = "key_is_coming_from_hamburger_menu";
        var ACTION_BROADCAST_PLAYBACK_CONTROL =
            BuildConfig.APPLICATION_ID + ExoVideoPlayerProvider::class.java.simpleName

        var VIDEO_URL = "video_url"
        /*String KEY_LAYOUT_TYPE = "key_layout_type";*/
        var KEY_EXO_PLAYER_POSITION = "key_exo_player_position"
        var KEY_EXO_PLAYER_WINDOW = "key_exo_player_window"
        var KEY_VIDEO_DURATION = "key_video_duration"

    }
}