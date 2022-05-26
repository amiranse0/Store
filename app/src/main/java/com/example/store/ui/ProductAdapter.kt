package com.example.store.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.store.data.model.product.ProductItem
import com.example.store.databinding.CustomeCardViewBinding


class ProductAdapter: RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {

    private var oldList: List<ProductItem> = emptyList()
    private lateinit var clickOnItem: ClickOnItem

    inner class MyViewHolder(private val binding: CustomeCardViewBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(position: Int) {
            binding.titleCardViewTv.text = oldList[position].name

            binding.cardViewIv.load("https://fantasy.premierleague.com/img/favicons/favicon-32x32.png")
            //binding.cardViewIv.load(oldList[position].images.first().src)
            binding.priceCardViewTv.text = oldList[position].price
        }

        override fun onClick(p0: View?) {
            clickOnItem.clickOnItem(adapterPosition, p0)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = CustomeCardViewBinding.inflate(inflater)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return oldList.size
    }

    interface ClickOnItem {
        fun clickOnItem(position: Int, view: View?)
    }

    fun setToClickOnItem(clickOnItem: ClickOnItem) {
        this.clickOnItem = clickOnItem
    }

    fun setData(newList: List<ProductItem>) {
        val diffUtil = ProductDiffUtil(oldList, newList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldList = newList
        diffResults.dispatchUpdatesTo(this)
    }
}