package com.example.store.ui.product.home.search

import android.os.Bundle
import android.util.Log
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
import com.example.store.databinding.FragmentResultSearchBinding
import com.example.store.ui.product.ProductAdapter
import com.example.store.ui.viewmodels.SearchResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchResultFragment : Fragment(R.layout.fragment_result_search) {

    private lateinit var binding: FragmentResultSearchBinding
    private val viewModel by viewModels<SearchResultViewModel>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdaptor: ProductAdapter

    private val query = arguments?.getString("query") ?: ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentResultSearchBinding.bind(view)


        getItemsFromSearch()

        goToDetail()

        sortResult()
    }

    private fun sortResult() {
        binding.sortLayout.setOnClickListener {
            val orderDialogFragment = OrderDialogFragment()
            val args = bundleOf("Query" to query)
            orderDialogFragment.arguments = args
            orderDialogFragment.show(parentFragmentManager, "order")
        }
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
                findNavController().navigate(R.id.action_searchResultFragment_to_detailProductFragment, bundle)
            }

        })
    }

    private fun getItemsFromSearch() {
        recyclerView = binding.resultRc
        recyclerAdaptor = ProductAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recyclerAdaptor

        lifecycleScope.launch {
            Log.d("QUERY", query)
            viewModel.resultSearch(query).collect {
                when (it) {
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        recyclerAdaptor.setData(it.data)
                    }
                    is Result.Error -> {

                    }
                }
            }
        }
    }
}