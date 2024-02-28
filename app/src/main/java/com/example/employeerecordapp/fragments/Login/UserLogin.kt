package com.example.employeerecordapp.fragments.Login

import AppViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.employeerecordapp.activity.MainActivity
import com.example.employeerecordapp.databinding.FragmentUserLoginBinding
import com.example.employeerecordapp.viewmodel.AppViewModel
import com.example.employeerecordapp.viewmodel.MainViewModel
import com.example.employeerecordapp.viewmodel.MainViewModelFactory

class UserLogin : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var appViewModel: AppViewModel
    private lateinit var binding: FragmentUserLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserLoginBinding.inflate(inflater, container, false)

        appViewModel =
            ViewModelProvider(this, AppViewModelFactory(requireContext()))[AppViewModel::class.java]
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(requireContext())
        )[MainViewModel::class.java]

        setupTextChangeListeners()

        binding.login.setOnClickListener {
            val username = binding.userUsername.text.toString()
            val password = binding.userpassword.text.toString()

            viewModel.authenticateUser(username, password)
        }

        viewModel.isAuthenticated.observe(viewLifecycleOwner) { result ->

            when {
                result.isSuccess -> {
                    result.onSuccess {
                        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT)
                            .show()

                        appViewModel.saveUserLoginState(it.name)

                        navigateToListFragment()

                    }
                }

                result.isFailure -> {
                    result.onFailure {
                        Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        return binding.root
    }

    private fun setupTextChangeListeners() {
        with(binding) {
            userUsername.doOnTextChanged { _, _, _, _ -> updateLoginButtonState() }
            userpassword.doOnTextChanged { _, _, _, _ -> updateLoginButtonState() }
        }
    }

    private fun updateLoginButtonState() {
        val isUsernameEmpty = binding.userUsername.text.toString().isBlank()
        val isPasswordEmpty = binding.userpassword.text.toString().isBlank()
        binding.login.isEnabled = !isUsernameEmpty && !isPasswordEmpty
    }

    private fun navigateToListFragment() {
        // Use the FragmentManager to replace the current fragment with EmployeeMenu
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
