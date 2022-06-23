package com.example.store.ui.cart

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.R
import com.example.store.data.Result
import com.example.store.data.model.order.body.LineItem
import com.example.store.databinding.FinalOrderDialogBinding
import com.example.store.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint
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

    private var usedCode = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCartBinding.bind(view)

        initValues()
        getProducts()

        finalOrder()

        setTotalPrice()

        updateCart()
    }

    private fun updateCart() {
        cartAdaptor.setToClickOnItemDecrease(object : CartAdaptor.ClickOnDecrease{
            override fun clickOnDecrease(position: Int, view: View?) {
                Toast.makeText(requireContext(), "decrease",Toast.LENGTH_SHORT).show()
            }
        })

        cartAdaptor.setToClickOnItemIncrease(object : CartAdaptor.ClickOnIncrease{
            override fun clickOnIncrease(position: Int, view: View?) {
                Toast.makeText(requireContext(), "Increase",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setTotalPrice() {
        viewModel.totalPriceLiveData.observe(viewLifecycleOwner){
            binding.totalPriceTv.text = getString(R.string.price_format, it.toString())

            if (orderSharedPreferences?.getString("id", "0") != "0"){
                if (it == 0){
                    activity?.findViewById<FragmentContainerView>(R.id.fragment)?.visibility =
                        View.INVISIBLE
                    activity?.findViewById<ProgressBar>(R.id.progress_bar)?.visibility =
                        View.VISIBLE
                } else {
                    activity?.findViewById<FragmentContainerView>(R.id.fragment)?.visibility =
                        View.VISIBLE
                    activity?.findViewById<ProgressBar>(R.id.progress_bar)?.visibility =
                        View.INVISIBLE
                }
            } else if(orderSharedPreferences?.getString("id", "0") == "0"){
                binding.emptyCartLayout.visibility = View.VISIBLE
                binding.finalLayout.visibility = View.GONE
                binding.emptyCardView.visibility = View.GONE
            }

        }
    }

    private fun finalOrder() {

        val bindingDialog = FinalOrderDialogBinding.inflate(layoutInflater)

        val finalOrderDialogFragment =
            Dialog(requireContext(), androidx.transition.R.style.Base_ThemeOverlay_AppCompat)
        finalOrderDialogFragment.setContentView(bindingDialog.root)
        finalOrderDialogFragment.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        viewModel.totalPriceLiveData.observe(viewLifecycleOwner){
            bindingDialog.totalPriceTv.text = getString(R.string.price_format, it.toString())
        }

        binding.finalLayout.setOnClickListener {
            finalOrderDialogFragment.show()
        }

        bindingDialog.dismissBtn.setOnClickListener {
            finalOrderDialogFragment.dismiss()
        }

        bindingDialog.submitBtn.setOnClickListener {
            editor?.apply {
                putString("id", "0")
                apply()
            }

            binding.emptyCartLayout.visibility = View.VISIBLE
        }

        bindingDialog.submitCoupon.setOnClickListener {
            if (usedCode){

                Toast.makeText(requireContext(),getString(R.string.used_code), Toast.LENGTH_SHORT).show()

            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                        viewModel.getCoupons(bindingDialog.couponEd.text.toString()).collect{
                            when(it){
                                is Result.Success -> {
                                    val minimum: Int = it.data.minimumAmount.toDouble().toInt()
                                    val percent = it.data.amount
                                    val totalPrice: Int = viewModel.totalPriceLiveData.value?:0
                                    if (totalPrice > minimum){
                                        val newPrice: Double = totalPrice * (100.0 - percent.toDouble()) * 0.01
                                        viewModel.changeTotalPrice(newPrice)
                                        bindingDialog.totalPriceTv.text = newPrice.toInt().toString()
                                    }
                                    usedCode = true

                                    if (it.data == null){
                                        Toast.makeText(requireContext(), getString(R.string.incorrect_code), Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private fun getProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAllProductInCart(orderId).collect {
                    cartAdaptor.setData(it)
                    Log.d("ORDER", it.toString())

                    if (it.isNotEmpty()) {
                        binding.emptyCartLayout.visibility = View.GONE
                        binding.finalLayout.visibility = View.VISIBLE
                        binding.emptyCardView.visibility = View.VISIBLE
                    } else{
                        binding.emptyCartLayout.visibility = View.VISIBLE
                        binding.finalLayout.visibility = View.GONE
                        binding.emptyCardView.visibility = View.GONE
                    }
                }
            }
        }

        binding.cartRc.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRc.adapter = cartAdaptor
    }


    private fun initValues() {
        orderSharedPreferences =
            activity?.getSharedPreferences("orderSharePref", Context.MODE_PRIVATE)
        editor = orderSharedPreferences?.edit()
        orderId = orderSharedPreferences?.getString("id", "") ?: ""

        cartAdaptor = CartAdaptor()
    }

}