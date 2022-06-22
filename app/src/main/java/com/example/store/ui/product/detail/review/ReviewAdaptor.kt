package com.example.store.ui.product.detail.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.store.R
import com.example.store.data.model.product.ProductItem
import com.example.store.data.model.reviews.result.ReviewItem
import com.example.store.databinding.ReviewCardViewBinding
import com.example.store.ui.product.ProductDiffUtil

class ReviewAdaptor :
    RecyclerView.Adapter<ReviewAdaptor.ViewHolder>() {

    var oldList: List<ReviewItem> = emptyList()

    inner class ViewHolder(private val binding: ReviewCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pos: Int) {
//            if(oldList[pos].reviewerAvatarUrls != ""){
//                Glide.with(binding.root)
//                    .load(oldList[pos].reviewerAvatarUrls)
//                    .placeholder(R.drawable.ic_baseline_image_24)
//                    .into(binding.iconUserIv)
//            }

            binding.ratingTv.text = oldList[pos].rating.toString()
            binding.reviewTimeTv.text = oldList[pos].dateCreated.split("T".toRegex()).first()
                .replace("-", "/")
            binding.reviewTv.text = oldList[pos].review.replace("<[^>]*>".toRegex(), "")
            binding.reviewerNameTv.text = oldList[pos].reviewer
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ReviewCardViewBinding.inflate(inflater)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = oldList.size

    fun setData(newList: List<ReviewItem>) {
        val diffUtil = ReviewDiffUtil(oldList, newList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldList = newList
        diffResults.dispatchUpdatesTo(this)
    }
}