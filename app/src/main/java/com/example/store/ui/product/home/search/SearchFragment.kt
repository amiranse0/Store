package com.example.store.ui.product.home.search

import android.os.Bundle
import android.view.View
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
import com.example.store.databinding.FragmentSearchBinding
import com.example.store.ui.product.ProductAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchAdapter: ProductAdapter

    private val viewModel:SearchViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchBinding.bind(view)

        binding.customSearchView.searchEd.isSelected = true

        suggestion()

        goToDetail(view)

        submitSearch()

        backToHome()
    }

    private fun backToHome() {
        binding.customSearchView.backToHomeFromSearch.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
        }
    }

    private fun submitSearch() {

        binding.customSearchView.searchEd.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                goToSearchResult()
                return@OnEditorActionListener true
            }
            false
        })

    }

    private fun goToSearchResult() {
        val bundle = bundleOf("query" to binding.customSearchView.searchEd.text.toString())
        findNavController().navigate(R.id.action_searchFragment_to_searchResultFragment, bundle)
    }

    private fun goToDetail(view: View) {
        searchAdapter.setToClickOnItem(object : ProductAdapter.ClickOnItem{
            override fun clickOnItem(position: Int, view: View?) {
                val item = searchAdapter.oldList[position]
                val action = SearchFragmentDirections.actionSearchFragmentToDetailProductFragment(item)
                if (view != null) {
                    Navigation.findNavController(view).navigate(action)
                }
            }

        })
    }

    private fun suggestion() {
        searchAdapter = ProductAdapter()
        searchRecyclerView = binding.suggestionRc
        searchRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        searchRecyclerView.adapter = searchAdapter

        var query = arguments?.getString("query")?:""
        binding.customSearchView.searchEd.setText(query)

        binding.customSearchView.searchEd.addTextChangedListener {
            query = binding.customSearchView.searchEd.text.toString()
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                    viewModel.searchSuggestion(query).collect{
                        when(it){
                            is Result.Success -> {
                                searchAdapter.setData(it.data)
                            }
                            is Result.Error -> {

                            }
                        }
                    }
                }
            }
        }
    }
}