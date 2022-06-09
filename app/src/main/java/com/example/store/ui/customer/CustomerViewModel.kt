package com.example.store.ui.customer

import androidx.lifecycle.ViewModel
import com.example.store.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(private val repository: Repository): ViewModel() {

}