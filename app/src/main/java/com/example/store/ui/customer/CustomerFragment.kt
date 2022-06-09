package com.example.store.ui.customer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.store.R
import com.example.store.databinding.FragmentCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerFragment: Fragment(R.layout.fragment_customer) {

    private lateinit var binding: FragmentCategoryBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCategoryBinding.bind(view)

    }
}