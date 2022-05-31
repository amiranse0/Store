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
class ProductViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _bestProductsStateFlow =
        MutableStateFlow<Result<List<ProductItem>>>(Result.Loading)
    val bestProductsStateFlow = _bestProductsStateFlow

    private val _favouriteProductsStateFlow =
        MutableStateFlow<Result<List<ProductItem>>>(Result.Loading)
    val favouriteProductsStateFlow = _favouriteProductsStateFlow

    private val _lastProductsStateFlow =
        MutableStateFlow<Result<List<ProductItem>>>(Result.Loading)
    val lastProductsStateFlow = _lastProductsStateFlow

    init {
        getBestProducts(1)
        getLatestProducts(1)
        getFavouriteProducts(1)
    }

    fun getFavouriteProducts(page: Int) {
        viewModelScope.launch {
            repository.getFavouriteProducts(page).collect {
                favouriteProductsStateFlow.emit(it)
            }
        }
    }

    fun getLatestProducts(page: Int) {
        viewModelScope.launch {
            repository.getLatestProducts(page).collect {
                lastProductsStateFlow.emit(it)
            }
        }
    }

    fun getBestProducts(page: Int){
        viewModelScope.launch {
            repository.getBestProducts(page).collect{
                bestProductsStateFlow.emit(it)
            }
        }
    }
}