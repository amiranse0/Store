package com.example.store.ui.product.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.data.model.order.body.LineItem
import com.example.store.data.model.order.body.Order
import com.example.store.data.model.product.ProductItem
import com.example.store.databinding.FragmentDetailProductBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailProductFragment : Fragment(R.layout.fragment_detail_product) {

    private lateinit var binding: FragmentDetailProductBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdaptor: GalleryAdapter

    private val args: DetailProductFragmentArgs by navArgs()
    private lateinit var item: ProductItem

    private var idProduct = ""

    private val viewModel: DetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailProductBinding.bind(view)

        initValues()

        createGallery()
        detailProduct()
        cart()
    }

    private fun initValues() {
        item = args.item
    }

    private fun cart() {
        val sharedPreferences =
            activity?.getSharedPreferences("orderSharePref", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "")
        val setOfProductInCart: MutableSet<String>? =
            sharedPreferences?.getStringSet("set", emptySet())

        val editor = sharedPreferences?.edit()

        if (id == ""){
            createCart()
        } else{
            updateCart()
        }

    }

    private fun updateCart() {
        TODO("Not yet implemented")
    }

    private fun createCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){

            }
        }
    }

    private fun detailProduct() {

        val name = item.name
        binding.titleProduct.text = name

        val description = item.description.replace("<[^>]*>".toRegex(), "")
        binding.descriptionProduct.text = getString(R.string.description_format, description)

        val price = item.price
        binding.priceProduct.text = getString(R.string.price_format, price)
    }

    private fun createGallery() {
        val images = item.images.map { it.src }

        recyclerView = binding.galleryRc

        if (images != null) {
            recyclerAdaptor = GalleryAdapter(images)
        } else recyclerAdaptor = GalleryAdapter(emptyList())

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        recyclerView.adapter = recyclerAdaptor
    }
}