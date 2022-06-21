package com.example.store.ui.cart

import com.example.store.data.model.product.ProductItem

data class CartItem(
    val id: Int,
    val quantity: Int,
    val productItem: ProductItem
)