package com.example.store.ui.product.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.R
import com.example.store.data.Result
import com.example.store.data.model.order.body.LineItem
import com.example.store.data.model.order.body.Order
import com.example.store.data.model.product.ProductItem
import com.example.store.data.model.reviews.body.ReviewBody
import com.example.store.databinding.AddNewCommentDialogBinding
import com.example.store.databinding.FragmentDetailProductBinding
import com.example.store.databinding.SnackBarLoginCardViewBinding
import com.example.store.ui.product.detail.review.ReviewAdaptor
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

    private lateinit var reviewAdaptor: ReviewAdaptor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailProductBinding.bind(view)

        initValues()

        getCart()

        createGallery()
        detailProduct()
        cart()

        reviews()
        addNewReview()

    }

    private fun addNewReview() {

        val bindingDialog = AddNewCommentDialogBinding.inflate(layoutInflater)

        val newReviewDialogFragment =
            Dialog(requireContext(), androidx.transition.R.style.Base_ThemeOverlay_AppCompat)
        newReviewDialogFragment.setContentView(bindingDialog.root)
        newReviewDialogFragment.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        binding.addReviewCardView.setOnClickListener {
            newReviewDialogFragment.show()
        }

        bindingDialog.dismissBtn.setOnClickListener {
            newReviewDialogFragment.dismiss()
        }

        bindingDialog.submitBtn.setOnClickListener {
            val reviewer = bindingDialog.nameReviewerEd.text.toString()
            val review = bindingDialog.reviewEd.text.toString()
            val rating = when {
                bindingDialog.rating1Rb.isChecked -> 1
                bindingDialog.rating2Rb.isChecked -> 2
                bindingDialog.rating3Rb.isChecked -> 3
                bindingDialog.rating4Rb.isChecked -> 4
                bindingDialog.rating5Rb.isChecked -> 5
                else -> 5
            }

            val reviewBody = ReviewBody(
                item.id,
                rating,
                review,
                reviewer,
                "amiranse0@gmail.com"
            )

            Log.d("REVIEW", reviewBody.toString())

            //viewModel.createReview(reviewBody)

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.createReview(reviewBody).collect {
                        when (it) {
                            is Result.Success -> {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.review_added),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is Result.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.it_is_problem),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }

            newReviewDialogFragment.dismiss()
        }

    }

    private fun reviews() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getReviews(item.id.toString()).collect {
                    when (it) {
                        is Result.Success -> {
                            reviewAdaptor.setData(it.data)

                            activity?.findViewById<FragmentContainerView>(R.id.fragment)?.visibility =
                                View.VISIBLE
                            activity?.findViewById<ProgressBar>(R.id.progress_bar)?.visibility =
                                View.INVISIBLE
                        }

                        is Result.Loading -> {
                            activity?.findViewById<FragmentContainerView>(R.id.fragment)?.visibility =
                                View.INVISIBLE
                            activity?.findViewById<ProgressBar>(R.id.progress_bar)?.visibility =
                                View.VISIBLE
                        }

                    }
                }
            }
        }

        binding.reviewRc.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.reviewRc.adapter = reviewAdaptor
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

        reviewAdaptor = ReviewAdaptor()

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
                } else if (setOfProductInCart.contains(args.item.id.toString())) {
                    findNavController().navigate(R.id.action_detailProductFragment_to_cartFragment)
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
                Log.d("ORDER", "$it")
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

                        if (item.id.toString() in setOfProductInCart) {
                            binding.addToCartBtn.text = getString(R.string.see_cart)
                        }

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
                                getString(R.string.added_to_cart),
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