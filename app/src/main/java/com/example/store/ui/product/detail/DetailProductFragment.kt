package com.example.store.ui.product.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.store.R
import com.example.store.data.Result
import com.example.store.data.model.order.body.LineItem
import com.example.store.data.model.order.body.Order
import com.example.store.data.model.product.ProductItem
import com.example.store.databinding.FragmentDetailProductBinding
import com.example.store.databinding.SnackBarLoginCardViewBinding
import com.example.store.ui.product.home.slider.SpecialAdaptor
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DetailProductFragment : Fragment(R.layout.fragment_detail_product) {

    private lateinit var binding: FragmentDetailProductBinding

    private val args: DetailProductFragmentArgs by navArgs()
    private lateinit var item: ProductItem

    private val viewModel: DetailViewModel by viewModels()

    private var userId = ""
    private var orderId = ""

    private var orderItems: MutableList<LineItem> = mutableListOf()

    private var setOfProductInCart: MutableList<String> = mutableListOf()

    private var accountSharedPreferences: SharedPreferences? = null
    private var orderSharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailProductBinding.bind(view)

        getCart()

        initValues()

        createGallery()
        detailProduct()
        cart()

        search()
    }


    private fun search() {
        binding.homeSearchCardView.setOnClickListener {
            findNavController().navigate(R.id.action_detailProductFragment_to_searchFragment)
        }
    }

    private fun initValues() {
        item = args.item

        accountSharedPreferences =
            activity?.getSharedPreferences("loginSharedPref", Context.MODE_PRIVATE)
        userId = accountSharedPreferences?.getString("id", "") ?: ""

        orderSharedPreferences =
            activity?.getSharedPreferences("orderSharePref", Context.MODE_PRIVATE)
        editor = orderSharedPreferences?.edit()
        orderId = orderSharedPreferences?.getString("id", "") ?: ""

    }

    @SuppressLint("RestrictedApi")
    private fun cart() {

        if (setOfProductInCart.contains(args.item.id.toString())) {
            binding.addToCartBtn.text = getString(R.string.see_cart)
        }

        binding.addToCartBtn.setOnClickListener {
            if (userId == "") {
                snackBarForGoToAccount(it)
            } else {
                if (orderId == "") {
                    createCart()
                } else if (!setOfProductInCart.contains(args.item.id.toString())) {
                    addNewItemToCart()
                }
            }
        }
    }

    private fun snackBarForGoToAccount(view: View) {
        val snackbar = Snackbar.make(view, "", 5000)

        val customSnackView: View =
            layoutInflater.inflate(R.layout.snack_bar_login_card_view, null)
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT)
        val snackbarLayout = snackbar.view as SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)

        val snackBarBinding = SnackBarLoginCardViewBinding.bind(customSnackView)

        snackbarLayout.addView(customSnackView, 0)
        snackbar.show()

        snackBarBinding.goToLoginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_detailProductFragment_to_loginFragment)
            snackbar.dismiss()
        }
    }

    private fun getCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getOrder(orderId).collect {
                when (it) {
                    is Result.Success -> {

                        orderItems = it.data.line_items.map { it1 ->
                            LineItem(
                                id = it1.id,
                                productId = it1.productId,
                                quantity = it1.quantity
                            )
                        } as MutableList<LineItem>

                        setOfProductInCart = it.data.line_items.map { it2 ->
                            it2.productId.toString()
                        } as MutableList<String>

                        Log.d("ORDER", "1. $setOfProductInCart")
                    }
                }
            }

        }
    }

    private fun addNewItemToCart() {

        orderItems.add(
            LineItem(
                productId = item.id,
                quantity = 1
            )
        )

        val newOrder = Order(
            lineItems = orderItems,
            customerId = userId.toInt()
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.updateOrder(newOrder, orderId).collect {
                    when (it) {
                        is Result.Success -> {
                            binding.addToCartBtn.text = getString(R.string.see_cart)

                            Toast.makeText(
                                requireContext(),
                                "به سبد خرید اضافه شد.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }


    private fun createCart() {
        val items = LineItem(
            productId = item.id,
            quantity = 1
        )
        val order = Order(
            lineItems = listOf(items),
            customerId = userId.toInt()
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.createCart(order).collect {
                    when (it) {
                        is Result.Success -> {

                            editor?.apply {
                                putString("id", "${it.data.id}")

                                putStringSet("items", setOf(args.item.id.toString()))
                                apply()
                            }

                            binding.addToCartBtn.text = getString(R.string.see_cart)

                            Toast.makeText(
                                requireContext(),
                                "به سبد خرید اضافه شد.",
                                Toast.LENGTH_SHORT
                            ).show()
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
        binding.priceProduct.text = price

        val categories =
            item.categories.map { it.name }.toString().replace(",", " / ").replace("[", "")
                .replace("]", "")
        binding.categoriesDetailTv.text = categories

        val rating = item.averageRating
        binding.ratingTv.text = rating
    }

    private fun createGallery() {
        val images = item.images.map { it.src }

        val sliderAdapter = SpecialAdaptor(requireContext(), images)
        binding.galleryVp.adapter = sliderAdapter


        binding.tabLayout.setupWithViewPager(binding.galleryVp, true)
    }
}