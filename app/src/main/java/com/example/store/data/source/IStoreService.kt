package com.example.store.data.source

import com.example.store.data.model.category.CategoryItem
import com.example.store.data.model.product.ProductItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface IStoreService {

    @GET("products")
    suspend fun getProducts(@QueryMap hashMap: HashMap<String, String>): List<ProductItem>

    @GET("products/categories")
    suspend fun getCategories(@QueryMap hashMap: HashMap<String, String>): List<CategoryItem>

    @GET("products/{id}")
    suspend fun getSpecialOffers(@Path("id") id:String, @QueryMap hashMap: HashMap<String, String>): List<ProductItem>
}