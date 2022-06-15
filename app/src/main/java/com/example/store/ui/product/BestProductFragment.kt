package com.example.store.ui.product

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.data.Result
import com.example.store.data.model.product.ProductItem
import com.example.store.databinding.FragmentBestProductBinding
import com.example.store.ui.viewmodels.BestProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BestProductFragment : Fragment(R.layout.fragment_best_product) {

    private lateinit var binding: FragmentBestProductBinding

    private lateinit var recyclerView: RecyclerView

    private lateinit var recyclerAdaptor: ProductAdapter

    private val viewModel by viewModels<BestProductViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBestProductBinding.bind(view)

        getListBestProduct()
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
                findNavController().navigate(R.id.action_bestProductFragment_to_detailProductFragment, bundle)
            }

        })
    }

    private fun getListBestProduct() {
        recyclerView = binding.bestProductRc
        recyclerAdaptor = ProductAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recyclerAdaptor

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.bestProductsStateFlow.collect {
                    when (it) {
                        is Result.Error -> {
                            binding.bestProductPb.visibility = View.GONE
                            recyclerAdaptor.setData(emptyList())
                        }
                        is Result.Loading -> {
                            binding.bestProductPb.visibility = View.VISIBLE
                            recyclerAdaptor.setData(emptyList())
                        }
                        is Result.Success -> {
                            binding.bestProductPb.visibility = View.GONE
                            recyclerAdaptor.setData(it.data)
                        }
                    }
                }
            }
        }
    }
}