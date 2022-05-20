package com.example.store.data.source

import com.example.store.data.model.ProductItem
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface IStoreService {

    @GET("product")
    suspend fun getProduct(@QueryMap hashMap: HashMap<String, String>): List<ProductItem>
}