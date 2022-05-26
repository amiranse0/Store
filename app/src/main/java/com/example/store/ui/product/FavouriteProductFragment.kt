package com.example.store.ui.product

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.data.Result
import com.example.store.databinding.FragmentFavouriteProductBinding
import com.example.store.ui.viewmodels.FavouriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouriteProductFragment : Fragment(R.layout.fragment_favourite_product) {

    private lateinit var binding: FragmentFavouriteProductBinding
    private val viewModel by viewModels<FavouriteViewModel>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdaptor: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFavouriteProductBinding.bind(view)

        getListFavouriteProduct()

        goToDetail()

    }

    private fun goToDetail() {
        recyclerAdaptor.setToClickOnItem(object : ProductAdapter.ClickOnItem {
            override fun clickOnItem(position: Int, view: View?) {
                val item = recyclerAdaptor.oldList[position]
                val bundle = bundleOf(
                    "title" to item.name,
                    "images" to item.images.map { it.src },
                    "price" to item.price,
                    "description" to item.description,
                    "category" to item.categories.map { it.name },
                    "purchasable" to item.purchasable
                )
                findNavController().navigate(
                    R.id.action_favourite_product_menu_to_detailProductFragment,
                    bundle
                )
            }

        })
    }

    private fun getListFavouriteProduct() {
        recyclerView = binding.favouriteProductRc
        recyclerAdaptor = ProductAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recyclerAdaptor

        lifecycleScope.launch {
            viewModel.favouriteProductsStateFlow.collect {
                when (it) {
                    is Result.Error -> {
                        binding.favouriteProductPb.visibility = View.GONE
                        recyclerAdaptor.setData(emptyList())
                    }
                    is Result.Loading -> {
                        binding.favouriteProductPb.visibility = View.VISIBLE
                        recyclerAdaptor.setData(emptyList())
                    }
                    is Result.Success -> {
                        binding.favouriteProductPb.visibility = View.GONE
                        recyclerAdaptor.setData(it.data)
                    }
                }
            }
        }
    }
}