package com.example.store.ui.customer.signin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.data.model.customer.body.Billing
import com.example.store.data.model.customer.body.Customer
import com.example.store.data.model.customer.body.Shipping
import com.example.store.databinding.FragmentSignInBinding
import com.example.store.ui.customer.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment: Fragment(R.layout.fragment_sign_in) {

    private lateinit var binding: FragmentSignInBinding
    private val viewModel by viewModels<CustomerViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignInBinding.bind(view)

        goToLogin()

        createCustomer()
    }
    private fun createCustomer() {
        binding.signInBtn.setOnClickListener {
            lifecycleScope.launch{
                val customer = Customer(
                    first_name = binding.firstNameIet.text.toString(),
                    last_name = binding.lastNameIet.text.toString(),
                    email = binding.emailIet.text.toString(),
                    username = binding.userNameIet.text.toString(),
                    billing = Billing(email = binding.emailIet.text.toString())
                )
                viewModel.createCustomer(customer)
            }
            findNavController().navigate(R.id.action_signInFragment_to_accountFragment)
        }
    }

    private fun goToLogin() {
        binding.goToLoginTv.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_loginFragment)
        }
    }
}