package com.example.store.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.Repository
import com.example.store.data.Result
import com.example.store.data.model.order.body.Order
import com.example.store.data.model.order.result.OrderResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    fun updateOrder(order: Order, id:String): MutableStateFlow<Result<OrderResult>> {
        val orderResultStateFlow = MutableStateFlow<Result<OrderResult>>(Result.Loading)
        viewModelScope.launch {
            repository.updateOrder(order, id).collect{
                orderResultStateFlow.emit(it)
            }
        }
        return orderResultStateFlow
    }

    fun getOrder(id: String): MutableStateFlow<Result<OrderResult>> {
        val orderResult2StateFlow = MutableStateFlow<Result<OrderResult>>(Result.Loading)
        viewModelScope.launch {
            repository.getOrder(id).collect{
                orderResult2StateFlow.emit(it)
            }
        }
        return orderResult2StateFlow
    }
}