package com.example.store.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.data.Result
import com.example.store.databinding.FragmentNewProductBinding
import com.example.store.ui.viewmodels.NewestProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewProductFragment:Fragment(R.layout.fragment_new_product) {

    private lateinit var binding: FragmentNewProductBinding
    private val viewModel by viewModels<NewestProductViewModel>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdaptor: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNewProductBinding.bind(view)

        getListNewestProduct()
    }

    private fun getListNewestProduct() {
        recyclerView = binding.newestProductRc
        recyclerAdaptor = ProductAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recyclerAdaptor

        lifecycleScope.launch {
            viewModel.newestProductsStateFlow.collect {
                when (it) {
                    is Result.Error -> {
                        binding.newestProductPb.visibility = View.GONE
                        recyclerAdaptor.setData(emptyList())
                    }
                    is Result.Loading -> {
                        binding.newestProductPb.visibility = View.VISIBLE
                        recyclerAdaptor.setData(emptyList())
                    }
                    is Result.Success -> {
                        binding.newestProductPb.visibility = View.GONE
                        recyclerAdaptor.setData(it.data)
                    }
                }
            }
        }
    }
}