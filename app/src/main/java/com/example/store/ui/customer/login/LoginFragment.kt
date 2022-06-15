package com.example.store.ui.customer.login

import android.content.Context
import android.content.SharedPreferences
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
import com.example.store.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)

        checkForLogin()

    }

    private fun checkForLogin() {
        val loginSharedPreferences: SharedPreferences? =
            activity?.getSharedPreferences("loginSharedPref", Context.MODE_PRIVATE)
        val editor = loginSharedPreferences?.edit()

        val id = loginSharedPreferences?.getString("id", "")
        val email = loginSharedPreferences?.getString("email", "")

        Log.d("DEBUG", id ?: "")

        if (id == "") {
            login(editor)
            goToSignIn()
        } else {
            val bundle = bundleOf("id" to id, "email" to email)
            findNavController().navigate(R.id.action_loginFragment_to_accountFragment, bundle)
        }
    }

    private fun login(editor: SharedPreferences.Editor?) {
        binding.loginBtn.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getCustomer(binding.emailIet.text.toString()).collect {
                        when (it) {
                            is Result.Success -> {
                                val id = it.data.id
                                val email = it.data.email
                                val bundle = bundleOf("id" to id.toString(), "email" to email)

                                editor?.apply {
                                    putString("id", id.toString())
                                    putString("email", email)
                                    apply()
                                }

                                findNavController().navigate(
                                    R.id.action_loginFragment_to_accountFragment,
                                    bundle
                                )
                            }
                            is Result.Error -> {
                                binding.emailIl.error = getString(R.string.invalid_email)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun goToSignIn() {
        binding.goToSignInTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signInFragment)
        }
    }
}