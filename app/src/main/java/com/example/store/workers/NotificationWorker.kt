package com.example.store.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.store.MainActivity
import com.example.store.R

class NotificationWorker(context: Context, workerParameters: WorkerParameters)
    :Worker(context, workerParameters){

    companion object{
        const val CHANNEL_ID = "id"
        const val NOTIFICATION_ID = 1
    }

    override fun doWork(): Result {

        showNotification()

        Log.d("NOTIFICATION", "message")

        return Result.success()
    }

    private fun showNotification() {
        val intent = Intent(
            applicationContext,
            MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent, 0
        )

        val notification = NotificationCompat.Builder(
            applicationContext, CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("محصولات جدید رسید.")
            .setContentText("کلیک کن!")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelName = "channel name"
            val channelDescription = "channel description"
            val channelPriority = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, channelPriority).apply {
                description = channelDescription
            }

            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(applicationContext)){
            notify(NOTIFICATION_ID, notification.build())
        }
    }
}