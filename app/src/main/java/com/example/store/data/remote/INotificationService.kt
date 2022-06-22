package com.example.store.data.remote

import com.example.store.data.model.product.ProductItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface INotificationService {
    @GET("products")
    suspend fun getNewProductList(
        @QueryMap hashMap: HashMap<String, String>
    ): Response<List<ProductItem>>
}