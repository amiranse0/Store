package com.example.store.ui.product.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store.data.model.product.ProductItem
import com.example.store.databinding.HomeCardViewMainRowBinding
import com.example.store.ui.product.ParentDiffUtil

class MainHomeAdaptor(private val context: Context):RecyclerView.Adapter<MainHomeAdaptor.ViewHolder>() {

    private var oldList: HashMap<String, List<ProductItem>> = hashMapOf()

    inner class ViewHolder(private val binding: HomeCardViewMainRowBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(position: Int){
            val name = when(position){
                0 -> "best"
                1 -> "latest"
                2 -> "favourite"
                else -> "nothing"
            }
            setRecyclerView(binding.rowRc, oldList[name]?: emptyList())
            Log.d("MAIN", position.toString())
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = HomeCardViewMainRowBinding.inflate(inflater)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return oldList.size
    }

    fun setRecyclerView(recyclerView: RecyclerView, items: List<ProductItem>){
        val innerAdaptor = MainRowHomeAdaptor(context)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = innerAdaptor

        innerAdaptor.setData(items)

    }

    fun setData(newList: HashMap<String, List<ProductItem>>){
        val diffUtil = ParentDiffUtil(oldList, newList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldList = newList
        diffResults.dispatchUpdatesTo(this)

    }
}