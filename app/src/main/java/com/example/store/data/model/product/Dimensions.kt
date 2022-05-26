package com.example.store.data.model.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dimensions(
    val height: String,
    val length: String,
    val width: String
) : Parcelable