package com.example.store

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.store.databinding.FragmentFavouriteProductBinding
import com.example.store.viewmodels.FavouriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouriteProductFragment : Fragment(R.layout.fragment_favourite_product) {

    private lateinit var binding: FragmentFavouriteProductBinding
    private val viewModel by viewModels<FavouriteViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFavouriteProductBinding.bind(view)

        lifecycleScope.launch {
            viewModel.favouriteProductsStateFlow.collect{
                binding.tv.text = it.toString()
            }
        }
    }
}