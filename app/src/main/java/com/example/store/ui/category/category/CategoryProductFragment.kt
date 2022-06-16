package com.example.store.ui.category.category

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.data.Result
import com.example.store.databinding.FragmentCategoryProductBinding
import com.example.store.ui.product.ProductAdapter
import com.example.store.ui.product.home.HomeFragmentDirections
import com.example.store.ui.viewmodels.BestProductViewModel
import com.example.store.ui.viewmodels.SpecificCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryProductFragment : Fragment(R.layout.fragment_category_product) {
    private lateinit var binding: FragmentCategoryProductBinding

    private lateinit var recyclerView: RecyclerView

    private lateinit var recyclerAdaptor: ProductAdapter

    private val viewModel by viewModels<SpecificCategoryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCategoryProductBinding.bind(view)

        getProducts()

        goToDetail()
    }

    private fun goToDetail() {
        recyclerAdaptor.setToClickOnItem(object : ProductAdapter.ClickOnItem {
            override fun clickOnItem(position: Int, view: View?) {
                val item = recyclerAdaptor.oldList[position]
                val action = CategoryProductFragmentDirections.actionCategoryProductFragmentToDetailProductFragment(item)

                if (view != null) {
                    Navigation.findNavController(view).navigate(action)
                }
            }

        })
    }

    private fun getProducts() {
        val category = arguments?.getString("category")
        if (category != null) {
            viewModel.getProducts(1, category)
        }

        recyclerView = binding.productRc
        recyclerAdaptor = ProductAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recyclerAdaptor

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productsStateFlow.collect {
                    when (it) {
                        is Result.Success -> {
                            binding.productPb.visibility = View.GONE
                            recyclerAdaptor.setData(it.data)
                        }
                        is Result.Error -> {
                            binding.productPb.visibility = View.GONE

                        }
                        is Result.Loading -> {
                            binding.productPb.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}