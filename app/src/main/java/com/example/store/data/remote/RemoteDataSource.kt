package com.example.store.data.remote

import com.example.store.data.Keys
import com.example.store.data.model.category.CategoryItem
import com.example.store.data.model.customer.body.Customer
import com.example.store.data.model.customer.result.CustomerResult
import com.example.store.data.model.order.body.Order
import com.example.store.data.model.order.result.OrderResult
import com.example.store.data.model.product.ProductItem

class RemoteDataSource(
    private val service: IStoreService
) : DataSource {
    private val query =
        hashMapOf(
            "consumer_key" to Keys.consumerKey,
            "consumer_secret" to Keys.consumerSecret
        )

    override suspend fun getLatestProducts(page: Int, perPage: Int): List<ProductItem> {
        val latestQuery = query
        latestQuery["orderby"] = "date"
        latestQuery["page"] = "$page"
        latestQuery["per_page"] = "$perPage"
        return service.getProducts(latestQuery)
    }

    override suspend fun getFavouriteProducts(page: Int, perPage: Int): List<ProductItem> {
        val favouriteQuery = query
        favouriteQuery["orderby"] = "popularity"
        favouriteQuery["page"] = "$page"
        favouriteQuery["per_page"] = "$perPage"
        return service.getProducts(favouriteQuery)
    }

    override suspend fun getBestProducts(page: Int, perPage: Int): List<ProductItem> {
        val bestQuery = query
        bestQuery["orderby"] = "rating"
        bestQuery["page"] = "$page"
        bestQuery["per_page"] = "$perPage"
        return service.getProducts(bestQuery)
    }

    override suspend fun getProduct(id: String): ProductItem {
        return service.getProduct(id, query)
    }

    override suspend fun getCategories(): List<CategoryItem> {
        return service.getCategories(
            hashMapOf(
                "consumer_key" to Keys.consumerKey,
                "consumer_secret" to Keys.consumerSecret
            )
        )
    }

    override suspend fun getSomeCategory(page: Int, category: String): List<ProductItem> {
        val queryForCategory =
            hashMapOf(
                "consumer_key" to Keys.consumerKey,
                "consumer_secret" to Keys.consumerSecret
            )
        queryForCategory["page"] = "$page"
        queryForCategory["category"] = category
        queryForCategory["per_page"] = "20"
        return service.getProducts(queryForCategory)
    }

    override suspend fun searchQuery(perPage: Int, searchQuery: String): List<ProductItem> {
        val query =
            hashMapOf(
                "consumer_key" to Keys.consumerKey,
                "consumer_secret" to Keys.consumerSecret
            )
        query["search"] = searchQuery
        query["perPage"] = "$perPage"
        return service.getProducts(query)
    }

    override suspend fun sortAndFilter(
        perPage: Int, searchQuery: String, sort: String, lowerPrice: String,
        higherPrice: String,
        categoryId: Int
    ): List<ProductItem> {
        val query = hashMapOf(
            "consumer_key" to Keys.consumerKey,
            "consumer_secret" to Keys.consumerSecret
        )
        query["search"] = searchQuery
        query["perPage"] = "$perPage"
        when (sort) {
            "date" -> query["orderby"] = "date"
            "cheap" -> {
                query["orderby"] = "price"
                query["order"] = "asc"
            }
            "expensive" -> query["orderby"] = "price"
            "rating" -> query["orderby"] = "rating"
            "popularity" -> query["orderby"] = "popularity"
        }

        if (lowerPrice != "")
            query["min_price"] = lowerPrice
        if (higherPrice != "")
            query["max_price"] = higherPrice
        if (categoryId != 0)
            query["category"] = categoryId.toString()

        return service.getProducts(query)
    }

    override suspend fun getSpecialOffers(): ProductItem {
        val query = hashMapOf(
            "consumer_key" to Keys.consumerKey,
            "consumer_secret" to Keys.consumerSecret
        )
        return service.getSpecialOffers("608", query)
    }

    override suspend fun createCustomer(customer: Customer): CustomerResult {
        val query = hashMapOf(
            "consumer_key" to Keys.consumerKey,
            "consumer_secret" to Keys.consumerSecret
        )
        return service.createCustomer(query, customer)
    }

    override suspend fun updateCustomer(customer: Customer, id: String): CustomerResult {
        val query = hashMapOf(
            "consumer_key" to Keys.consumerKey,
            "consumer_secret" to Keys.consumerSecret
        )
        return service.updateCustomer(id, query, customer)
    }

    override suspend fun getCustomer(email: String): List<CustomerResult> {
        val query = hashMapOf(
            "consumer_key" to Keys.consumerKey,
            "consumer_secret" to Keys.consumerSecret
        )
        query["email"] = email
        return service.getCustomer(query)
    }

    override suspend fun createOrder(order: Order): OrderResult {
        val query = hashMapOf(
            "consumer_key" to Keys.consumerKey,
            "consumer_secret" to Keys.consumerSecret
        )
        return service.createOrder(query, order)
    }

    override suspend fun updateOrder(order: Order, id: String): OrderResult {
        val query = hashMapOf(
            "consumer_key" to Keys.consumerKey,
            "consumer_secret" to Keys.consumerSecret
        )
        return service.updateOrder(id, query, order)
    }

    override suspend fun deleteOrder(id: String) {
        val query = hashMapOf(
            "consumer_key" to Keys.consumerKey,
            "consumer_secret" to Keys.consumerSecret
        )
        service.deleteOrder(id, query)
    }

    override suspend fun getOrder(id: String): OrderResult {
        val query = hashMapOf(
            "consumer_key" to Keys.consumerKey,
            "consumer_secret" to Keys.consumerSecret
        )
        return service.getOrder(id, query)
    }

}