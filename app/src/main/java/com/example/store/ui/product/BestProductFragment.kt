package com.example.store.ui.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.data.Result
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

    }

    private fun getListBestProduct() {
        recyclerView = binding.bestProductRc
        recyclerAdaptor = ProductAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recyclerAdaptor

        lifecycleScope.launch {
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