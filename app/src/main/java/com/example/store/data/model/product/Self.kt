package com.example.store.data.model.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Self(
    val href: String
) : Parcelable