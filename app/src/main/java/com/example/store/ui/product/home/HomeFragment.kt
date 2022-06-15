package com.example.store.ui.product.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.store.R
import com.example.store.data.Result
import com.example.store.data.model.product.ProductItem
import com.example.store.databinding.HomeProductBinding
import com.example.store.ui.product.ProductAdapter
import com.example.store.ui.product.home.slider.SpecialOffersAdaptor
import com.example.store.ui.viewmodels.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Math.abs

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_product) {

    override fun onStart() {
        super.onStart()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.VISIBLE
    }

    private lateinit var binding: HomeProductBinding

    private lateinit var mainBestRecyclerView: RecyclerView
    private lateinit var mainFavouriteRecyclerView: RecyclerView
    private lateinit var mainLatestRecyclerView: RecyclerView

    private lateinit var bestAdaptor: MainRowHomeAdaptor
    private lateinit var favouriteAdaptor: MainRowHomeAdaptor
    private lateinit var latestAdaptor: MainRowHomeAdaptor

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var sliderAdaptor: SpecialOffersAdaptor
    private lateinit var viewPager2: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeProductBinding.bind(view)

        viewPager2 = binding.specialOffersVp


        initValues()

        getLatestProduct()
        getBestProduct()
        getFavouriteProduct()

        putDataInRecyclerView()
        goToDetail()
        goToSeeAll()

        slider()

        goToSearchFragment()
    }

    private fun goToSearchFragment() {
        binding.homeSearchCardView.setOnClickListener {
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
                View.GONE
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun goToSeeAll() {
        binding.seeAllBestTv.setOnClickListener {
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
                View.GONE
            findNavController().navigate(R.id.action_homeFragment_to_bestProductFragment)
        }
        binding.seeAllFavouriteTv.setOnClickListener {
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
                View.GONE
            findNavController().navigate(R.id.action_homeFragment_to_favouriteProductFragment)
        }
        binding.seeAllLatestTv.setOnClickListener {
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
                View.GONE
            findNavController().navigate(R.id.action_homeFragment_to_newProductFragment)
        }
    }

    private fun goToDetail() {
        bestAdaptor.setToClickOnItem(object : MainRowHomeAdaptor.ClickOnItem {
            override fun clickOnItem(position: Int, view: View?) {
                val item = bestAdaptor.oldList[position]
                val bundle = bundleOf(
                    "title" to item.name,
                    "images" to item.images.map { it.src },
                    "price" to item.price,
                    "description" to item.description,
                    "category" to item.categories.map { it.name },
                    "purchasable" to item.purchasable
                )
                activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
                    View.GONE
                findNavController().navigate(
                    R.id.action_homeFragment_to_detailProductFragment,
                    bundle
                )
            }
        })

        favouriteAdaptor.setToClickOnItem(object : MainRowHomeAdaptor.ClickOnItem {
            override fun clickOnItem(position: Int, view: View?) {
                val item = favouriteAdaptor.oldList[position]
                val bundle = bundleOf(
                    "title" to item.name,
                    "images" to item.images.map { it.src },
                    "price" to item.price,
                    "description" to item.description,
                    "category" to item.categories.map { it.name },
                    "purchasable" to item.purchasable
                )
                findNavController().navigate(
                    R.id.action_homeFragment_to_favouriteProductFragment,
                    bundle
                )
            }
        })

        latestAdaptor.setToClickOnItem(object : MainRowHomeAdaptor.ClickOnItem {
            override fun clickOnItem(position: Int, view: View?) {
                val item = latestAdaptor.oldList[position]
                val bundle = bundleOf(
                    "title" to item.name,
                    "images" to item.images.map { it.src },
                    "price" to item.price,
                    "description" to item.description,
                    "category" to item.categories.map { it.name },
                    "purchasable" to item.purchasable
                )
                findNavController().navigate(
                    R.id.action_homeFragment_to_newProductFragment,
                    bundle
                )
            }

        })
    }

    private fun initValues() {
        mainBestRecyclerView = binding.bestProductRc
        mainFavouriteRecyclerView = binding.favouriteProductRc
        mainLatestRecyclerView = binding.latestProductRc

        bestAdaptor = MainRowHomeAdaptor()
        favouriteAdaptor = MainRowHomeAdaptor()
        latestAdaptor = MainRowHomeAdaptor()
    }

    private fun putDataInRecyclerView() {
        mainBestRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mainBestRecyclerView.adapter = bestAdaptor

        mainFavouriteRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mainFavouriteRecyclerView.adapter = favouriteAdaptor

        mainLatestRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mainLatestRecyclerView.adapter = latestAdaptor
    }

    private fun slider() {
        sliderAdaptor = SpecialOffersAdaptor(viewPager2)
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultSpecialOffersStateFlow.collect {
                    when (it) {
                        is Result.Loading -> {
                        }
                        is Result.Success -> {
                            sliderAdaptor.images = it.data.images.map { it -> it.src }
                            sliderAdaptor.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        val transformer = CompositePageTransformer()
        transformer.apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r + 0.14f
            }
        }

        viewPager2.setPageTransformer(transformer)

    }


    private fun getFavouriteProduct() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favouriteProductsStateFlow.collect {
                    when (it) {
                        is Result.Error -> {
                        }
                        is Result.Loading -> {
                        }
                        is Result.Success -> {
                            favouriteAdaptor.setData(it.data)
                        }
                    }
                }
            }
        }
    }

    private fun getBestProduct() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bestProductsStateFlow.collect {
                    when (it) {
                        is Result.Error -> {
                        }
                        is Result.Loading -> {
                        }
                        is Result.Success -> {
                            bestAdaptor.setData(it.data)
                        }
                    }
                }
            }
        }
    }

    private fun getLatestProduct() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.lastProductsStateFlow.collect {
                    when (it) {
                        is Result.Error -> {
                        }
                        is Result.Loading -> {
                        }
                        is Result.Success -> {
                            latestAdaptor.setData(it.data)
                        }
                    }
                }
            }

        }
    }
}