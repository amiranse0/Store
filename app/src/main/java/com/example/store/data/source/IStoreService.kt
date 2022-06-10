package com.example.store.data.source

import com.example.store.data.model.category.CategoryItem
import com.example.store.data.model.customer.body.Customer
import com.example.store.data.model.customer.result.CustomerResult
import com.example.store.data.model.product.ProductItem
import retrofit2.http.*

interface IStoreService {

    @GET("products")
    suspend fun getProducts(@QueryMap hashMap: HashMap<String, String>): List<ProductItem>

    @GET("products/categories")
    suspend fun getCategories(@QueryMap hashMap: HashMap<String, String>): List<CategoryItem>

    @GET("products/{id}")
    suspend fun getSpecialOffers(
        @Path("id") id: String,
        @QueryMap hashMap: HashMap<String, String>
    ): ProductItem

    @POST("customers")
    suspend fun createCustomer(
        @QueryMap hashMap: HashMap<String, String>,
        @Body customer: Customer
    ): CustomerResult

    @PUT("customers/{id}")
    suspend fun updateCustomer(
        @QueryMap hashMap: HashMap<String, String>,
        @Path("id") id: String,
        @Body customer: Customer
    ): CustomerResult

    @GET("customers")
    suspend fun getCustomer(@QueryMap hashMap: HashMap<String, String>): List<CustomerResult>
}