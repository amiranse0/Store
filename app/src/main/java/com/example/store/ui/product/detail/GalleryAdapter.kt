package com.example.store.ui.product.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.store.R
import com.example.store.databinding.GalleryCardViewBinding
import java.util.ArrayList

class GalleryAdapter(private val items: List<String>) :RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {

    private lateinit var clickOnItem: ClickOnItem

    inner class MyViewHolder(private val binding: GalleryCardViewBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(position: Int) {

            Glide.with(binding.root)
                .load(items[position])
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(binding.galleryCardViewIv)
        }

        override fun onClick(p0: View?) {
            clickOnItem.clickOnItem(adapterPosition, p0)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = GalleryCardViewBinding.inflate(inflater)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface ClickOnItem {
        fun clickOnItem(position: Int, view: View?)
    }

    fun setToClickOnItem(clickOnItem: ClickOnItem) {
        this.clickOnItem = clickOnItem
    }
}