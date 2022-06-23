package com.example.store.data.model.coupons

import com.google.gson.annotations.SerializedName as SN

data class CouponItem(
    val amount: String,
    val code: String,
    @SN("discount_type")
    val discountType: String,
    val id: Int,
    @SN("maximum_amount")
    val maximumAmount: String,
    @SN("minimum_amount")
    val minimumAmount: String,
)