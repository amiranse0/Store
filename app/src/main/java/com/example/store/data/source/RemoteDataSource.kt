package com.example.store.data.source

import com.example.store.data.Keys
import com.example.store.data.model.category.CategoryItem
import com.example.store.data.model.product.ProductItem

class RemoteDataSource(
    private val service: IStoreService
) : DataSource {
    private val query =
        hashMapOf(
            "consumer_key" to Keys.consumerKey,
            "consumer_secret" to Keys.consumerSecret
        )

    override suspend fun getLatestProducts(page: Int): List<ProductItem> {
        val latestQuery = query
        latestQuery["orderby"] = "date"
        latestQuery["page"] = "$page"
        latestQuery["per_page"] = "20"
        return service.getProducts(latestQuery)
    }

    override suspend fun getFavouriteProducts(page: Int): List<ProductItem> {
        val favouriteQuery = query
        favouriteQuery["orderby"] = "popularity"
        favouriteQuery["page"] = "$page"
        favouriteQuery["per_page"] = "20"
        return service.getProducts(favouriteQuery)
    }

    override suspend fun getBestProducts(page: Int): List<ProductItem> {
        val bestQuery = query
        bestQuery["orderby"] = "rating"
        bestQuery["page"] = "$page"
        bestQuery["per_page"] = "20"
        return service.getProducts(bestQuery)
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
}