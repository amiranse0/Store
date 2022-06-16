package com.example.store.data.model.customer.result

import com.google.gson.annotations.SerializedName as SN

data class CustomerResult(
    val email: String,
    @SN("first_name")
    val firstName: String,
    val id: Int,
    @SN("is_paying_customer")
    val isPayingCustomer: Boolean,
    @SN("last_name")
    val lastName: String,
    @SN("meta_data")
    val metaData: List<Any>,
    val role: String,
    val username: String
)