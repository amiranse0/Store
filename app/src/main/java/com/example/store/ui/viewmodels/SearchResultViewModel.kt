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
class SearchResultViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _searchResultProductsStateFlow =
        MutableStateFlow<Result<List<ProductItem>>>(Result.Loading)
    val searchResultProductsStateFlow = _searchResultProductsStateFlow

    fun resultSearch(querySearch: String): MutableStateFlow<Result<List<ProductItem>>>{
        viewModelScope.launch {
            repository.search(100, querySearch).collect{
                searchResultProductsStateFlow.emit(it)
            }
        }
        return searchResultProductsStateFlow
    }
}