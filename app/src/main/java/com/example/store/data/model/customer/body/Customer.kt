package com.example.store.data.model.customer.body

data class Customer(
    val billing: Billing,
    val email: String,
    val first_name: String,
    val last_name: String,
    val shipping: Shipping,
    val username: String
)