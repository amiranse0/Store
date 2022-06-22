package com.example.store.data.remote

import com.example.store.data.model.category.CategoryItem
import com.example.store.data.model.customer.body.Customer
import com.example.store.data.model.customer.result.CustomerResult
import com.example.store.data.model.order.body.Order
import com.example.store.data.model.order.result.OrderResult
import com.example.store.data.model.product.ProductItem
import com.example.store.data.model.reviews.body.ReviewBody
import com.example.store.data.model.reviews.result.ReviewItem
import retrofit2.http.*

interface IStoreService {

    @GET("products")
    suspend fun getProducts(@QueryMap hashMap: HashMap<String, String>): List<ProductItem>

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: String,
        @QueryMap hashMap: HashMap<String, String>
    ): ProductItem

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
        @Path("id") id: String,
        @QueryMap hashMap: HashMap<String, String>,
        @Body customer: Customer
    ): CustomerResult

    @GET("customers")
    suspend fun getCustomer(@QueryMap hashMap: HashMap<String, String>): List<CustomerResult>

    @POST("orders")
    suspend fun createOrder(
        @QueryMap hashMap: HashMap<String, String>,
        @Body order: Order
    ): OrderResult

    @PUT("orders/{id}")
    suspend fun updateOrder(
        @Path("id") id: String,
        @QueryMap hashMap: HashMap<String, String>,
        @Body order: Order
    ): OrderResult

    @DELETE("orders/{id}")
    suspend fun deleteOrder(
        @Path("id") id: String,
        @QueryMap hashMap: HashMap<String, String>
    )

    @GET("orders/{id}")
    suspend fun getOrder(
        @Path("id") id: String,
        @QueryMap hashMap: HashMap<String, String>
    ): OrderResult

    @GET("products/reviews")
    suspend fun getReviews(
        @QueryMap hashMap: HashMap<String, String>
    ): List<ReviewItem>

    @POST("products/reviews")
    suspend fun setReviews(
        @QueryMap hashMap: HashMap<String, String>,
        @Body body: ReviewBody
    ): ReviewItem
}