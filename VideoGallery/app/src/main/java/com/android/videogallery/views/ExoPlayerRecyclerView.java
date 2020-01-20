package com.android.videogallery.views;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.videogallery.R;
import com.android.videogallery.Utils;
import com.android.videogallery.activities.ExoVideoPlayerActivity;
import com.android.videogallery.models.VideoGallery;
import com.android.videogallery.viewitems.ExoPlayerRecyclerViewItem;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Objects;

public final class ExoPlayerRecyclerView extends RecyclerView {
    private static final String TAG = ExoPlayerRecyclerView.class.getSimpleName();
    private Context videoApp;
    private int videoSurfaceDefaultHeight = 0;
    private int screenDefaultHeight = 0;
    private int playPosition = -1;
    private boolean isVideoViewAdded;

    private ImageView mediaCoverImage;
    private ImageView volumeControl, pauseControl, fullScreenControl;
    private PlayerView videoSurfaceView;
    private SimpleExoPlayer videoPlayer;
    private View viewHolderParent;
    private ProgressBar videoProgressBar;
    private FrameLayout mediaContainer, exoControlsContainer;

    private boolean isVolumeMute = false;

    private ArrayList<VideoGallery> mediaObjects = new ArrayList<>();

    private void init(@NonNull Context context) {
        this.videoApp =  context.getApplicationContext();

        Display display = ((WindowManager) Objects.requireNonNull(
                context.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        videoSurfaceDefaultHeight = point.x;
        screenDefaultHeight = point.y;

        videoSurfaceView = (PlayerView) LayoutInflater.from(context).inflate(R.layout.layout_video_listing_exo_player, null);
        exoControlsContainer = videoSurfaceView.findViewById(R.id.exoplayer_controller);

        fullScreenControl = videoSurfaceView.findViewById(R.id.exo_full_screen);
        volumeControl = videoSurfaceView.findViewById(R.id.exo_volume);
        pauseControl = videoSurfaceView.findViewById(R.id.exo_pause);
        setVideoControlState();

        volumeControl.setOnClickListener(exoControlsClickListener);
        fullScreenControl.setOnClickListener(exoControlsClickListener);

        ////videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        //Create the player using ExoPlayerFactory
        videoPlayer = ExoPlayerFactory.newSimpleInstance(videoApp);
        // Disable Player Control
        videoSurfaceView.setUseController(true);
        videoSurfaceView.setControllerAutoShow(true);
        // Bind the player to the view.
        videoSurfaceView.setPlayer(videoPlayer);

        //setVolumeControl(VolumeState.ON);
        addOnScrollListener(onRecyclerViewScrollListener);
        addOnChildAttachStateChangeListener(onRvChildViewAttachStateChangeListener);
        videoPlayer.addListener(exoPlayerEventsListener);
    }

    private final RecyclerView.OnScrollListener onRecyclerViewScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (mediaCoverImage != null) {
                    // show the old thumbnail
                    mediaCoverImage.setVisibility(VISIBLE);
                }
                // There's a special case when the end of the list has been reached.
                // Need to handle that with this bit of logic
                if (!recyclerView.canScrollVertically(1)) {
                    playVideo(true);
                } else {
                    playVideo(false);
                }
            }
        }
    };

    /**
     * @param isEndOfList
     */
    public void playVideo(boolean isEndOfList) {

        int targetPosition;

        if (!isEndOfList) {
            int startPosition = ((LinearLayoutManager) Objects.requireNonNull(
                    getLayoutManager())).findFirstVisibleItemPosition();
            int endPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();

            // if there is more than 2 list-items on the screen, set the difference to be 1
            if (endPosition - startPosition > 1) {
                endPosition = startPosition + 1;
            }

            // something is wrong. return.
            if (startPosition < 0 || endPosition < 0) {
                return;
            }

            // if there is more than 1 list-item on the screen
            if (startPosition != endPosition) {
                int startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition);
                int endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition);

                targetPosition =
                        startPositionVideoHeight > endPositionVideoHeight ? startPosition : endPosition;
            } else {
                targetPosition = startPosition;
            }
        } else {
            targetPosition = mediaObjects.size() - 1;
        }

        Log.d(TAG, "playVideo: target position: " + targetPosition);

        // video is already playing so return
        if (targetPosition == playPosition) {
            return;
        }

        // set the position of the list-item that is to be played
        playPosition = targetPosition;
        if (videoSurfaceView == null) {
            return;
        }

        // remove any old surface views from previously playing videos
        videoSurfaceView.setVisibility(INVISIBLE);
        removeVideoView(videoSurfaceView);

        int currentPosition =
                targetPosition - ((LinearLayoutManager) Objects.requireNonNull(
                        getLayoutManager())).findFirstVisibleItemPosition();

        View child = getChildAt(currentPosition);
        if (child == null) {
            return;
        }

        ExoPlayerRecyclerViewItem.ExoPlayerRvHolder holder = (ExoPlayerRecyclerViewItem.ExoPlayerRvHolder) child.getTag();
        if (holder == null) {
            playPosition = -1;
            return;
        }
        mediaCoverImage = holder.mediaCoverImage;
        viewHolderParent = holder.itemView;
        mediaContainer = holder.mediaContainer;
        videoProgressBar = holder.videoProgressBar;

        videoSurfaceView.setPlayer(videoPlayer);

        DataSource.Factory defaultDataSourceFactory =
                new DefaultDataSourceFactory(
                        videoApp, Util.getUserAgent(videoApp, videoApp.getString(R.string.app_name)));

        VideoGallery videoGallery = mediaObjects.get(targetPosition);
        String mediaUrl = videoGallery.getVideoUrl();
        if (!TextUtils.isEmpty(mediaUrl)) {
            Uri uri = Uri.parse(mediaUrl);
            MediaSource videoSource = buildMediaSource(defaultDataSourceFactory, uri);

            /*MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(mediaUrl));*/

            videoPlayer.prepare(videoSource);
            videoPlayer.setPlayWhenReady(true);
            fullScreenControl.setTag(videoGallery);
        } else {
            hideVideoProgressBar();
            onPausePlayer();
        }
    }

    private final OnChildAttachStateChangeListener onRvChildViewAttachStateChangeListener = new OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(@NonNull View view) {

        }

        @Override
        public void onChildViewDetachedFromWindow(@NonNull View view) {
            try {
                hideVideoProgressBar();
                if (isVideoViewAdded && videoSurfaceView != null &&
                        mediaCoverImage != null && viewHolderParent != null && viewHolderParent.equals(view)) {
                    removeVideoView(videoSurfaceView);
                    playPosition = -1;
                    videoSurfaceView.setVisibility(INVISIBLE);
                    mediaCoverImage.setVisibility(VISIBLE);
                }
                Log.d(TAG, "onChildViewDetachedFromWindow()");
            } catch (Exception e) {
                Log.e(TAG, "onChildViewDetachedFromWindow() " + e.getMessage());
            }
        }
    };

    private void hideVideoProgressBar(){
        if (videoProgressBar != null) {
            videoProgressBar.setVisibility(View.GONE);
        }
    }

    private void showVideoProgressBar(){
        if (videoProgressBar != null) {
            videoProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private final Player.EventListener exoPlayerEventsListener = new Player.EventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case Player.STATE_BUFFERING:
                    Log.d(TAG, "onPlayerStateChanged: STATE_BUFFERING");
                    if (exoControlsContainer != null) {
                        exoControlsContainer.setVisibility(View.GONE);
                    }
                    showVideoProgressBar();
                    break;
                case Player.STATE_ENDED:
                    Log.d(TAG, "onPlayerStateChanged: STATE_ENDED");
                    if (exoControlsContainer != null) {
                        exoControlsContainer.setVisibility(View.VISIBLE);
                    }
                    hideVideoProgressBar();
                    if (videoPlayer != null) {
                        videoPlayer.seekTo(0);
                    }
                    break;
                case Player.STATE_IDLE:
                    Log.d(TAG, "onPlayerStateChanged: STATE_IDLE");
                    if (exoControlsContainer != null) {
                        exoControlsContainer.setVisibility(View.VISIBLE);
                    }
                    hideVideoProgressBar();
                    break;
                case Player.STATE_READY:
                    Log.d(TAG, "onPlayerStateChanged: STATE_READY");
                    if (exoControlsContainer != null) {
                        exoControlsContainer.setVisibility(View.VISIBLE);
                    }
                    hideVideoProgressBar();
                    if (!isVideoViewAdded) {
                        addVideoView();
                    }
                    checkAndStopAudioMediaService();
                    break;
                default:
                    Log.d(TAG, "onPlayerStateChanged: STATE_default");
                    hideVideoProgressBar();
                    if (exoControlsContainer != null) {
                        exoControlsContainer.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };

    private void addVideoView() {
        try {
            mediaContainer.addView(videoSurfaceView);
            isVideoViewAdded = true;
            videoSurfaceView.requestFocus();
            videoSurfaceView.setVisibility(VISIBLE);
            videoSurfaceView.setAlpha(1);
            mediaCoverImage.setVisibility(GONE);
        } catch (Exception e) {
            Log.e(TAG, "addVideoView() " + e.getMessage());
        }
    }

//    /**
//     * @param videoView
//     */
//    private void removeVideoView(@NonNull PlayerView videoView) {
//        try {
//            ViewGroup parent = (ViewGroup) videoView.getParent();
//            if (parent == null) {
//                return;
//            }
//
//            int index = parent.indexOfChild(videoView);
//            if (index >= 0) {
//                parent.removeViewAt(index);
//                isVideoViewAdded = false;
//                viewHolderParent.setOnClickListener(null);
//            }
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage() + "");
//        }
//    }

    /**
     * @param videoView
     */
    private void removeVideoView(@NonNull PlayerView videoView) {
        try {
            if (mediaContainer != null && viewHolderParent != null && videoView != null) {
                mediaContainer.removeView(videoView);
                isVideoViewAdded = false;
                viewHolderParent.setOnClickListener(null);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    /***
     *
     * @param playPosition
     * @return
     */
    private int getVisibleVideoSurfaceHeight(int playPosition) {
        int at = playPosition - ((LinearLayoutManager) Objects.requireNonNull(
                getLayoutManager())).findFirstVisibleItemPosition();
        Log.d(TAG, "getVisibleVideoSurfaceHeight: at: " + at);

        View child = getChildAt(at);
        if (child == null) {
            return 0;
        }

        int[] location = new int[2];
        child.getLocationInWindow(location);

        if (location[1] < 0) {
            return location[1] + videoSurfaceDefaultHeight;
        } else {
            return screenDefaultHeight - location[1];
        }
    }


    public ExoPlayerRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }


    public ExoPlayerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExoPlayerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setMediaObjects(@NonNull ArrayList<VideoGallery> mediaObjects) {
        this.mediaObjects = mediaObjects;
    }

    private final OnClickListener exoControlsClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.exo_full_screen:
                    try {
                        Object object = v.getTag();
                        if (videoPlayer != null && object instanceof VideoGallery) {
                            long currentPosition = videoPlayer.getCurrentPosition();
                            showFullExoPlayerActivity(v.getContext(), currentPosition,(VideoGallery) object);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "exoControlsClickListener: " + e.getMessage());
                    }
                    break;
                case R.id.exo_volume:
                    try {
                        isVolumeMute = !isVolumeMute;
                        setVolumeControl();
                        setVideoControlState();
                    } catch (Exception e) {
                        Log.e(TAG, "exoControlsClickListener: " + e.getMessage());
                    }
                    break;
            }
        }
    };

    /**
     *
     * @param context
     * @param currentPosition
     * @param videoGallery
     */
    private void showFullExoPlayerActivity(@NonNull Context context, long currentPosition, @NonNull VideoGallery videoGallery) {
        onPausePlayer();
        Bundle bundle = new Bundle();
        bundle.putString(Utils.Companion.getVIDEO_URL(), videoGallery.getVideoUrl());
        bundle.putLong(Utils.Companion.getKEY_VIDEO_CURRENT_POSITION(), currentPosition);
        ExoVideoPlayerActivity.Companion.startExoPlayerActivity(context, bundle);
    }

    private void setVideoControlState() {
        if (isVolumeMute) {
            volumeControl.setImageResource(R.drawable.ic_volume_off);
        } else {
            volumeControl.setImageResource(R.drawable.ic_volume_on);
        }
    }

    private void setVolumeControl() {
        if (videoPlayer != null) {
            if (isVolumeMute) {
                videoPlayer.setVolume(0f);
            } else {
                videoPlayer.setVolume(0.5f);
            }
        }
    }

    private MediaSource buildMediaSource(@NonNull DataSource.Factory dataSourceFactory, @NonNull Uri uri) {
        @C.ContentType int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_DASH:
                Log.d(TAG, "buildMediaSource: TYPE = TYPE_DASH");
                return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_SS:
                Log.d(TAG, "buildMediaSource: TYPE = TYPE_SS");
                return new SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_HLS:
                Log.d(TAG, "buildMediaSource: TYPE = TYPE_HLS");
                return new HlsMediaSource.Factory(dataSourceFactory).setAllowChunklessPreparation(true).createMediaSource(uri);
            case C.TYPE_OTHER:
                Log.d(TAG, "buildMediaSource: TYPE = TYPE_OTHER");
                return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            default:
                Log.d(TAG, "buildMediaSource: TYPE = Unsupported type" + type);
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    public void onPausePlayer() {
        if (pauseControl != null) {
            pauseControl.performClick();
        }
    }

    public void onStopPlayer() {
        if (videoPlayer != null) {
            videoPlayer.stop(true);
        }
    }

    public void releasePlayer() {
        if (videoPlayer != null) {
            videoPlayer.release();
            videoPlayer = null;
        }
        viewHolderParent = null;
    }


    public VideoGallery getCurrentVideoGallery() {
        if (fullScreenControl.getTag() instanceof VideoGallery) {
            return (VideoGallery) fullScreenControl.getTag();
        } else {
            return null;
        }
    }

    public long getCurrentVideoPosition() {
        if (videoPlayer != null) {
            return videoPlayer.getCurrentPosition();
        } else {
            return 0L;
        }
    }

    private void checkAndStopAudioMediaService() {
        // Here you can stop your other Media Services to play this video
    }
}
