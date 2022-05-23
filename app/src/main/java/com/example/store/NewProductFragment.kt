package com.example.store

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.store.databinding.FragmentNewProductBinding
import com.example.store.viewmodels.NewestProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewProductFragment:Fragment(R.layout.fragment_new_product) {

    private lateinit var binding: FragmentNewProductBinding
    private val viewModel by viewModels<NewestProductViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNewProductBinding.bind(view)

        lifecycleScope.launch {
            viewModel.newestProductsStateFlow.collect{
                binding.tv.text = it.toString()
            }
        }
    }
}