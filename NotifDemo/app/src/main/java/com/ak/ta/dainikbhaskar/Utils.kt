package com.ak.ta.dainikbhaskar

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.ak.ta.dainikbhaskar.activity.R

class Utils {

    companion object {
        fun getAppIcon(): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                R.mipmap.ic_notification_small
            } else {
                R.mipmap.ic_launcher
            }
        }

        fun getThemeColor(context: Context, color: String): Int {
            return try {
                Color.parseColor(color.trim { it <= ' ' })
            } catch (e: Exception) {
                ContextCompat.getColor(context, R.color.colorPrimary)
            }
        }

        fun generateCustomNotification(
            context: Context,
            notificationBuilder: NotificationCompat.Builder,
            downloadedBitmap: Bitmap?,
            message: String?,
            isVideo: Boolean
        ) {
            val contentView =
                RemoteViews(context.packageName, R.layout.layout_expanded_notification)
            contentView.setTextViewText(R.id.notification_title, message)
            if (downloadedBitmap != null) {
                contentView.setImageViewBitmap(R.id.notification_image, downloadedBitmap)
                if (isVideo) {
                    try {
                        contentView.setViewVisibility(R.id.ivVideoFlag, View.VISIBLE)
                        contentView.setImageViewResource(
                            R.id.ivVideoFlag,
                            R.drawable.ic_video_play_flag
                        )
                    } catch (ignored: java.lang.Exception) {
                    }
                }
                notificationBuilder
                    .setCustomContentView(contentView)
                    .build()
            } else {
                contentView.setViewVisibility(R.id.notification_image, View.GONE)
                contentView.setViewVisibility(R.id.vRightView, View.GONE)
                notificationBuilder
                    .setCustomContentView(contentView)
                    .build()
            }
        }

        fun generateDefaultNotification(
            notificationBuilder: NotificationCompat.Builder,
            downloadedBitmap: Bitmap?
        ) {
            if (downloadedBitmap != null) {
                notificationBuilder
                    .setLargeIcon(downloadedBitmap)
            }
        }

        //push type 5
        fun generateDefaultBigImageNotification(
            notificationBuilder: NotificationCompat.Builder,
            downloadedBitmap: Bitmap?
        ) {
            if (downloadedBitmap != null) {
                notificationBuilder
                    .setLargeIcon(downloadedBitmap)
                    .setStyle(
                        NotificationCompat.BigPictureStyle()
                            .bigPicture(downloadedBitmap)
                            .bigLargeIcon(null)
                    )
            }
        }

        fun generateDefaultMultiLineNotification(
            notificationBuilder: NotificationCompat.Builder,
            downloadedBitmap: Bitmap?,
            message: String?
        ) {
            if (downloadedBitmap != null) {
                notificationBuilder
                    .setLargeIcon(downloadedBitmap)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(message)
                    )
            } else {
                notificationBuilder
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(message)
                    )
            }
        }

        fun setNotificationProperty(
            context: Context?,
            notification: Notification?
        ): Notification? {
            if (context != null && notification != null) {
                notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL

                val soundStatus = true
                /*soundStatus = getNotificationSoundStatus(context)*/

                if (soundStatus) {
                    val uri = RingtoneManager.getDefaultUri(
                        RingtoneManager.TYPE_NOTIFICATION
                    )
                    if (uri != null) {
                        notification.sound = uri
                    }
                } else {
                    notification.sound = null
                }
                val vibrateStatus = true
                /*vibrateStatus = getNotificationVibrateStatus(context)*/

                if (vibrateStatus) {
                    notification.vibrate = longArrayOf(0, 100, 200, 300)
                } else {
                    notification.vibrate = longArrayOf(-1)
                }
                notification.defaults =
                    notification.defaults or Notification.DEFAULT_LIGHTS
            }
            return notification
        }
    }

    interface PushType {
        companion object {
            const val PUSH_TYPE_SMALL = 1
            const val PUSH_TYPE_BIG = 2
            const val PUSH_TYPE_MULTILINE = 3
            const val PUSH_TYPE_DEFAULT = 4
            const val PUSH_TYPE_DEFAULT_BIG = 5
            const val PUSH_TYPE_DEFAULT_MULTILINE = 6
            const val PUSH_TYPE_CRICKET_NOTIFICATION = 7
            const val PUSH_TYPE_AUDIO_VIDEO_NOTIFICATION = 8
            const val NOTIFICATION_TYPE_SYSTEM = 1
            const val NOTIFICATION_TYPE_SMALL_IMAGE = 2
            const val NOTIFICATION_TYPE_BIG_IMAGE = 3
            const val NOTIFICATION_TYPE_APPEND = 10
        }
    }
}