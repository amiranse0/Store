package com.example.store.data.model.order.body

import com.google.gson.annotations.SerializedName as SN

data class Order(
    val billing: Billing,
    val line_items: List<LineItem>,
    val payment_method: String,
    val payment_method_title: String,
    val set_paid: Boolean,
    val shipping: Shipping,
    val shipping_lines: List<ShippingLine>
)