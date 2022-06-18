package com.example.store.data.model.customer.body

import com.google.gson.annotations.SerializedName as SN

data class Customer(
    val billing: Billing = Billing(),
    val email: String,
    @SN("first_name")
    val firstName: String,
    @SN("last_name")
    val lastName: String,
    val shipping: Shipping = Shipping(),
    val username: String
)