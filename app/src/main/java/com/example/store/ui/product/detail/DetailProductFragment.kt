package com.example.store.ui.product.detail

import android.content.Context
import android.content.SharedPreferences
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
import com.example.store.data.Result
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

    private val lineItem: MutableList<LineItem> = mutableListOf()

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
            sharedPreferences?.getStringSet("items", emptySet())

        if (setOfProductInCart?.contains(args.item.id.toString()) == true) {
            binding.addToCartBtn.text = getString(R.string.see_cart)
        }

        val editor = sharedPreferences?.edit()

        binding.addToCartBtn.setOnClickListener {
            if (id == "") {
                createCart(editor)
            } else {
                if (setOfProductInCart?.contains(args.item.id.toString()) == true) {
                    addOnesToCart()
                } else {
                    addNewItemToCart(editor)
                }
            }
        }

    }

    private fun addNewItemToCart(editor: SharedPreferences.Editor?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

            }
        }
    }

    private fun addOnesToCart() {
        TODO("Not yet implemented")
    }

    private fun createCart(editor: SharedPreferences.Editor?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val items = LineItem(
                    item.id,
                    1,
                    item.id + 1,
                    item.price.toInt()
                )
                val order = Order(
                    listOf(items),
                    "bacs",
                    "Direct Bank Transfer",
                    false
                )
                viewModel.createCart(order).collect {
                    when (it) {
                        is Result.Success -> {
                            editor?.apply {
                                putString("id", "${it.data.id}")
                                putStringSet("items", setOf(args.item.id.toString()))
                                apply()
                            }

                            binding.addToCartBtn.text = getString(R.string.see_cart)
                        }
                    }
                }
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