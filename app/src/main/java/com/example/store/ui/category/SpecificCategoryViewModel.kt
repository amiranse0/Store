package com.example.store.ui.category

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
class SpecificCategoryViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val _productsStateFlow =
        MutableStateFlow<Result<List<ProductItem>>>(Result.Loading)
    val productsStateFlow = _productsStateFlow

    fun getProducts(page: Int, category:String) {
        viewModelScope.launch {
            repository.getSomeCategory(page, category).collect {
                productsStateFlow.value = it
            }
        }
    }
}