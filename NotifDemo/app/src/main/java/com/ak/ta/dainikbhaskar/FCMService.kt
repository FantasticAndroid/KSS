package com.ak.ta.dainikbhaskar

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ak.ta.dainikbhaskar.activity.MainActivity
import com.ak.ta.dainikbhaskar.activity.R
import com.ak.ta.dainikbhaskar.models.NotifModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson


class FCMService : FirebaseMessagingService() {

    companion object {
        private val TAG = FCMService::class.java.simpleName
    }

    /**
     * @param token String
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken() ".plus(token))
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        try {
            Log.d(TAG, "onMessageReceived() " + remoteMessage.data)
            val map = remoteMessage.data
            //handle DB notification
            val gson = Gson()
            val notifModel: NotifModel =
                gson.fromJson<NotifModel>(gson.toJsonTree(map), NotifModel::class.java)
            parseNotifModel(notifModel)
        } catch (e: Exception) {
            Log.e(TAG, "onMessageReceived()".plus(e.message))
        }
    }

    private fun parseNotifModel(notifModel: NotifModel) {
        Log.d(TAG, "parseNotifModel() " + notifModel)
        val notifType: Int?
        val pendingIntent: PendingIntent?
        val downloadedBitmap: Bitmap?
        try {
            notifType = notifModel.pushType?.toInt()

            val pushIntent = Intent(this, MainActivity::class.java)
            pushIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            pushIntent.putExtra(Utils.KEY_NOTIF_MODEL, notifModel)

            pendingIntent = if (notifType == Utils.PushType.NOTIFICATION_TYPE_APPEND) {
                PendingIntent.getActivity(
                    this,
                    Utils.NOTIFICATION_APPEND_ID,
                    pushIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            } else {
                PendingIntent.getActivity(
                    this,
                    System.currentTimeMillis().toInt(),
                    pushIntent,
                    PendingIntent.FLAG_ONE_SHOT
                )
            }

            val notifImageUrl = notifModel.imageURL
            // Make downloadedBitmap from notifImageUrl actually
            val futureTarget: FutureTarget<Bitmap> = Glide.with(applicationContext)
                .asBitmap()
                .load(notifImageUrl).submit()
            ////.submit(width, height)

            downloadedBitmap = futureTarget.get()
            ////val downloadedBitmap= BitmapFactory.decodeResource(resources,R.drawable.ic_launcher_foreground)
            generateNotification(notifType, notifModel, downloadedBitmap, pendingIntent, false)
        } catch (e: Exception) {
            Log.d(TAG, "parseNotifModel() " + e.message)
        }
    }

    private fun generateNotification(
        notifType: Int?,
        notificationInfo: NotifModel, downloadedBitmap: Bitmap,
        pendingIntent: PendingIntent, isVideo: Boolean
    ) {
        try {
            val `when` = System.currentTimeMillis()
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationBuilder: NotificationCompat.Builder
            val id = resources.getString(R.string.app_name)
            val nTitle =
                if (TextUtils.isEmpty(notificationInfo.title))
                    id
                else notificationInfo.title


            notificationBuilder = NotificationCompat.Builder(this, id)
                .setContentIntent(pendingIntent)
                .setWhen(`when`)
                .setSmallIcon(Utils.getAppIcon())
                .setContentTitle(nTitle)
                .setContentText(notificationInfo.message)
                .setDeleteIntent(getDeleteIntent())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroupSummary(true)
                //.setGroup(GROUP_KEY)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            /*.setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)*/

            if (!TextUtils.isEmpty(notificationInfo.color)) {
                notificationBuilder.color = Utils.getThemeColor(this, notificationInfo.color!!)
            }

            val count = 1
            when (notifType) {
                Utils.PushType.PUSH_TYPE_SMALL -> Utils.generateCustomNotification(
                    this,
                    notificationBuilder,
                    downloadedBitmap,
                    notificationInfo.message,
                    isVideo
                )
                Utils.PushType.PUSH_TYPE_DEFAULT -> {
                    Utils.generateDefaultNotification(notificationBuilder, downloadedBitmap)
                    setPushAction(
                        notificationInfo.actionText,
                        notificationBuilder,
                        getActionIntent(pendingIntent, count)
                    )
                }
                Utils.PushType.PUSH_TYPE_DEFAULT_BIG -> {
                    Utils.generateDefaultBigImageNotification(
                        notificationBuilder,
                        downloadedBitmap
                    )
                    setPushAction(
                        notificationInfo.actionText,
                        notificationBuilder,
                        getActionIntent(pendingIntent, count)
                    )
                }
                Utils.PushType.PUSH_TYPE_DEFAULT_MULTILINE -> {
                    Utils.generateDefaultMultiLineNotification(
                        notificationBuilder,
                        downloadedBitmap,
                        notificationInfo.message
                    )
                    setPushAction(
                        notificationInfo.actionText,
                        notificationBuilder,
                        getActionIntent(pendingIntent, count)
                    )
                }
                Utils.PushType.NOTIFICATION_TYPE_APPEND -> Utils.generateStackNotification(
                    this,
                    notificationBuilder,
                    notificationInfo.message
                )

                else -> Utils.generateDefaultNotification(
                    notificationBuilder,
                    downloadedBitmap
                )
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                val name = resources.getString(R.string.app_name)
                val mChannel = NotificationChannel(id, name, importance)
                notificationManager.createNotificationChannel(mChannel)
            }
            var notification = notificationBuilder.build()
            notification = Utils.setNotificationProperty(this, notification)
            try {
                notificationManager.notify(count, notification)
            } catch (e: Exception) {
                Log.e(TAG, "generateNotification() " + e.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "generateNotification() " + e.message)
        }
    }

    private fun setPushAction(
        actionText: String?,
        notificationBuilder: NotificationCompat.Builder,
        pendingIntent: PendingIntent
    ) {
        if (!TextUtils.isEmpty(actionText)) {
            val action =
                NotificationCompat.Action(0, actionText, pendingIntent)
            notificationBuilder.addAction(action)
        }
    }

    private fun getActionIntent(
        pendingIntent: PendingIntent?,
        notificationId: Int
    ): PendingIntent {
        val intent = Intent(this, NotificationReceiver::class.java)
        intent.action = applicationContext.packageName
        intent.putExtra("type", "action")
        intent.putExtra(Intent.EXTRA_INTENT, pendingIntent)
        intent.putExtra("id", notificationId)
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    private fun getDeleteIntent(): PendingIntent? {
        val intent = Intent(this, NotificationReceiver::class.java)
        intent.action = applicationContext.packageName
        intent.putExtra("type", "deletion")
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }
}