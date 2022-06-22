package com.example.store.data.remote

import com.example.store.data.Keys
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NotificationNetworkManager {

    var httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    private var retrofit = Retrofit.Builder()
        .baseUrl(Keys.baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val service: INotificationService = retrofit.create(INotificationService::class.java)
}