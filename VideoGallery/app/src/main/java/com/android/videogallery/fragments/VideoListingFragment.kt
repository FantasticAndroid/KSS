package com.android.videogallery.fragments;

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.videogallery.R
import com.android.videogallery.Utils
import com.android.videogallery.adapters.RecyclerAdapter
import com.android.videogallery.models.VideoGallery
import com.android.videogallery.viewitems.ExoPlayerRecyclerViewItem
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_video_listing.*
import java.util.*

class VideoListingFragment : CoreFragment() {

    private var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null
    private var recyclerAdapter: RecyclerAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var deviceWidth: Int = 0
    private var firstTime = true
    private val mArticleIds = ArrayList<String>()
    private val videoGalleryArrayList = ArrayList<VideoGallery>()

    private var mCatColor: String? = null
    private var mTitle: String? = null
    private var mTitleInEnglish: String? = null
    private var mCategoryName: String? = null
    private var mName: String? = null


    companion object {
        private val TAG = VideoListingFragment::class.java.simpleName


        fun newInstance(): VideoListingFragment {
            return VideoListingFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_listing,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deviceWidth = Utils.getDeviceWidthAndHeight(this)[0]

        /*Drawable dividerDrawable = ContextCompat.getDrawable(dbApplication, R.drawable.drawable_divider_exo_player_rv);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(dbApplication, RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(dividerDrawable);
        exoPlayerRv.addItemDecoration(dividerItemDecoration);
        exoPlayerRv.setItemAnimator(new DefaultItemAnimator());*/

        linearLayoutManager = LinearLayoutManager(videoApp, RecyclerView.VERTICAL, false)
        exoPlayerRv.layoutManager = linearLayoutManager

        /*val gestureDetector = GestureDetector(coreActivity, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                //return super.onFling(e1, e2, velocityX, velocityY)
                return true
            }
        })
        exoPlayerRv.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }*/

        setRecyclerAdapter()
        processData()
    }

    private fun setTopVideoListFromBundle(videoList: ArrayList<VideoGallery>) {
        for (videoGallery in videoList) {

            val exoPlayerRecyclerViewItem = ExoPlayerRecyclerViewItem(videoApp, videoGallery, deviceWidth)
            recyclerAdapter?.add(exoPlayerRecyclerViewItem)
            mArticleIds.add(videoGallery.id!!)

            /*val viewItem = VideoListingViewItem()
            viewItem.data = videoGallery
            recyclerAdapter?.add(viewItem)*/
        }
        videoGalleryArrayList.clear()
        videoGalleryArrayList.addAll(videoList)

        playExoVideoAtFirst()
    }

    private fun playExoVideoAtFirst() {
        if (firstTime) {
            Handler(Looper.getMainLooper()).post { exoPlayerRv.playVideo(false) }
            firstTime = false
        }
    }

    private fun processData() {
        val bundle = arguments
        if (null != bundle) {
            mName = bundle.getString(BundleConstants.SELECTED_CAT_NAME)
            val videoJson = Utils.readJSONFromAssetFile(videoApp, "gallery.json")

            val typeToken = object : TypeToken<List<VideoGallery>>() {}.type
            videoGalleryList.addAll(
                GsonBuilder().create().fromJson<List<VideoGallery>>(
                    videoJson,
                    typeToken
                )
            )


            mIsTopNewsListing = bundle.getBoolean(BundleConstants.IS_TOP_NEWS_LISTING)
            if (mIsTopNewsListing) {
                swipeRefresh.isEnabled = false

                mCategoryId = bundle.getString(BundleConstants.CATEGORY_ID)
                mCategoryName = bundle.getString(BundleConstants.CATEGORY_NAME)
                mCatColor = DbColorThemeHelper.getInstance().getCategoryColor(mCategoryId)
                mPaginationEnabled = false

                val videoList = bundle.getParcelableArrayList<VideoGallery>(BundleConstants.TOP_VIDEO_LIST)

                videoList?.let {
                    mTitle = dbApplication.getString(R.string.top_videos)
                    mTitleInEnglish = Constants.EN_TITLE_VIDEO_GALLERY
                    setTopVideoListFromBundle(it, mCategoryId)
                    alreadyTracked = try {
                        trackPageView(GAConstants.ScreenNames.VIDEO_GALLERY_TOP_VIDEOS, mTitleInEnglish)
                        true
                    } catch (e: Exception) {
                        trackPageView(GAConstants.ScreenNames.VIDEO_GALLERY_TOP_VIDEOS, "")
                        true
                    }
                } ?: run {
                    mSubCategoryData?.let {
                        if (AppConstants.MENU_TYPE_VIDEO_LIST.equals(it.type, true)) {
                            mTitle = dbApplication.getString(R.string.top_videos)
                            mTitleInEnglish = Constants.EN_TITLE_VIDEO_GALLERY
                        } else {
                            mTitle = it.labelInEnglish
                            mTitleInEnglish = it.labelInEnglish
                        }
                    }
                }
            } else {
                onRefreshListener = initSwipeRefreshListener()
                swipeRefresh.setOnRefreshListener(onRefreshListener)
                mSubCategoryData = bundle.getParcelable(BundleConstants.MENU_ITEM)
                mSeeMoreUrl = bundle.getString(BundleConstants.SEE_MORE_URL)
                mType = bundle.getString(BundleConstants.CATEGORY_TYPE)
                mCategoryId = bundle.getString(BundleConstants.CATEGORY_ID)
                mCategoryName = bundle.getString(BundleConstants.CATEGORY_NAME)
                mIsFromMEnu = bundle.getBoolean(BundleConstants.IS_FROM_MORE_MENU)
                mCatColor = DbColorThemeHelper.getInstance().getCategoryColor(mCategoryId)

                mSubCategoryData?.let {
                    showProgressBar(AppConstants.TYPE_DEFAULT)
                    fetchVideoList()
                    if (AppConstants.MENU_TYPE_VIDEO_LIST.equals(it.type, true)) {
                        mTitle = dbApplication.getString(R.string.top_videos)
                        mTitleInEnglish = Constants.EN_TITLE_VIDEO_GALLERY
                    } else {
                        mTitle = it.labelInEnglish
                        mTitleInEnglish = it.labelInEnglish
                    }
                }
            }
        }
        if (parentFragment == null) {
            //Track page view
            if (!alreadyTracked) {
                if (null != mType) {
                    val newsID = mCategoryId
                    var unformattedUrl = ""
                    when (mType) {
                        AppConstants.MENU_TYPE_PHOTO_LIST -> unformattedUrl = UrlUtil.getPhotoListUrl()
                        AppConstants.MENU_TYPE_VIDEO_LIST -> unformattedUrl = UrlUtil.getVideoListUrl()
                    }
                    val newsListUrl = String.format(unformattedUrl, newsID, 0, 0)
                    Log.e("Tracker", "Title: " + analyticScreenName
                            + " Url: " + newsListUrl)
                }
                trackPageView(analyticScreenName, analyticCategoryName)
                alreadyTracked = true
            }
        }
    }

    private fun setRecyclerAdapter() {
        exoPlayerRv.setMediaObjects(videoGalleryArrayList)
        recyclerAdapter = RecyclerAdapter(rxBus)
        recyclerAdapter?.setItemAnimationType(RecyclerAdapter.ANIMATION_TYPE_LIST_ITEM)

        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        exoPlayerRv.layoutManager = linearLayoutManager
        exoPlayerRv.adapter = recyclerAdapter
        exoPlayerRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onPageScrolled(dy)
            }
        })
        exoPlayerRv.isNestedScrollingEnabled = true
    }

    private fun onPageScrolled(dy: Int) {
        if (dy > 0 && isLastItemVisible() && mPaginationEnabled) {
            showPaginationProgress()
            fetchVideoList()
        }
    }

    private fun fetchVideoList() {
        try {
            if (CommonUtils.isNetworkAvailable(dbApplication)) {
                if (null != mSubCategoryData && !TextUtils.isEmpty(mSubCategoryData?.id)) {
                    val unformattedUrl = UrlUtil.getVideoListUrl()
                    fetchVideoList(unformattedUrl, mSubCategoryData!!.id)
                } else if (!TextUtils.isEmpty(mSeeMoreUrl)) {
                    mSeeMoreUrl?.let {
                        val unformattedUrl = dbApplication.getString(R.string.see_more_url_format)
                        fetchVideoList(unformattedUrl, it)
                    }
                } else {
                    showErrorView(dbApplication.getString(R.string.error_no_news))
                }
            } else {
                DBToast.showAlertToast(dbApplication, dbApplication.getString(R.string.alert_title_alert), dbApplication.getString(R.string.alert_network_not_exist))
            }
        } catch (e: Exception) {
            Log.e(TAG, "fetchVideoList: " + e.message)
        }
    }

    private fun fetchVideoList(unformattedUrl: String, idOrUrl: String) {

        val url = String.format(unformattedUrl, idOrUrl, mPaginationOffset, NO_OF_RESULTS_PER_PAGE)

        val call = ApiClient.getClient(dbApplication).create(ApiService::class.java).fetchVideoGalleryList(url)

        call.enqueue(object : RetroCallbackProvider<VideoListResponse>() {

            override fun onSuccess(call: Call<VideoListResponse>?, response: VideoListResponse) {
                try {
                    onVideoListFetched(response.data.videoGallery)
                } catch (e: Exception) {
                    showErrorView(dbApplication.getString(R.string.alert_request_unable_to_process))
                }
            }

            override fun onFailed(call: Call<VideoListResponse>?, t: Throwable?) {
                showErrorView(dbApplication.getString(R.string.error_no_news))
            }
        })
    }

    private fun showPaginationProgress() {
        if (mPaginationOffset > 0) {
            mPaginationEnabled = false
            mPaginationResults = true
            recyclerAdapter?.add(PaginationProgressViewItem())
            exoPlayerRv.scrollToPosition(recyclerAdapter?.itemCount!!.minus(1))
        }
    }

    private fun hidePaginationProgress() {
        if (mPaginationOffset > 0) {
            recyclerAdapter?.removeAllItemsWithClass(PaginationProgressViewItem::class.java)
        }
    }

    private fun isLastItemVisible(): Boolean {
        return linearLayoutManager?.findLastCompletelyVisibleItemPosition() == recyclerAdapter?.itemCount?.minus(1)
    }

    override fun handleEvent(event: Any) {
        if (event is NavigationEvent<*>) {
            handleNavigationEvent(event)
            if (exoPlayerRv != null) {
                exoPlayerRv.onPausePlayer()
            }
        }
    }

    private fun handleNavigationEvent(event: NavigationEvent<*>) {
        when (event.flag) {
            NavigationEvent.EVENT_VIDEO_ITEM_CLICKED -> {
                if (this.toString() != event.filter && !mIsTopNewsListing)
                    return
                launchVideoDetailScreen(event.data as VideoGallery)
            }
            NavigationEvent.EVENT_UPDATE_DETAILS_TOOLBAR -> {
                setupActionBar(toolBarSetting)
            }
        }
    }

    /**
     * @param videoGallery
     */
    private fun launchVideoDetailScreen(videoGallery: VideoGallery) {
        try {
            val selectedIndex = videoGalleryArrayList.indexOf(videoGallery)
            val playerVideoGallery = exoPlayerRv.currentVideoGallery
            var currentPlayerPosition = 0L
            if (playerVideoGallery != null && videoGallery.id.equals(playerVideoGallery.id)) {
                currentPlayerPosition = exoPlayerRv.currentVideoPosition
            }
            AppNavigatorHelper.getInstance().launchExoVideoGalleryForSelectedCategory(mBaseActivity, videoGalleryArrayList, selectedIndex,
                    videoGallery.catId, currentPlayerPosition)
        } catch (e: Exception) {
            Log.e(TAG, e.message + "")
        }
    }

    override fun getFragmentLayoutId(): Int {
        return R.layout.fragment_exo_player_recycler_view
    }


    fun onVideoListFetched(videoList: List<VideoGallery>?) {
        if (isVisible) {
            hideProgressBar()
            hidePaginationProgress()
            if (!mPaginationResults) {
                clearAdapter()
            }

            if (null != videoList && videoList.isNotEmpty()) {
                videoGalleryArrayList.addAll(videoList)
                for (i in videoList.indices) {
                    val videoGallery = videoList[i]
                    videoGallery.reponseFilter = this.toString()
                    videoGallery.isVideoGallery = true
                    /*val viewItem = VideoListingViewItem()
                    viewItem.data = videoGallery
                    recyclerAdapter?.add(viewItem)*/

                    val exoPlayerRecyclerViewItem = ExoPlayerRecyclerViewItem(dbApplication, videoGallery, deviceWidth)
                    recyclerAdapter?.add(exoPlayerRecyclerViewItem)
                    mArticleIds.add(videoGallery.id)
                }

                mPaginationOffset += videoList.size
                mPaginationEnabled = videoList.size == NO_OF_RESULTS_PER_PAGE

                playExoVideoAtFirst()

                if (AdsDataModel.isAdsEnable()) {
                    var adCount = 0
                    val startPosition = recyclerAdapter?.itemCount!!.minus(videoList.size)
                    var offset = -1
                    if (startPosition == 0) offset = 0
                    val commonAdsAttributes = mAdsSharedPref.getAdsDataFromKey(AdsSharedPref.KEY_PHOTO_VIDEO_LIST_BANNER_AD, CommonAdsAttributes::class.java)
                    for (index in 0 until videoList.size + adCount) {
                        if (null != commonAdsAttributes && index != 0 && AdsDataModel.isBanneAdsAvailableForPosition(commonAdsAttributes, startPosition + index + offset)) {
                            val nativeNewsAdViewItem = BannerAd250CardViewItem(mAdsSharedPref, this)
                            val model = AdItemDataModel(commonAdsAttributes, AdsDataModel.getAdsSlot(commonAdsAttributes.itemList, startPosition + index + offset) - 1)
                            nativeNewsAdViewItem.data = model
                            recyclerAdapter?.addAtPosition(nativeNewsAdViewItem, startPosition + index)
                            adCount++
                        }
                    }
                }
            } else {
                showErrorView(dbApplication.getString(R.string.error_no_videos))
            }
        }
    }

    private fun showErrorView(msg: String) {
        EasyDialogUtils.showInfoDialog(mBaseActivity, dbApplication.getString(R.string.alert_title_error), msg)
    }

    private fun clearAdapter() {
        swipeRefresh.isRefreshing = false
        mPaginationResults = false
        mPaginationEnabled = true
        mPaginationOffset = 0
    }

    override fun getToolBarSetting(): ToolbarSetting? {
        return if (mIsTopNewsListing) {
            ToolbarSetting(ToolbarSetting.AppToolBarBuilder(true)
                    .setBackButtonEnabled(!mIsFirstLevelScreen)
                    .setTitle(mTitle)
                    .setBackground(mCatColor))
        } else if (!TextUtils.isEmpty(mName)) {
            ToolbarSetting(ToolbarSetting.AppToolBarBuilder(true)
                    .setBackButtonEnabled(!mIsFirstLevelScreen)
                    .setTitle(mName)
                    .setBackground(mCatColor))
        } else {
            null
        }
    }

    override fun onPause() {
        super.onPause()
        exoPlayerRv?.onPausePlayer()
    }

    override fun onDestroyView() {
        exoPlayerRv?.let {
            it.onStopPlayer()
            it.adapter = null
        }
        swipeRefresh.setOnRefreshListener(null)
        recyclerAdapter = null

        if (null != mAdsBannerHandler) {
            mAdsBannerHandler?.removeCallbacksAndMessages(null)
            mAdsBannerHandler = null
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        exoPlayerRv?.releasePlayer()
        super.onDestroy()
    }

    private fun initSwipeRefreshListener(): SwipeRefreshLayout.OnRefreshListener {
        return SwipeRefreshLayout.OnRefreshListener {
            videoGalleryArrayList.clear()
            swipeRefresh.isRefreshing = false
            recyclerAdapter?.clear()
            mPaginationOffset = 0
            mPaginationEnabled = true
            mPaginationResults = false
            fetchVideoList()
        }
    }

    override fun onBannerAdsLoadedFailed(adapterItem: Any?) {
        if (null != adapterItem && null != mAdsBannerHandler) {
            mAdsBannerHandler?.postDelayed(Runnable {
                try {
                    if (isVisible) {
                        //Do something after 100ms
                        if (recyclerAdapter != null) {
                            if (adapterItem is BannerAd250CardViewItem) {
                                recyclerAdapter?.removeAllItemsWithClass(BannerAd250CardViewItem::class.java)
                            } else {
                                val position = recyclerAdapter?.getItemPosition(adapterItem as AdapterItem<*>?)
                                recyclerAdapter?.removeItemAtPosition(position!!)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, e.message + "")
                }
            }, 1000)
        }
    }
}