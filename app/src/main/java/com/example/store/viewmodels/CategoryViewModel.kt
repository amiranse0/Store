package com.example.store.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.Repository
import com.example.store.data.Result
import com.example.store.data.model.category.CategoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: Repository
):ViewModel() {

    init {
        getCategories()
    }

    private val _categoriesStateFlow =
        MutableStateFlow<Result<List<CategoryItem>>>(Result.Loading)
    val categoriesStateFlow = _categoriesStateFlow

    fun getCategories() {
        viewModelScope.launch {
            repository.getCategories().collect {
                categoriesStateFlow.value = it
            }
        }
    }
}