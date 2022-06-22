package com.example.store.data.model.reviews.result

import com.google.gson.annotations.SerializedName as SN

data class ReviewItem(
    @SN("date_created")
    val dateCreated: String,
    val id: Int,
    @SN("product_id")
    val productId: Int,
    val rating: Int,
    val review: String,
    val reviewer: String,
    @SN("reviewer_avatar_urls")
    val reviewerAvatarUrls: ReviewerAvatarUrls,
    @SN("reviewer_email")
    val reviewerEmail: String,
    val status: String,
    val verified: Boolean
)