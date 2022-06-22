package com.example.store.data.remote

import com.example.store.data.model.category.CategoryItem
import com.example.store.data.model.customer.body.Customer
import com.example.store.data.model.customer.result.CustomerResult
import com.example.store.data.model.order.body.Order
import com.example.store.data.model.order.result.OrderResult
import com.example.store.data.model.product.ProductItem

interface DataSource {
    suspend fun getLatestProducts(page: Int, perPage: Int): List<ProductItem>
    suspend fun getFavouriteProducts(page: Int, perPage: Int): List<ProductItem>
    suspend fun getBestProducts(page: Int, perPage: Int): List<ProductItem>
    suspend fun getNewAddedProduct(): ProductItem

    suspend fun getProduct(id:String): ProductItem

    suspend fun getCategories(): List<CategoryItem>
    suspend fun getSomeCategory(page: Int, category: String): List<ProductItem>

    suspend fun searchQuery(perPage: Int, searchQuery: String): List<ProductItem>
    suspend fun sortAndFilter(
        perPage: Int,
        searchQuery: String,
        sort: String,
        lowerPrice: String,
        higherPrice: String,
        categoryId: Int
    ): List<ProductItem>

    suspend fun getSpecialOffers(): ProductItem

    suspend fun createCustomer(customer: Customer): CustomerResult
    suspend fun updateCustomer(customer: Customer, id: String): CustomerResult
    suspend fun getCustomer(email: String): List<CustomerResult>

    suspend fun createOrder(order: Order): OrderResult
    suspend fun updateOrder(order: Order, id: String): OrderResult
    suspend fun deleteOrder(id: String)
    suspend fun getOrder(id: String): OrderResult
}