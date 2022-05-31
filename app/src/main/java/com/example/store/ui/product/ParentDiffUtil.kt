package com.example.store.ui.product

import androidx.recyclerview.widget.DiffUtil
import com.example.store.data.model.product.ProductItem

class ParentDiffUtil(
    private val oldList: HashMap<String, List<ProductItem>>,
    private val newList: HashMap<String, List<ProductItem>>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPos = when(oldItemPosition){
            0 -> "best"
            1 -> "latest"
            2 -> "favourite"
            else -> "nothing"
        }
        val newPos = when(newItemPosition){
            0 -> "best"
            1 -> "latest"
            2 -> "favourite"
            else -> "nothing"
        }
        return oldList[oldPos] == newList[newPos]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPos = when(oldItemPosition){
            0 -> "best"
            1 -> "latest"
            2 -> "favourite"
            else -> "nothing"
        }
        val newPos = when(newItemPosition){
            0 -> "best"
            1 -> "latest"
            2 -> "favourite"
            else -> "nothing"
        }
        return when{
            oldList[oldPos] != newList[newPos] -> false
            oldPos != newPos -> false
            else -> true
        }
    }

}