package com.example.store.ui.product.home.search

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
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
import com.example.store.databinding.DialogFragmentOrderBinding
import com.example.store.databinding.FilterLayoutDialogBinding
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

    private var query: String = ""

    private var lowerPrice = ""
    private var higherPrice = ""

    private var categoryId = 0

    private var sort = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentResultSearchBinding.bind(view)

        getQuery()

        getItemsFromSearch()

        goToDetail()

        sortResult()

        filterResult()

        searchAgain()

        backToHome()
    }

    private fun backToHome() {
        binding.customSearchView.backToHomeFromSearch.setOnClickListener {
            findNavController().navigate(R.id.action_searchResultFragment_to_homeFragment)
        }
    }

    private fun searchAgain() {
        binding.customSearchView.searchEd.addTextChangedListener {
            val query = binding.customSearchView.searchEd.text.toString()
            val bundle = bundleOf("query" to query)
            findNavController().navigate(R.id.action_searchResultFragment_to_searchFragment, bundle)

        }

    }

    private fun getQuery() {
        query = arguments?.getString("query") ?: ""
        binding.customSearchView.searchEd.setText(query)
    }

    private fun filterResult() {
        binding.filterLayout.setOnClickListener {
            val filterBinding = FilterLayoutDialogBinding.inflate(layoutInflater)
            val filterDialog = Dialog(
                requireContext(),
                androidx.transition.R.style.Base_Theme_AppCompat_Light_DarkActionBar
            )
            filterDialog.setContentView(filterBinding.root)
            filterDialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            filterBinding.lowerBoundPriceEd.setText(lowerPrice)
            filterBinding.higherBoundPriceEd.setText(higherPrice)

            when (categoryId) {
                121 -> filterBinding.health.isChecked = true
                63 -> filterBinding.womenCloth.isChecked = true
                64 -> filterBinding.cloth.isChecked = true
                52 -> filterBinding.digital.isChecked = true
                102 -> filterBinding.watch.isChecked = true
                81 -> filterBinding.supermarket.isChecked = true
                119 -> filterBinding.sale.isChecked = true
                77 -> filterBinding.movie.isChecked = true
                79 -> filterBinding.book.isChecked = true
                76 -> filterBinding.art.isChecked = true
            }

            filterDialog.show()

            filterBinding.dismissBtn.setOnClickListener {
                filterDialog.dismiss()
            }

            filterBinding.submitFilter.setOnClickListener {
                lowerPrice = filterBinding.lowerBoundPriceEd.text.toString()
                higherPrice = filterBinding.higherBoundPriceEd.text.toString()
                if (filterBinding.art.isChecked) categoryId = 76
                else if (filterBinding.movie.isChecked) categoryId = 77
                else if (filterBinding.book.isChecked) categoryId = 79
                else if (filterBinding.sale.isChecked) categoryId = 119
                else if (filterBinding.health.isChecked) categoryId = 121
                else if (filterBinding.cloth.isChecked) categoryId = 64
                else if (filterBinding.womenCloth.isChecked) categoryId = 63
                else if (filterBinding.digital.isChecked) categoryId = 52
                else if (filterBinding.watch.isChecked) categoryId = 102
                else if (filterBinding.supermarket.isChecked) categoryId = 81

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.sortAndFilter(query, sort, higherPrice, lowerPrice, categoryId)
                            .collect {
                                when (it) {
                                    is Result.Success -> {
                                        recyclerAdaptor.setData(it.data)
                                        filterDialog.dismiss()
                                    }
                                }
                            }
                    }
                }

            }
        }
    }

    private fun sortResult() {
        binding.sortLayout.setOnClickListener {
            val bindingSort = DialogFragmentOrderBinding.inflate(layoutInflater)
            val orderDialogFragment = Dialog(
                requireContext(),
                androidx.transition.R.style.Base_Theme_AppCompat_Light_DarkActionBar
            )
            orderDialogFragment.setContentView(bindingSort.root)
            orderDialogFragment.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            orderDialogFragment.show()

            bindingSort.dismissOrderDialogBtn.setOnClickListener {
                orderDialogFragment.dismiss()
            }

            bindingSort.dateTv.setOnClickListener {
                sort = "date"
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.sortAndFilter(query, sort, higherPrice, lowerPrice, categoryId)
                            .collect {
                                when (it) {
                                    is Result.Success -> {
                                        recyclerAdaptor.setData(it.data)
                                        orderDialogFragment.dismiss()
                                    }
                                }
                            }
                    }
                }
            }

            bindingSort.priceHighTv.setOnClickListener {
                sort = "expensive"
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.sortAndFilter(query, sort, higherPrice, lowerPrice, categoryId)
                            .collect {
                                when (it) {
                                    is Result.Success -> {
                                        recyclerAdaptor.setData(it.data)
                                        orderDialogFragment.dismiss()
                                    }
                                }
                            }
                    }
                }
            }

            bindingSort.priceLowTv.setOnClickListener {
                sort = "cheap"
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.sortAndFilter(query, sort, higherPrice, lowerPrice, categoryId)
                            .collect {
                                when (it) {
                                    is Result.Success -> {
                                        recyclerAdaptor.setData(it.data)
                                        orderDialogFragment.dismiss()
                                    }
                                }
                            }
                    }
                }
            }

            bindingSort.ratingTv.setOnClickListener {
                sort = "rating"
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.sortAndFilter(query, sort, higherPrice, lowerPrice, categoryId)
                            .collect {
                                when (it) {
                                    is Result.Success -> {
                                        recyclerAdaptor.setData(it.data)
                                        orderDialogFragment.dismiss()
                                    }
                                }
                            }
                    }
                }
            }

            bindingSort.popularityTv.setOnClickListener {
                sort = "popularity"
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.sortAndFilter(query, sort, higherPrice, lowerPrice, categoryId)
                            .collect {
                                when (it) {
                                    is Result.Success -> {
                                        recyclerAdaptor.setData(it.data)
                                        orderDialogFragment.dismiss()
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

    private fun goToDetail() {
        recyclerAdaptor.setToClickOnItem(object : ProductAdapter.ClickOnItem {
            override fun clickOnItem(position: Int, view: View?) {
                val item = recyclerAdaptor.oldList[position]
                val action =
                    SearchResultFragmentDirections.actionSearchResultFragmentToDetailProductFragment(
                        item
                    )
                if (view != null) {
                    Navigation.findNavController(view).navigate(action)
                }
            }

        })
    }

    private fun getItemsFromSearch() {
        recyclerView = binding.resultRc
        recyclerAdaptor = ProductAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recyclerAdaptor

        lifecycleScope.launch {
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