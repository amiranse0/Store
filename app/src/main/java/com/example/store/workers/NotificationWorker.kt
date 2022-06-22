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
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.store.MainActivity
import com.example.store.R
import com.example.store.data.Keys
import com.example.store.data.model.product.ProductItem
import com.example.store.data.remote.NotificationNetworkManager
import com.example.store.ui.product.detail.DetailProductFragmentArgs

class NotificationWorker(private val context: Context, workerParameters: WorkerParameters)
    :CoroutineWorker(context, workerParameters){

    companion object{
        const val CHANNEL_ID = "1"
        const val NOTIFICATION_ID = 1
    }

    override suspend fun doWork(): Result {

        val productItem = NotificationNetworkManager.service.getNewProductList(
            hashMapOf(
                "consumer_key" to Keys.consumerKey,
                "consumer_secret" to Keys.consumerSecret,
                "orderby" to "date"
            )
        )

        val sharedPreferences = applicationContext.getSharedPreferences("notif", Context.MODE_PRIVATE)

        val previousId = sharedPreferences.getString("id", "")?:""

        val editor = sharedPreferences.edit()

        if(productItem.isSuccessful){
            var item: List<ProductItem> = emptyList()
            productItem.body().let {
                it?.let { it1 -> item = it1 }
            }

            if (item.isNotEmpty() && previousId != id.toString()){
                editor.apply{
                    putString("id", item.first().id.toString())
                    apply()
                }

                Log.d("NOTIFICATION", "Message")
                showNotification(item.first())

                return Result.success()
            }
        }
        return Result.retry()

    }

    private fun showNotification(productItem: ProductItem) {
        val intent = Intent(
            applicationContext,
            MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setGraph(R.navigation.navigation_bottom_graph)
            .setDestination(R.id.detailProductFragment)
            .setArguments(DetailProductFragmentArgs(productItem).toBundle())
            .createPendingIntent()

        val notification = NotificationCompat.Builder(
            applicationContext, CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(context.getString(R.string.new_product_avail))
            .setContentText(context.getString(R.string.click))
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