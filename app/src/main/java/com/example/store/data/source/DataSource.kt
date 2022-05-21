package com.example.store.data.source

import com.example.store.data.model.ProductItem

interface DataSource {
    suspend fun getLatestProducts(page: Int) : List<ProductItem>
    suspend fun getFavouriteProducts(page: Int) : List<ProductItem>
    suspend fun getBestProducts(page: Int) : List<ProductItem>
}