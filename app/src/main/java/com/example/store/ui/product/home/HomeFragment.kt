package com.example.store.ui.product.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.data.Result
import com.example.store.data.model.product.ProductItem
import com.example.store.databinding.HomeProductBinding
import com.example.store.ui.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_product) {
    private lateinit var binding: HomeProductBinding

    private lateinit var mainRecyclerView: RecyclerView

    private lateinit var mainAdapter: MainHomeAdaptor

    private val viewModel by viewModels<ProductViewModel>()

    private val metaData: HashMap<String, List<ProductItem>> = hashMapOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeProductBinding.bind(view)

        putItemsInRows()
        getLatestProduct()
        getBestProduct()
        getFavouriteProduct()

    }

    private fun putItemsInRows() {
        mainRecyclerView = binding.mainProductRc
        mainAdapter = MainHomeAdaptor(requireContext())
        mainRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        mainRecyclerView.adapter = mainAdapter

        mainAdapter.setData(metaData)
    }

    private fun getFavouriteProduct() {

        lifecycleScope.launch {
            viewModel.favouriteProductsStateFlow.collect {
                when (it) {
                    is Result.Error -> {
                    }
                    is Result.Loading -> {
                    }
                    is Result.Success -> {
                        metaData["favourite"] = it.data
                        mainAdapter.setData(metaData)
                    }
                }
            }
        }
    }

    private fun getBestProduct() {

        lifecycleScope.launch {
            viewModel.bestProductsStateFlow.collect {
                when (it) {
                    is Result.Error -> {
                    }
                    is Result.Loading -> {
                    }
                    is Result.Success -> {
                        metaData["best"] = it.data
                        mainAdapter.setData(metaData)
                    }
                }
            }
        }
    }

    private fun getLatestProduct() {

        lifecycleScope.launch {
            viewModel.lastProductsStateFlow.collect {
                when (it) {
                    is Result.Error -> {
                    }
                    is Result.Loading -> {
                    }
                    is Result.Success -> {
                        metaData["latest"] = it.data
                        mainAdapter.setData(metaData)
                    }
                }
            }
        }
    }
}