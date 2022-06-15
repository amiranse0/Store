package com.example.store.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.Repository
import com.example.store.data.Result
import com.example.store.data.model.customer.result.CustomerResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _resultCustomerStateFlow =
        MutableStateFlow<Result<CustomerResult>>(Result.Loading)
    val resultCustomerStateFlow = _resultCustomerStateFlow


    fun getCustomer(email: String): MutableStateFlow<Result<CustomerResult>> {
        viewModelScope.launch {
            repository.getCustomer(email).collect {
                resultCustomerStateFlow.emit(it)
            }
        }
        return resultCustomerStateFlow
    }
}