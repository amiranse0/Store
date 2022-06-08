package com.example.store.ui.product.home.slider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.store.data.model.product.ProductItem
import com.example.store.databinding.SpecialOffersCardViewBinding
import com.example.store.ui.product.ProductDiffUtil

class SpecialOffersAdaptor(private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<SpecialOffersAdaptor.ImageViewHolder>() {

    var images: List<String> = emptyList()

    inner class ImageViewHolder(private val binding: SpecialOffersCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pos: Int) {
            Glide.with(binding.root)
                .load(images[pos])
                .into(binding.specialOffersIv)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = SpecialOffersCardViewBinding.inflate(inflater)

        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(position)
        if (position == images.size - 1){

        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

}