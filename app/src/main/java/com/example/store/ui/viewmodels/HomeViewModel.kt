package com.example.store.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.Repository
import com.example.store.data.Result
import com.example.store.data.model.product.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
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

    private val _resultSearchProductsStateFlow =
        MutableStateFlow<Result<List<ProductItem>>>(Result.Loading)
    val resultSearchProductsStateFlow = _resultSearchProductsStateFlow

    private val _resultSpecialOffersStateFlow =
        MutableStateFlow<Result<ProductItem>>(Result.Loading)
    val resultSpecialOffersStateFlow = _resultSpecialOffersStateFlow

    init {
        getBestProducts(1, 10)
        getLatestProducts(1, 10)
        getFavouriteProducts(1, 10)
        getSpecialOffers()
    }

    fun getFavouriteProducts(page: Int, perPage: Int) {
        viewModelScope.launch {
            repository.getFavouriteProducts(page, perPage).collect {
                favouriteProductsStateFlow.emit(it)
            }
        }
    }

    fun getLatestProducts(page: Int, perPage: Int) {
        viewModelScope.launch {
            repository.getLatestProducts(page, perPage).collect {
                lastProductsStateFlow.emit(it)
            }
        }
    }

    fun getBestProducts(page: Int, perPage: Int) {
        viewModelScope.launch {
            repository.getBestProducts(page, perPage).collect {
                bestProductsStateFlow.emit(it)
            }
        }
    }

    fun search(page: Int, searchQuery: String): MutableStateFlow<Result<List<ProductItem>>> {
        viewModelScope.launch {
            repository.search(page, searchQuery).collect {
                resultSearchProductsStateFlow.emit(it)
            }
        }
        return resultSearchProductsStateFlow
    }

    fun getSpecialOffers() {
        viewModelScope.launch {
            repository.getSpecialOffers().collect {
                resultSpecialOffersStateFlow.emit(it)
            }
        }
    }
}