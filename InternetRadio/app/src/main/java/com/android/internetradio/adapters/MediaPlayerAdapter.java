package com.android.internetradio.adapters;//package com.bhaskar.darwin.ui.fm.adapters;
//
//import android.content.Context;
//import android.media.AudioManager;
//import android.net.Uri;
//import android.os.SystemClock;
//import android.support.v4.media.MediaMetadataCompat;
//import android.support.v4.media.session.PlaybackStateCompat;
//
//import com.bhaskar.darwin.ui.fm.helpers.PlaybackInfoListener;
//
//import net.protyposis.android.mediaplayer.MediaPlayer;
//import net.protyposis.android.mediaplayer.MediaSource;
//import net.protyposis.android.mediaplayer.UriSource;
//
///**
// * Exposes the functionality of the {@link MediaPlayer} and implements the {@link PlayerAdapter}
// * so that {@link com.bhaskar.darwin.ui.fm.activities.InternetRadioActivity} can control music playback.
// */
//public final class MediaPlayerAdapter extends PlayerAdapter {
//
//    private MediaPlayer mMediaPlayer;
//    private String mMediaId;
//    private PlaybackInfoListener mPlaybackInfoListener;
//    private MediaMetadataCompat mCurrentMedia;
//    private int mState;
//
//    // Work-around for a MediaPlayer bug related to the behavior of MediaPlayer.seekTo()
//    // while not playing.
//    private int mSeekWhileNotPlaying = -1;
//
//    private Context context;
//
//    /**
//     * @param context
//     * @param listener
//     */
//    public MediaPlayerAdapter(Context context, PlaybackInfoListener listener) {
//        super(context);
//        this.context = context;
//        mPlaybackInfoListener = listener;
//    }
//
//    /**
//     * Once the {@link MediaPlayer} is released, it can't be used again, and another one has to be
//     * created. In the onStop() method of the {@link com.bhaskar.darwin.ui.fm.activities.InternetRadioActivity} the {@link MediaPlayer} is
//     * released. Then in the onStart() of the {@link com.bhaskar.darwin.ui.fm.activities.InternetRadioActivity} a new {@link MediaPlayer}
//     * object has to be created. That's why this method is private, and called by load(int) and
//     * not the constructor.
//     */
//    private void initializeMediaPlayer() {
//        setNewState(PlaybackStateCompat.STATE_BUFFERING);
//        if (mMediaPlayer == null) {
//            mMediaPlayer = new MediaPlayer();
//            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    mPlaybackInfoListener.onPlaybackCompleted();
//
//                    // Set the state to "paused" because it most closely matches the state
//                    // in MediaPlayer with regards to available state transitions compared
//                    // to "stop".
//                    // Paused allows: seekTo(), start(), pause(), stop()
//                    // Stop allows: stop()
//                    setNewState(PlaybackStateCompat.STATE_PAUSED);
//                }
//            });
//
//            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    play();
//                    setNewState(PlaybackStateCompat.STATE_PLAYING);
//                }
//            });
//
//            mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//
//                @Override
//                public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                    switch (what) {
//                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                            setNewState(PlaybackStateCompat.STATE_BUFFERING);
//                            break;
//                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                            setNewState(PlaybackStateCompat.STATE_PLAYING);
//                            ///setNewState(PlaybackStateCompat.STATE_);
//                            break;
//                    }
//                    return false;
//                }
//            });
//        }
//    }
//
//    // Implements PlaybackControl.
//    @Override
//    public void playFromMedia(MediaMetadataCompat metadata) {
//        mCurrentMedia = metadata;
//        String mediaUrl = String.valueOf(metadata.getDescription().getMediaUri());
//        String mediaId = String.valueOf(metadata.getDescription().getMediaId());
//        playFile(mediaUrl, mediaId);
//    }
//
//    @Override
//    public MediaMetadataCompat getCurrentMedia() {
//        return mCurrentMedia;
//    }
//
//    /**
//     * @param mediaUrl
//     * @param mediaId
//     */
//    private void playFile(String mediaUrl, String mediaId) {
//        boolean mediaChanged = (mMediaId == null || !mediaId.equals(mMediaId));
//
//        if (!mediaChanged) {
//            if (!isPlaying()) {
//                play();
//            }
//            return;
//        } else {
//            release();
//        }
//
//        mMediaId = mediaId;
//
//        initializeMediaPlayer();
//
//        try {
//            MediaSource mediaSource = new UriSource(context, Uri.parse(mediaUrl));
//            mMediaPlayer.setDataSource(mediaSource);
//
//            try {
//                mMediaPlayer.prepareAsync();
//            } catch (Exception e) {
//                e.printStackTrace();
//                setNewState(PlaybackStateCompat.STATE_ERROR);
//                ////throw new RuntimeException("Failed to open FM with FM URL: " + mediaUrl, e);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            setNewState(PlaybackStateCompat.STATE_ERROR);
//            ////throw new RuntimeException("Failed to open FM with FM URL: " + mediaUrl, e);
//        }
//
//    }
//
//    @Override
//    public void onStop() {
//        // Regardless of whether or not the MediaPlayer has been created / started, the state must
//        // be updated, so that MediaNotificationManager can take down the notification.
//        setNewState(PlaybackStateCompat.STATE_STOPPED);
//        release();
//    }
//
//    private void release() {
//        try {
//            if (mMediaPlayer != null) {
//                mMediaPlayer.release();
//                mMediaPlayer = null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public boolean isPlaying() {
//        try {
//            return mMediaPlayer != null && mMediaPlayer.isPlaying();
//        } catch (Exception e) {
//            e.printStackTrace();
//            setNewState(PlaybackStateCompat.STATE_ERROR);
//        }
//        return false;
//    }
//
//    @Override
//    protected void onPlay() {
//        try {
//            if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
//                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mMediaPlayer.start();
//                setNewState(PlaybackStateCompat.STATE_PLAYING);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            setNewState(PlaybackStateCompat.STATE_ERROR);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        try {
//            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
//                mMediaPlayer.pause();
//                setNewState(PlaybackStateCompat.STATE_PAUSED);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            setNewState(PlaybackStateCompat.STATE_ERROR);
//        }
//    }
//
//    // This is the main reducer for the player state machine.
//    private void setNewState(@PlaybackStateCompat.State int newPlayerState) {
//        try {
//            mState = newPlayerState;
//
//            // Whether playback goes to completion, or whether it is stopped, the
//            // mCurrentMediaPlayedToCompletion is set to true.
//            if (mState == PlaybackStateCompat.STATE_STOPPED) {
//                /// mCurrentMediaPlayedToCompletion = true;
//            }
//
//            // Work around for MediaPlayer.getCurrentPosition() when it changes while not playing.
//            final long reportPosition;
//            if (mSeekWhileNotPlaying >= 0) {
//                reportPosition = mSeekWhileNotPlaying;
//
//                if (mState == PlaybackStateCompat.STATE_PLAYING) {
//                    mSeekWhileNotPlaying = -1;
//                }
//            } else {
//                reportPosition = mMediaPlayer == null ? 0 : mMediaPlayer.getCurrentPosition();
//            }
//
//            final PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder();
//            stateBuilder.setActions(getAvailableActions());
//            stateBuilder.setState(mState,
//                    reportPosition,
//                    1.0f,
//                    SystemClock.elapsedRealtime());
//            mPlaybackInfoListener.onPlaybackStateChange(stateBuilder.build());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Set the current capabilities available on this session. Note: If a capability is not
//     * listed in the bitmask of capabilities then the MediaSession will not handle it. For
//     * example, if you don't want ACTION_STOP to be handled by the MediaSession, then don't
//     * included it in the bitmask that's returned.
//     */
//    @PlaybackStateCompat.Actions
//    private long getAvailableActions() {
//        long actions = PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
//                | PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
//                | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
//                | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;
//        switch (mState) {
//            case PlaybackStateCompat.STATE_STOPPED:
//                actions |= PlaybackStateCompat.ACTION_PLAY
//                        | PlaybackStateCompat.ACTION_PAUSE;
//                break;
//            case PlaybackStateCompat.STATE_PLAYING:
//                actions |= PlaybackStateCompat.ACTION_STOP
//                        | PlaybackStateCompat.ACTION_PAUSE
//                        | PlaybackStateCompat.ACTION_SEEK_TO;
//                break;
//            case PlaybackStateCompat.STATE_PAUSED:
//                actions |= PlaybackStateCompat.ACTION_PLAY
//                        | PlaybackStateCompat.ACTION_STOP;
//                break;
//            default:
//                actions |= PlaybackStateCompat.ACTION_PLAY
//                        | PlaybackStateCompat.ACTION_PLAY_PAUSE
//                        | PlaybackStateCompat.ACTION_STOP
//                        | PlaybackStateCompat.ACTION_PAUSE;
//        }
//        return actions;
//    }
//
//    @Override
//    public void seekTo(long position) {
//        try {
//            if (mMediaPlayer != null) {
//                if (!mMediaPlayer.isPlaying()) {
//                    mSeekWhileNotPlaying = (int) position;
//                }
//                mMediaPlayer.seekTo((int) position);
//
//                // Set the state (to the current state) because the position changed and should
//                // be reported to clients.
//                setNewState(mState);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void setVolume(float volume) {
//        if (mMediaPlayer != null) {
//            mMediaPlayer.setVolume(volume, volume);
//        }
//    }
//}
