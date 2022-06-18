package com.example.store.data.model.order.body

import com.google.gson.annotations.SerializedName as SN

data class LineItem(
    @SN("product_id")
    val productId: Int,
    val quantity: Int
)