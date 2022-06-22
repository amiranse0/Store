package com.example.store.ui.product.detail.review

import androidx.recyclerview.widget.DiffUtil
import com.example.store.data.model.reviews.result.ReviewItem

class ReviewDiffUtil(
    private val oldList: List<ReviewItem>,
    private val newList: List<ReviewItem>
):DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].id != newList[newItemPosition].id -> false
            oldList[oldItemPosition].review != newList[newItemPosition].review -> false
            oldList[oldItemPosition].reviewer != newList[newItemPosition].reviewer -> false
            oldList[oldItemPosition].dateCreated != newList[newItemPosition].dateCreated -> false
            oldList[oldItemPosition].productId != newList[newItemPosition].productId -> false
            else -> true
        }
    }
}