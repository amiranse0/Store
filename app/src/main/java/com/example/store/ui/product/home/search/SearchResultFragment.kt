package com.example.store.ui.product.home.search

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
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
import com.example.store.databinding.FragmentResultSearchBinding
import com.example.store.ui.product.ProductAdapter
import com.example.store.ui.viewmodels.SearchResultViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchResultFragment : Fragment(R.layout.fragment_result_search) {

    private lateinit var binding: FragmentResultSearchBinding
    private val viewModel by viewModels<SearchResultViewModel>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdaptor: ProductAdapter

    private var query: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentResultSearchBinding.bind(view)

        getQuery()

        getItemsFromSearch()

        goToDetail()

        sortResult()

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

    private fun sortResult() {
        binding.sortLayout.setOnClickListener {
            val bindingSort = DialogFragmentOrderBinding.inflate(layoutInflater)
            val orderDialogFragment = Dialog(requireContext(), androidx.transition.R.style.Base_Theme_AppCompat_Light_DarkActionBar)
            orderDialogFragment.setContentView(bindingSort.root)
            orderDialogFragment.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            orderDialogFragment.show()

            bindingSort.dismissOrderDialogBtn.setOnClickListener {
                orderDialogFragment.dismiss()
            }

            bindingSort.dateTv.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                        viewModel.sort(query, "date").collect{
                            when(it){
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
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                        viewModel.sort(query, "expensive").collect{
                            when(it){
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
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                        viewModel.sort(query, "cheap").collect{
                            when(it){
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
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                        viewModel.sort(query, "rating").collect{
                            when(it){
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
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                        viewModel.sort(query, "popularity").collect{
                            when(it){
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
                val action = SearchResultFragmentDirections.actionSearchResultFragmentToDetailProductFragment(item)
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