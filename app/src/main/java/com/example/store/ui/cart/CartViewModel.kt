package com.example.store.ui.cart

import androidx.lifecycle.ViewModel
import com.example.store.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: Repository): ViewModel() {

}