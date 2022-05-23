package com.example.store.viewmodels

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
class BestProductViewModel @Inject constructor(
    private val repository: Repository
):ViewModel() {
    init {
        getBestProducts(1)
    }

    private val _bestProductsStateFlow =
        MutableStateFlow<Result<List<ProductItem>>>(Result.Loading)
    val bestProductsStateFlow = _bestProductsStateFlow

    fun getBestProducts(page: Int) {
        viewModelScope.launch {
            repository.getBestProducts(page).collect {
                bestProductsStateFlow.value = it
            }
        }
    }
}