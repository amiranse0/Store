package com.example.store.ui.customer.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentLoginBinding
import com.example.store.ui.customer.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)

        goToSignIn()


    }

    private fun goToSignIn() {
        binding.goToSignInTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signInFragment)
        }
    }
}