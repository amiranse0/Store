package com.example.store.data.model.order.result

import com.google.gson.annotations.SerializedName as SN

data class ResultLineItem(
    val id: Int,
    val name: String,
    val price: Int,
    @SN("product_id")
    val productId: Int,
    val quantity: Int,
    val total: String,
    @SN("variation_id")
    val variationId: Int
)