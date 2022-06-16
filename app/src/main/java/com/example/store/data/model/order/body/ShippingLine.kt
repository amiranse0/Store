package com.example.store.data.model.order.body

data class ShippingLine(
    val method_id: String,
    val method_title: String,
    val total: String
)