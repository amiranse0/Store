package com.example.store.data.model.reviews.body

import com.google.gson.annotations.SerializedName as SN

data class ReviewBody(
    @SN("product_id")
    val productId: Int,
    val rating: Int,
    val review: String,
    val reviewer: String,
    @SN("reviewer_email")
    val reviewerEmail: String
)