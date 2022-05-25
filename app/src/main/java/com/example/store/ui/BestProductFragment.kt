package com.example.store.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.store.R
import com.example.store.databinding.FragmentBestProductBinding
import com.example.store.ui.viewmodels.BestProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BestProductFragment:Fragment(R.layout.fragment_best_product) {

    private lateinit var binding:FragmentBestProductBinding

    private val viewModel by viewModels<BestProductViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBestProductBinding.bind(view)

        lifecycleScope.launch {
            viewModel.bestProductsStateFlow.collect{
                binding.tv.text = it.toString()
            }
        }

    }
}