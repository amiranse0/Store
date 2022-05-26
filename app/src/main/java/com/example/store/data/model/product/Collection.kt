package com.example.store.data.model.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Collection(
    val href: String
) : Parcelable