package com.example.store.data.model.order.body

data class LineItem(
    val product_id: Int,
    val quantity: Int,
    val variation_id: Int
)