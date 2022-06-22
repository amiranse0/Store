package com.example.store.data.model.reviews.result

import com.google.gson.annotations.SerializedName as SN

data class ReviewerAvatarUrls(
    @SN("`24`")
    val image24: String,
    @SN("`48`")
    val image48: String,
    @SN("`96`")
    val image96: String
)