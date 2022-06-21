package com.example.store.ui.cart

import androidx.recyclerview.widget.DiffUtil

class CartDiffUtil(
    private val oldList: List<CartItem>,
    private val newList: List<CartItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].id != newList[newItemPosition].id -> false
            oldList[oldItemPosition].quantity != newList[newItemPosition].quantity -> false
            oldList[oldItemPosition].productItem.id != newList[newItemPosition].productItem.id -> false
            else -> true
        }
    }

}