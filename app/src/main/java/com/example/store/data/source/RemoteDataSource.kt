package com.example.store.data.source

import com.example.store.data.Keys
import com.example.store.data.model.ProductItem

class RemoteDataSource(
    private val service: IStoreService
) : DataSource {
    private val query =
        hashMapOf(
            "consumer_key" to Keys.consumerKey,
            "consumer_secret" to Keys.consumerSecret,
            "per_page" to "20"
        )

    override suspend fun getLatestProducts(page: Int): List<ProductItem> {
        val latestQuery = query
        latestQuery["orderby"] = "date"
        latestQuery["order"] = "desc"
        latestQuery["page"] = "$page"
        return service.getProduct(latestQuery)
    }

    override suspend fun getFavouriteProducts(page: Int): List<ProductItem> {
        val favouriteQuery = query
        favouriteQuery["orderby"] = "popularity"
        favouriteQuery["order"] = "desc"
        favouriteQuery["page"] = "$page"
        return service.getProduct(favouriteQuery)
    }

    override suspend fun getBestProducts(page: Int): List<ProductItem> {
        val bestQuery = query
        bestQuery["orderby"] = "rating"
        bestQuery["order"] = "desc"
        bestQuery["page"] = "$page"
        return service.getProduct(bestQuery)
    }
}