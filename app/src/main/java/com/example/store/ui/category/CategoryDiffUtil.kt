package com.example.store.ui.category

import androidx.recyclerview.widget.DiffUtil
import com.example.store.data.model.category.CategoryItem

class CategoryDiffUtil(
    private val oldList: List<CategoryItem>,
    private val newList: List<CategoryItem>,

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
            oldList[oldItemPosition].description != newList[newItemPosition].description -> false
            oldList[oldItemPosition]._links != newList[newItemPosition]._links -> false
            else -> true
        }
    }
}