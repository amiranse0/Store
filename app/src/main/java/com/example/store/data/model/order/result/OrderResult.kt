package com.example.store.data.model.order.result

import com.google.gson.annotations.SerializedName as SN

data class OrderResult(
    val coupon_lines: List<Any>,
    val currency: String,
    val currency_symbol: String,
    val customer_id: Int,
    val customer_ip_address: String,
    val customer_note: String,
    val customer_user_agent: String,
    val discount_tax: String,
    val discount_total: String,
    val fee_lines: List<Any>,
    val id: Int,
    @SN("line_items")
    val lineItems: List<ResultLineItem>,
    val meta_data: List<Any>,
    val number: String,
    val order_key: String,
    val parent_id: Int,
    val payment_method: String,
    val payment_method_title: String,
    val prices_include_tax: Boolean,
    val refunds: List<Any>,
    val status: String,
    val tax_lines: List<Any>,
    val total: String,
    val total_tax: String,
    val transaction_id: String,
    val version: String
)