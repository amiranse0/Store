package com.example.store.data.model.category

data class CategoryItem(
    val count: Int,
    val description: String,
    val id: Int,
    val image: Image,
    val name: String,
    val parent: Int
)