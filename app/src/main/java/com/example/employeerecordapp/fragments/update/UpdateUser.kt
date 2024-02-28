package com.example.employeerecordapp.fragments.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.employeerecordapp.databinding.FragmentUpdateUserBinding
import com.example.employeerecordapp.model.User
import com.example.employeerecordapp.viewmodel.AppViewModel
import com.example.employeerecordapp.viewmodel.MainViewModel
import com.example.employeerecordapp.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar

class UpdateUser : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentUpdateUserBinding
    private lateinit var currentUser: User
    private lateinit var appViewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateUserBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(requireContext())
        )[MainViewModel::class.java]
        appViewModel = AppViewModel.getViewModel(requireContext())
        appViewModel.changeAppBarTitle("User Credential Update")

        currentUser = arguments?.getParcelable(ARG_USER) ?: User("", "", "", "")

        setupUI(currentUser)

        binding.registerButton.setOnClickListener {
            updateCredentials()
        }

        setupTextChangeListeners()

        return binding.root
    }

    private fun setupUI(user: User) {
        binding.etUsername.setText(user.userName)
        binding.etEmail.setText(user.emailId)
    }


    private fun updateCredentials() {
        val newUsername = binding.etUsername.text.toString()
        val newPassword = binding.etPassword.text.toString()
        val confirmNewPassword = binding.etConfirmPassword.text.toString()

        binding.tilUsername.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null

        if (newPassword.length < 8) {
            binding.tilPassword.error = "Password must be at least 8 characters long."
            return
        }

        if (currentUser.password.isNotEmpty() && newPassword != confirmNewPassword) {
            binding.tilConfirmPassword.error = "Passwords do not match."
            return
        }

        viewModel.updateUser(
            currentUser.copy(userName = newUsername, password = newPassword),
            onSuccess = {
                showUpdateSnackbar()
                navigateBackToAdminMenu()
            },
            onError = { errorMessage ->
                binding.tilUsername.error = errorMessage
            }
        )
    }

    private fun showUpdateSnackbar() {
        Snackbar.make(binding.root, "Credentials updated successfully", Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun navigateBackToAdminMenu() {
        parentFragmentManager.popBackStack()
    }

    private fun setupTextChangeListeners() {
        binding.etPassword.doOnTextChanged { _, _, _, _ ->
            enableUpdateButton()
        }

        binding.etConfirmPassword.doOnTextChanged { _, _, _, _ ->
            enableUpdateButton()
        }
    }

    private fun enableUpdateButton() {
        val newPassword = binding.etPassword.text.toString()
        val confirmNewPassword = binding.etConfirmPassword.text.toString()
        binding.registerButton.isEnabled =
            newPassword.length >= 8 && newPassword == confirmNewPassword
    }

    override fun onDestroy() {
        super.onDestroy()
        appViewModel.changeAppBarTitle("Welcome Admin")
    }

    companion object {
        private const val ARG_USER = "user"

        fun newInstance(user: User): UpdateUser {
            val args = Bundle().apply {
                putParcelable(ARG_USER, user)
            }
            val fragment = UpdateUser()
            fragment.arguments = args
            return fragment
        }
    }
}
