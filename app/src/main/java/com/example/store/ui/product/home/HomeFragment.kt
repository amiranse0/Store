package com.example.store.ui.product.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.data.Result
import com.example.store.data.model.product.ProductItem
import com.example.store.databinding.HomeProductBinding
import com.example.store.ui.product.ProductAdapter
import com.example.store.ui.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_product) {
    private lateinit var binding: HomeProductBinding

    private lateinit var mainRecyclerView: RecyclerView

    private lateinit var mainAdapter: MainHomeAdaptor

    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchAdapter: ProductAdapter

    private val viewModel by viewModels<HomeViewModel>()

    private val metaData: HashMap<String, List<ProductItem>> = hashMapOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeProductBinding.bind(view)

        putItemsInRows()
        getLatestProduct()
        getBestProduct()
        getFavouriteProduct()

        search()
        goToDetailFromSearch()
    }

    private fun search() {
        searchRecyclerView = binding.searchRc
        searchAdapter = ProductAdapter()
        searchRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        searchRecyclerView.adapter = searchAdapter

        binding.homeSv.setOnSearchClickListener {
            binding.mainHomeLayout.visibility = View.GONE
            binding.searchResultLayout.visibility = View.VISIBLE
        }

        binding.homeSv.setOnCloseListener {
            binding.mainHomeLayout.visibility = View.VISIBLE
            binding.searchResultLayout.visibility = View.GONE
            false
        }

        binding.homeSv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

                findNavController().navigate(
                    R.id.action_homeFragment_to_searchResultFragment,
                    bundleOf("query" to p0)
                )

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                lifecycleScope.launch {
                    if (p0 != null) {
                        viewModel.search(1, p0).collect {
                            if (it is Result.Success) Log.d("SEARCH", "${it.data}")
                            when (it) {
                                is Result.Error -> {

                                }
                                is Result.Loading -> {

                                }
                                is Result.Success -> {
                                    searchAdapter.setData(it.data)
                                }
                            }
                        }
                    }
                }

                return false
            }

        })
    }

    private fun goToDetailFromSearch() {
        searchAdapter.setToClickOnItem(object : ProductAdapter.ClickOnItem {
            override fun clickOnItem(position: Int, view: View?) {
                val item = searchAdapter.oldList[position]
                val bundle = bundleOf(
                    "title" to item.name,
                    "images" to item.images.map { it.src },
                    "price" to item.price,
                    "description" to item.description,
                    "category" to item.categories.map { it.name },
                    "purchasable" to item.purchasable
                )
                findNavController().navigate(
                    R.id.action_homeFragment_to_detailProductFragment,
                    bundle
                )
            }

        })
    }

    private fun putItemsInRows() {
        mainRecyclerView = binding.mainProductRc
        mainAdapter = MainHomeAdaptor(requireContext(), findNavController())
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