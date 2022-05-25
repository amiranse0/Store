package com.example.store.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.Repository
import com.example.store.data.Result
import com.example.store.data.model.product.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewestProductViewModel @Inject constructor(
    private val repository: Repository
):ViewModel() {
    init {
        getNewestProducts(1)
    }

    private val _newestProductsStateFlow =
        MutableStateFlow<Result<List<ProductItem>>>(Result.Loading)
    val newestProductsStateFlow = _newestProductsStateFlow

    fun getNewestProducts(page: Int) {
        viewModelScope.launch {
            repository.getLatestProducts(page).collect {
                newestProductsStateFlow.value = it
            }
        }
    }
}