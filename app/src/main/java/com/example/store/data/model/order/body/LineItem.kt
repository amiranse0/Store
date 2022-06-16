package com.example.store.data.model.order.body

import com.google.gson.annotations.SerializedName as SN

data class LineItem(
    @SN("product_id")
    val productId: Int,
    val quantity: Int,
    @SN("variation_id")
    val variationId: Int,
    @SN("price")
    val price:Int,
//    @SN("coupon_lines")
//    val couponLine: List<Coupon>
)