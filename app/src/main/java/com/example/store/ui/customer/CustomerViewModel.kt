package com.example.store.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.Repository
import com.example.store.data.model.customer.body.Customer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(private val repository: Repository) : ViewModel() {


    suspend fun createCustomer(customer: Customer) {
        repository.createCustomer(customer)
    }
}