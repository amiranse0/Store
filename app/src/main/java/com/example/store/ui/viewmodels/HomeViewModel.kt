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
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

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
        getBestProducts(1, 10)
        getLatestProducts(1, 10)
        getFavouriteProducts(1, 10)
    }

    fun getFavouriteProducts(page: Int, perPage:Int) {
        viewModelScope.launch {
            repository.getFavouriteProducts(page, perPage).collect {
                favouriteProductsStateFlow.emit(it)
            }
        }
    }

    fun getLatestProducts(page: Int, perPage:Int) {
        viewModelScope.launch {
            repository.getLatestProducts(page, perPage).collect {
                lastProductsStateFlow.emit(it)
            }
        }
    }

    fun getBestProducts(page: Int, perPage:Int){
        viewModelScope.launch {
            repository.getBestProducts(page, perPage).collect{
                bestProductsStateFlow.emit(it)
            }
        }
    }
}