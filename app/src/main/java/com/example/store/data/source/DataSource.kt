package com.example.store.data.source

import com.example.store.data.model.category.CategoryItem
import com.example.store.data.model.product.ProductItem

interface DataSource {
    suspend fun getLatestProducts(page: Int) : List<ProductItem>
    suspend fun getFavouriteProducts(page: Int) : List<ProductItem>
    suspend fun getBestProducts(page: Int) : List<ProductItem>
    suspend fun getCategories(): List<CategoryItem>
    suspend fun getSomeCategory(page: Int, category:String): List<ProductItem>
}