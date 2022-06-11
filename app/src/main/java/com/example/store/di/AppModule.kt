package com.example.store.di

import com.example.store.data.remote.DataSource
import com.example.store.data.remote.IStoreService
import com.example.store.data.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RemoteProductDataSource

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://woocommerce.maktabsharif.ir/wp-json/wc/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideClient())
        .build()


    @Provides
    @Singleton
    fun provideInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideClient() = OkHttpClient.Builder()
        .addInterceptor(provideInterceptor())
        .build()

    @Singleton
    @Provides
    fun provideIStoreService(): IStoreService = provideRetrofit().create(IStoreService::class.java)

    @Singleton
    @RemoteProductDataSource
    @Provides
    fun provideRemoteDataSource(): DataSource = RemoteDataSource(provideIStoreService())

}