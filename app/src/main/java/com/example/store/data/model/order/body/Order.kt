package com.example.store.data.model.order.body

import com.google.gson.annotations.SerializedName as SN

data class Order(
    @SN("line_items")
    val lineItems: List<LineItem>,
    @SN("payment_method")
    val paymentMethod: String = "bacs",
    @SN("payment_method_title")
    val paymentMethodTitle: String = "Direct Bank Transfer",
    @SN("set_paid")
    val setPaid: Boolean = false,
    @SN("customer_id")
    val customerId: Int
)