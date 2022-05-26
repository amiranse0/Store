package com.example.store.data.model.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Links(
    val collection: List<Collection>,
    val self: List<Self>
) : Parcelable