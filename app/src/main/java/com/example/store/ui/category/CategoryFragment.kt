package com.example.store.ui.category

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.data.Result
import com.example.store.databinding.FragmentCategoryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.progressindicator.LinearProgressIndicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var binding: FragmentCategoryBinding
    private val viewModel by viewModels<CategoryViewModel>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdaptor: CategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCategoryBinding.bind(view)

        getCategories()

        goToCategoryProductList()

    }

    private fun goToCategoryProductList() {
        recyclerAdaptor.setToClickOnItem(object : CategoryAdapter.ClickOnItem {
            override fun clickOnItem(position: Int, view: View?) {
                val category = recyclerAdaptor.oldList[position].id.toString()
                val bundle = bundleOf("category" to category)

                findNavController().navigate(
                    R.id.action_categoryFragment_to_categoryProductFragment,
                    bundle
                )
            }
        })
    }

    private fun getCategories() {
        recyclerView = binding.categoryRc
        recyclerAdaptor = CategoryAdapter()
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = recyclerAdaptor

        lifecycleScope.launch {
            viewModel.categoriesStateFlow.collect {
                when (it) {
                    is Result.Error -> {
                        recyclerAdaptor.setData(emptyList())
                    }
                    is Result.Loading -> {
                        recyclerAdaptor.setData(emptyList())

                        activity?.findViewById<FragmentContainerView>(R.id.fragment)?.visibility = View.INVISIBLE
                        activity?.findViewById<LinearProgressIndicator>(R.id.progress_bar)?.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        recyclerAdaptor.setData(it.data)
                        activity?.findViewById<FragmentContainerView>(R.id.fragment)?.visibility = View.VISIBLE
                        activity?.findViewById<LinearProgressIndicator>(R.id.progress_bar)?.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }
}