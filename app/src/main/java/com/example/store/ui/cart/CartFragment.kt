package com.example.store.ui.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.store.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment:Fragment(R.layout.fragment_cart) {

    private val viewModel: CartViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}