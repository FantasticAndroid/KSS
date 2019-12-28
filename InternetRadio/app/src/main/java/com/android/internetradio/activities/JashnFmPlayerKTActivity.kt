//package com.bhaskar.darwin.ui.fm.activities
//
//import android.graphics.drawable.AnimationDrawable
//import android.os.Bundle
//import android.os.Handler
//import android.util.Log
//import com.bhaskar.darwin.R
//import com.bhaskar.darwin.ui.base.CoreActivity
//import com.bhaskar.darwin.ui.fm.adapters.CategoryFmsRvAdapter
//import com.bhaskar.darwin.ui.fm.helpers.FMMetaDataProvider
//import com.bhaskar.darwin.ui.fm.helpers.MediaPlayerProvider
//import com.bhaskar.darwin.ui.fm.models.*
//import com.bhaskar.darwin.ui.fm.receivers.NetworkStateReceiver
//import com.bhaskar.darwin.ui.fm.services.MusicService
//import com.bhaskar.darwin.ui.fm.utils.FmSharedPref
//import com.bhaskar.utils.CommonUtils
//import com.bumptech.glide.request.RequestOptions
//import com.google.android.material.snackbar.Snackbar
//import kotlinx.android.synthetic.main.activity_player.*
//import uk.co.chrisjenx.calligraphy.CalligraphyConfig
//import java.util.*
//
//class JashnFmPlayerKTActivity : CoreActivity(), MediaPlayerProvider.OnUIInteractionListener,
//        FMMetaDataProvider.FMMetaDataListener {
//    companion object {
//        private val TAG = JashnFmPlayerKTActivity::class.java.simpleName
//    }
//
//    private var fmModel: FmModel? = null
//    private var fmStationList = ArrayList<FmStation>()
//    private var fmCategoryList = ArrayList<FmCategory>()
//    private var categoryFmsRvAdapter: CategoryFmsRvAdapter? = null
//    private var mediaPlayerProvider: MediaPlayerProvider? = null
//    private var animationDrawable: AnimationDrawable? = null
//    private var savedFmStation: FmSavedStation? = null
//    private var mNetworkStateReceiver: NetworkStateReceiver? = null
//    private var alertSnackBar: Snackbar? = null
//    private var fmMetaDataProvider: FMMetaDataProvider? = null
//    // Below params taken for GA event
//    private var mInitialPlayStationTime = 0L
//    private var mNextPlayStationTime = 0L
//    private var glideOptions: RequestOptions? = null
//    private var fmSharedPref: FmSharedPref? = null
//    private var startTime = 0L
//    private var difference = 0L
//    private var gaModel: GAModel? = null
//    private var lastTrackFM = ""
//    private var playHandler: Handler? = null
//    private var isPlayerServiceAlreadyRunning = false
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        isPlayerServiceAlreadyRunning = CommonUtils.isMyServiceRunning(this@JashnFmPlayerKTActivity, MusicService::class.java.simpleName)
//        Log.d(TAG, "isPlayerServiceAlreadyRunning: $isPlayerServiceAlreadyRunning")
//        CalligraphyConfig.initDefault(CalligraphyConfig.Builder().setDefaultFontPath(getString(R.string.default_font_path)).build())
//        setContentView(R.layout.activity_player)
//        playHandler = Handler()
//        fmSharedPref = FmSharedPref(dbApplication)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        val bundle = intent.extras
//        bundle?.let {
//
//        }
//
//    }
//}