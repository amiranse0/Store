package com.example.store.ui.product.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.store.R
import com.example.store.data.model.product.ProductItem
import com.example.store.databinding.HomeCardViewItemBinding

class MainRowHomeAdaptor(private val items: List<ProductItem>) :
    RecyclerView.Adapter<MainRowHomeAdaptor.ViewHolder>() {

    private lateinit var clickOnItem: MainRowHomeAdaptor.ClickOnItem

    inner class ViewHolder(private val binding: HomeCardViewItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(position: Int){
            Glide.with(binding.root)
                .load(items[position].images.first().src)
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(binding.homeItemIv)
        }

        override fun onClick(p0: View?) {
            clickOnItem.clickOnItem(adapterPosition, p0)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = HomeCardViewItemBinding.inflate(inflater)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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