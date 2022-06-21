package com.example.store.ui.cart

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.store.R
import com.example.store.data.Result
import com.example.store.data.model.order.body.LineItem
import com.example.store.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {

    private val viewModel: CartViewModel by viewModels()

    private var orderSharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    private var orderId = ""

    private var lineItems: MutableList<LineItem> = mutableListOf()

    private var items: MutableList<CartItem> = mutableListOf()

    private lateinit var cartAdaptor: CartAdaptor

    private lateinit var binding: FragmentCartBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCartBinding.bind(view)

        initValues()
        getOrder()
        getProducts()
    }

    private fun getProducts() {
        Log.d("ORDER", items.size.toString())

        viewLifecycleOwner.lifecycleScope.launch {

        }
    }


    private fun initValues() {
        orderSharedPreferences =
            activity?.getSharedPreferences("orderSharePref", Context.MODE_PRIVATE)
        editor = orderSharedPreferences?.edit()
        orderId = orderSharedPreferences?.getString("id", "") ?: ""

        cartAdaptor = CartAdaptor()
        binding.cartRc.adapter = cartAdaptor

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getOrder() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getOrder(orderId).collect {
                    when (it) {
                        is Result.Success -> {
                            lineItems.clear()
                            lineItems.addAll(it.data.line_items as MutableList<LineItem>)
                            Log.d("ORDER", "lineItems ${lineItems.size}")
                        }
                    }
                }
            }


            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.d("ORDER", "lineItems2 ${lineItems.size}")
                for (item in lineItems) {
                    viewModel.getProduct(item.productId.toString()).collect {

                        when (it) {
                            is Result.Success -> {
                                val newItem = CartItem(
                                    id = item.id,
                                    quantity = item.quantity,
                                    productItem = it.data
                                )
                                items.add(
                                    newItem
                                )
                                if (items.isEmpty()) {
                                    binding.emptyCartLayout.visibility = View.VISIBLE
                                }
                                cartAdaptor.oldList = items
                                cartAdaptor.notifyDataSetChanged()

                                Log.d("ORDER", items.toString())
                                Log.d("ORDER", "1. ${it.data}")

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
                            is Result.Error -> {
                                Log.d("ORDER", "fault")
                            }
                        }
                    }
                }
            }
        }
    }
}