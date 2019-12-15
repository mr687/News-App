package me.sedaph.newsapp.Service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import com.pusher.pushnotifications.fcm.MessagingService
import me.sedaph.newsapp.R
import me.sedaph.newsapp.ui.activity.SplashActivity

class NotificationsMessagingService : MessagingService() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: NotificationCompat.Builder
    private val channelId = "me.sedaph.newsapp.Service"
    private val description = "News App Notification"

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = getSystemService(NotificationManager::class.java)!!
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId,description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i("NotificationsService", remoteMessage.notification!!.body!!)

        val intent = Intent(this, SplashActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this,channelId)
                .setContentTitle(remoteMessage.notification!!.title)
                .setContentText(remoteMessage.notification!!.body!!)
                .setSmallIcon(R.drawable.ic_android)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.ic_android))
                .setContentIntent(pendingIntent)
        }else{
            NotificationCompat.Builder(this)
                .setContentTitle(remoteMessage.notification!!.title)
                .setContentText(remoteMessage.notification!!.body!!)
                .setSmallIcon(R.drawable.ic_android)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.ic_android))
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1,builder.build())
    }

}