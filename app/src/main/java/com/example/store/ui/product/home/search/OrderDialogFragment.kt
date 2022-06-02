package com.example.store.ui.product.home.search

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.store.R
import com.example.store.databinding.DialogFragmentOrderBinding
import com.example.store.ui.viewmodels.SearchResultViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDialogFragment : DialogFragment(R.layout.dialog_fragment_order) {

    private val viewModel by viewModels<SearchResultViewModel>()

    private lateinit var binding: DialogFragmentOrderBinding

    private var querySearch: String = arguments?.getString("Query") ?: ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogFragmentOrderBinding.bind(view)

        selectOrder()
    }

    private fun selectOrder() {
        binding.dateTv.setOnClickListener {
            viewModel.sort(querySearch, "date")
            dismiss()
        }

        binding.priceHighTv.setOnClickListener {
            viewModel.sort(querySearch, "expensive")
            dismiss()
        }


        binding.priceLowTv.setOnClickListener {
            viewModel.sort(querySearch, "cheap")
            dismiss()
        }


        binding.ratingTv.setOnClickListener {
            viewModel.sort(querySearch, "rating")
            dismiss()
        }

        binding.popularityTv.setOnClickListener {
            viewModel.sort(querySearch, "popularity")
            dismiss()
        }

    }
}