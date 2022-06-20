package com.example.store.ui.cart

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.store.databinding.CartCardViewBinding
import com.example.store.databinding.ProductCardViewBinding

class CartAdaptor: RecyclerView.Adapter<CartAdaptor.MyViewHolder>() {

    inner class MyViewHolder(private val binding: CartCardViewBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        init {
            binding.increaseDecreaseLayoutIncluded.decreaseBtnCart.setOnClickListener(this)
            binding.increaseDecreaseLayoutIncluded.increaseBtnCart.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}