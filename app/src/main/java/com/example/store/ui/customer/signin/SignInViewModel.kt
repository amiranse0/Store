package com.example.store.ui.customer.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.data.Repository
import com.example.store.data.Result
import com.example.store.data.model.customer.body.Customer
import com.example.store.data.model.customer.result.CustomerResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _resultSignInCustomerStateFlow =
        MutableStateFlow<Result<CustomerResult>>(Result.Loading)
    val resultSignInCustomerStateFlow = _resultSignInCustomerStateFlow


    suspend fun createCustomer(customer: Customer): MutableStateFlow<Result<CustomerResult>> {
        viewModelScope.launch {
            repository.createCustomer(customer).collect{
                resultSignInCustomerStateFlow.emit(it)
            }
        }
        return resultSignInCustomerStateFlow
    }
}