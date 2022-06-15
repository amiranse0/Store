package com.example.store.ui.customer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.store.R
import com.example.store.data.Result
import com.example.store.databinding.FragmentAccountBinding
import com.example.store.ui.customer.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var binding: FragmentAccountBinding
    private var id: String = ""
    private var email: String = ""

    private val viewModel: AccountViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initValues(view)

        getCustomerData()

        goToInfoDialog()

        exitAccount()
    }

    private fun exitAccount() {
        binding.exitAccountLayout.setOnClickListener {
            Toast.makeText(requireContext(), "exit", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToInfoDialog() {
        binding.editInfoLayout.setOnClickListener {
            Toast.makeText(requireContext(), "info", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCustomerData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.getCustomer(email).collect{
                    viewModel.getCustomer(email).collect{
                        when(it){
                            is Result.Success -> {
                                binding.nameAndLastNameTitle.text = "${it.data.first_name} ${it.data.last_name}"
                                binding.emailInfoTv.text = it.data.email
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initValues(view: View) {
        binding = FragmentAccountBinding.bind(view)
        id = arguments?.getString("id") ?: ""
        email = arguments?.getString("email") ?: ""
    }
}