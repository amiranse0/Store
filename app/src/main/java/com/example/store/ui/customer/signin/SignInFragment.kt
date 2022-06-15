package com.example.store.ui.customer.signin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.data.Result
import com.example.store.data.model.customer.body.Billing
import com.example.store.data.model.customer.body.Customer
import com.example.store.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.HttpException

@AndroidEntryPoint
class SignInFragment: Fragment(R.layout.fragment_sign_in) {

    private lateinit var binding: FragmentSignInBinding
    private val viewModel by viewModels<SignInViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignInBinding.bind(view)

        goToLogin()

        createCustomer()
    }
    private fun createCustomer() {
        binding.signInBtn.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch{
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                    val customer = Customer(
                        firstName = binding.firstNameIet.text.toString(),
                        last_name = binding.lastNameIet.text.toString(),
                        email = binding.emailIet.text.toString(),
                        username = binding.userNameIet.text.toString(),
                        billing = Billing(email = binding.emailIet.text.toString())
                    )
                    viewModel.createCustomer(customer).collect{
                        when(it){
                            is Result.Error -> {
                                    binding.userNameIl.error = getString(R.string.invalid_user_name_and_email)
                            }
                            is Result.Success -> {
                                val id = it.data.id
                                val email = it.data.email
                                val bundle = bundleOf("id" to id.toString(), "email" to email)
                                findNavController().navigate(R.id.action_signInFragment_to_accountFragment, bundle)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun goToLogin() {
        binding.goToLoginTv.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_loginFragment)
        }
    }
}