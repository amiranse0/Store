package com.example.store.ui.product.home.best

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.data.Result
import com.example.store.data.model.product.ProductItem
import com.example.store.databinding.FragmentBestProductBinding
import com.example.store.ui.product.ProductAdapter
import com.google.android.material.progressindicator.LinearProgressIndicator
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
        goToDetail()

    }

    private fun goToDetail() {
        recyclerAdaptor.setToClickOnItem(object : ProductAdapter.ClickOnItem {
            override fun clickOnItem(position: Int, view: View?) {
                val item:ProductItem = recyclerAdaptor.oldList[position]

                val action = BestProductFragmentDirections.actionBestProductFragmentToDetailProductFragment(item)

                if (view != null) {
                    Navigation.findNavController(view).navigate(action)
                }

            }

        })
    }

    private fun getListBestProduct() {
        recyclerView = binding.bestProductRc
        recyclerAdaptor = ProductAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recyclerAdaptor

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.bestProductsStateFlow.collect {
                    when (it) {
                        is Result.Error -> {
                            recyclerAdaptor.setData(emptyList())
                        }
                        is Result.Loading -> {
                            recyclerAdaptor.setData(emptyList())
                            activity?.findViewById<FragmentContainerView>(R.id.fragment)?.visibility = View.INVISIBLE
                            activity?.findViewById<ProgressBar>(R.id.progress_bar)?.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            recyclerAdaptor.setData(it.data)
                            activity?.findViewById<FragmentContainerView>(R.id.fragment)?.visibility = View.VISIBLE
                            activity?.findViewById<ProgressBar>(R.id.progress_bar)?.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }
    }
}