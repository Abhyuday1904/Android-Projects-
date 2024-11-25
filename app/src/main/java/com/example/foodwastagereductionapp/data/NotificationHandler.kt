package com.example.foodwastagereductionapp.data

import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.example.foodwastagereductionapp.R
import kotlin.random.Random

class NotificationHandler(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "notification_channel_id"


    fun showExpandedNotificationWithBigPicture(
        title: String,
        description: String,
        bitmap: Bitmap
    ) {
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.geeta_residency)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)

            )
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }

    private fun Context.bitmapFromResource(
        @DrawableRes resId: Int
    ) = BitmapFactory.decodeResource(
        resources,
        resId
    )
}