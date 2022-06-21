package com.example.store.ui.cart

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.store.R
import com.example.store.databinding.CartCardViewBinding


class CartAdaptor: RecyclerView.Adapter<CartAdaptor.MyViewHolder>() {

    var oldList: List<CartItem> = emptyList()

    inner class MyViewHolder(private val binding: CartCardViewBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        init {
            binding.increaseDecreaseLayoutIncluded.decreaseBtnCart.setOnClickListener(this)
            binding.increaseDecreaseLayoutIncluded.increaseBtnCart.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            Log.d("CART", "Clicked")
        }

        fun bind(position: Int){
            binding.increaseDecreaseLayoutIncluded.number = oldList[position].quantity
            val price = oldList[position].productItem.price.toInt() * oldList[position].quantity
            binding.priceCartView.text = "$price"
            binding.titleCartView.text = oldList[position].productItem.name

            if (oldList[position].productItem.images.isNotEmpty()){
                Glide.with(binding.root)
                    .load(oldList[position].productItem.images.first().src)
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .into(binding.cartViewIv)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = CartCardViewBinding.inflate(inflater)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = oldList.size

    fun setData(newList: List<CartItem>){
        val diffUtil = CartDiffUtil(oldList, newList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldList = newList
        diffResults.dispatchUpdatesTo(this)
    }
}