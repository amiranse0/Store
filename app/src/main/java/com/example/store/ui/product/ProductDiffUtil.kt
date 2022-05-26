package com.example.store.ui.product

import androidx.recyclerview.widget.DiffUtil
import com.example.store.data.model.product.ProductItem

class ProductDiffUtil(
    private val oldList: List<ProductItem>,
    private val newList: List<ProductItem>,

    ) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> false
            oldList[oldItemPosition].name != newList[newItemPosition].name -> false
            oldList[oldItemPosition].categories != newList[newItemPosition].categories -> false
            oldList[oldItemPosition]._links != newList[newItemPosition]._links -> false
            else -> true
        }
    }
}