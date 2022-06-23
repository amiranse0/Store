package com.example.store.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.Repository
import com.example.store.data.Result
import com.example.store.data.model.order.body.Order
import com.example.store.data.model.order.result.OrderResult
import com.example.store.data.model.product.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val cartItemStateFlow: MutableStateFlow<List<CartItem>> = MutableStateFlow(emptyList())

    var totalPriceLiveData: MutableLiveData<Int> = MutableLiveData(0)

    fun updateOrder(order: Order, id: String): MutableStateFlow<Result<OrderResult>> {
        val orderResultStateFlow = MutableStateFlow<Result<OrderResult>>(Result.Loading)
        viewModelScope.launch {
            repository.updateOrder(order, id).collect {
                orderResultStateFlow.emit(it)
            }
        }
        return orderResultStateFlow
    }

    fun getOrder(id: String): MutableStateFlow<Result<OrderResult>> {
        val orderResult2StateFlow = MutableStateFlow<Result<OrderResult>>(Result.Loading)
        viewModelScope.launch {
            repository.getOrder(id).collect {
                orderResult2StateFlow.emit(it)
            }
        }
        return orderResult2StateFlow
    }

    fun getProduct(id: String): MutableStateFlow<Result<ProductItem>> {
        val productStateFlow = MutableStateFlow<Result<ProductItem>>(Result.Loading)
        viewModelScope.launch {
            repository.getProduct(id).collect {
                productStateFlow.emit(it)
            }
        }
        return productStateFlow
    }

    fun getAllProductInCart(id: String): MutableStateFlow<List<CartItem>> {


        viewModelScope.launch {
            val result = repository.getItemsInCart(id)
            val cartItems: MutableList<CartItem> = mutableListOf()
            var totalPrice = 0

            for (item in result) {
                val listIItems: List<CartItem> = emptyList()

                repository.getProduct(item.first.toString()).collect {
                    when (it) {
                        is Result.Success -> {
                            cartItems.add(
                                CartItem(
                                    item.first,
                                    item.second,
                                    it.data
                                )
                            )

                            totalPrice += item.second * it.data.price.toInt()
                        }
                    }
                }
            }
            totalPriceLiveData.postValue(totalPrice)
            cartItemStateFlow.emit(cartItems)
        }
        return cartItemStateFlow
    }
}