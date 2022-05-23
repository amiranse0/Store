package com.example.store.data.model.category

data class CategoryItem(
    val _links: Links,
    val count: Int,
    val description: String,
    val display: String,
    val id: Int,
    val image: List<Any>,
    val menu_order: Int,
    val name: String,
    val parent: Int,
    val slug: String
)