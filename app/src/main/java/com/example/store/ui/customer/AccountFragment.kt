package com.example.store.ui.customer

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.data.Result
import com.example.store.data.model.customer.body.Customer
import com.example.store.databinding.AreYouSureDialogBinding
import com.example.store.databinding.EditInfoDialogBinding
import com.example.store.databinding.FragmentAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var binding: FragmentAccountBinding
    private var id: String = ""
    private var email: String = ""
    private var username: String = ""
    private var name: String = ""
    private var lastName: String = ""

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
            val loginSharedPref = activity?.getSharedPreferences("loginSharedPref", Context.MODE_PRIVATE)
            val editor = loginSharedPref?.edit()

            val bindingExitDialog = AreYouSureDialogBinding.inflate(layoutInflater)
            val exitDialog = Dialog(requireContext())
            exitDialog.setContentView(bindingExitDialog.root)
            exitDialog.window?.setLayout(700, 550)
            exitDialog.window?.attributes?.gravity = Gravity.CENTER
            exitDialog.show()

            bindingExitDialog.iAmNotSure.setOnClickListener {
                exitDialog.dismiss()
            }
            bindingExitDialog.iAmSureBtn.setOnClickListener {
                editor?.apply {
                    putString("id", "")
                    putString("email", "")
                    apply()
                }
                findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
                exitDialog.dismiss()
            }
        }
    }

    private fun goToInfoDialog() {
        binding.editInfoLayout.setOnClickListener {
            val bindingDialog = EditInfoDialogBinding.inflate(layoutInflater)

            val editDialogFragment =
                Dialog(requireContext(), androidx.transition.R.style.Base_ThemeOverlay_AppCompat)
            editDialogFragment.setContentView(bindingDialog.root)
            editDialogFragment.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            bindingDialog.lastNameInputEd.setText(lastName)
            bindingDialog.nameInputEd.setText(name)

            editDialogFragment.show()

            bindingDialog.dismissBtn.setOnClickListener {
                editDialogFragment.dismiss()
            }

            bindingDialog.submitInfoBtn.setOnClickListener {
                val customer = Customer(
                    email = email,
                    firstName = bindingDialog.nameInputEd.text.toString(),
                    last_name = bindingDialog.lastNameInputEd.text.toString(),
                    username = username
                )

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                        viewModel.updateCustomer(id, customer).collect{
                            when(it){
                                is Result.Success ->{
                                    binding.nameAndLastNameTitle.text = "${it.data.firstName} ${it.data.lastName}"
                                }
                                is Result.Error ->{
                                    Log.d("ERROR", "${it.exception}")
                                }
                            }
                        }
                    }
                }

                editDialogFragment.dismiss()
            }
        }
    }

    private fun getCustomerData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getCustomer(email).collect {
                    viewModel.getCustomer(email).collect {
                        when (it) {
                            is Result.Success -> {
                                username = it.data.username
                                lastName = it.data.lastName
                                name = it.data.firstName
                                binding.nameAndLastNameTitle.text =
                                    "${it.data.firstName} ${it.data.lastName}"
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