package com.example.store.ui.product.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.Repository
import com.example.store.data.Result
import com.example.store.data.model.order.body.Order
import com.example.store.data.model.order.result.OrderResult
import com.example.store.data.model.product.ProductItem
import com.example.store.data.model.reviews.body.ReviewBody
import com.example.store.data.model.reviews.result.ReviewItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _orderResultStateFlow =
        MutableStateFlow<Result<OrderResult>>(Result.Loading)
    val orderResultStateFlow = _orderResultStateFlow

    fun createCart(order: Order): MutableStateFlow<Result<OrderResult>> {
        viewModelScope.launch {
            repository.createOrder(order).collect {
                orderResultStateFlow.emit(it)
            }
        }
        return orderResultStateFlow
    }

    fun updateOrder(order: Order, id:String): MutableStateFlow<Result<OrderResult>>{
        viewModelScope.launch {
            repository.updateOrder(order, id).collect{
                orderResultStateFlow.emit(it)
            }
        }
        return orderResultStateFlow
    }

    fun getOrder(id: String): MutableStateFlow<Result<OrderResult>>{
        val orderResult2StateFlow = MutableStateFlow<Result<OrderResult>>(Result.Loading)
        viewModelScope.launch {
            repository.getOrder(id).collect{
                orderResult2StateFlow.emit(it)
            }
        }
        return orderResult2StateFlow
    }

    fun getReviews(productID: String): MutableStateFlow<Result<List<ReviewItem>>>{
        val reviewItemStateFlow = MutableStateFlow<Result<List<ReviewItem>>>(Result.Loading)
        viewModelScope.launch {
            repository.getReviews(productID).collect{
                reviewItemStateFlow.emit(it)
            }
        }
        return reviewItemStateFlow
    }

    fun createReview(reviewBody: ReviewBody): MutableStateFlow<Result<ReviewItem>> {
        val reviewItemStateFlow = MutableStateFlow<Result<ReviewItem>>(Result.Loading)
        viewModelScope.launch {
            repository.setReview(reviewBody).collect{
                reviewItemStateFlow.emit(it)
            }
        }
        return reviewItemStateFlow
    }
}