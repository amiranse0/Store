package com.example.store.data.source

import com.example.store.data.model.category.CategoryItem
import com.example.store.data.model.product.ProductItem
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface IStoreService {

    @GET("product")
    suspend fun getProducts(@QueryMap hashMap: HashMap<String, String>): List<ProductItem>

    @GET("categories")
    suspend fun getCategories(@QueryMap hashMap: HashMap<String, String>): List<CategoryItem>
}