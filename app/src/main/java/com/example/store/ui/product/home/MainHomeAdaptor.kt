package com.example.store.ui.product.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.data.model.product.ProductItem
import com.example.store.databinding.HomeCardViewMainRowBinding
import com.example.store.ui.product.ParentDiffUtil
import java.text.FieldPosition

class MainHomeAdaptor(private val context: Context, private val navController: NavController) :
    RecyclerView.Adapter<MainHomeAdaptor.ViewHolder>() {

    var oldList: HashMap<String, List<ProductItem>> = hashMapOf()

    inner class ViewHolder(val binding: HomeCardViewMainRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindPosToName(position: Int) = when (position) {
            0 -> "latest"
            1 -> "best"
            2 -> "favourite"
            else -> "nothing"
        }

        fun bind(position: Int) {
            val name = bindPosToName(position)

            binding.rowTitleTv.text = when (name) {
                "best" -> "جدیدترین محصولات"
                "latest" -> "بهترین محصولات"
                "favourite" -> "محبوب ترین محصولات"
                else -> "هیچی"
            }
        }

        fun goToAllProducts(position: Int) {
            binding.rowButton.setOnClickListener {
                when(position){
                    0 -> navController.navigate(R.id.action_homeFragment_to_newProductFragment)
                    1 -> navController.navigate(R.id.action_homeFragment_to_bestProductFragment)
                    2 -> navController.navigate(R.id.action_homeFragment_to_favouriteProductFragment)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = HomeCardViewMainRowBinding.inflate(inflater)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
        holder.goToAllProducts(position)
        setRecyclerView(holder.binding.rowRc, oldList[holder.bindPosToName(position)] ?: emptyList())

    }

    override fun getItemCount(): Int {
        return oldList.size
    }

    fun setRecyclerView(recyclerView: RecyclerView, items: List<ProductItem>) {
        val innerAdaptor: MainRowHomeAdaptor = MainRowHomeAdaptor(context)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = innerAdaptor

        innerAdaptor.setData(items)

    }

    fun setData(newList: HashMap<String, List<ProductItem>>) {
        val diffUtil = ParentDiffUtil(oldList, newList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldList = newList
        diffResults.dispatchUpdatesTo(this)

        Log.d("DATA", oldList.map { it.value.first().id }.toString())

    }
}