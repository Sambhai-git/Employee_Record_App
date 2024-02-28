package com.example.employeerecordapp.fragments.Login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.employeerecordapp.activity.MainActivity
import com.example.employeerecordapp.databinding.FragmentAdminLoginBinding
import com.example.employeerecordapp.viewmodel.AppViewModel

class AdminLogin : Fragment() {

    private lateinit var appViewModel: AppViewModel
    private lateinit var binding: FragmentAdminLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel = AppViewModel.getViewModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminLoginBinding.inflate(inflater, container, false)
        setupTextChangeListeners()

        binding.login.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                navigateToAdminHomePage()
            }
        }

        binding.login.setOnClickListener {
            if (isAdminCredentialsValid()) {
                navigateToAdminHomePage()
            } else {
                Toast.makeText(requireContext(), "Invalid Credentials", Toast.LENGTH_SHORT)
            }
        }

        return binding.root
    }

    private fun setupTextChangeListeners() {
        with(binding) {
            adminUsername.doOnTextChanged { _, _, _, _ -> updateLoginButtonState() }
            adminpassword.doOnTextChanged { _, _, _, _ -> updateLoginButtonState() }
        }
    }

    private fun updateLoginButtonState() {
        val isUsernameEmpty =
            binding.adminUsername.text.toString().filter { !it.isWhitespace() }.isEmpty()
        val isPasswordEmpty = binding.adminpassword.text.toString().isEmpty()
        binding.login.isEnabled = !isUsernameEmpty && !isPasswordEmpty
    }

    private fun isAdminCredentialsValid(): Boolean {
        return binding.adminUsername.text.toString() == "admin" && binding.adminpassword.text.toString() == "admin"
    }

    private fun navigateToAdminHomePage() {
        Toast.makeText(context, "Hello, moving to admin home page", Toast.LENGTH_LONG).show()
        appViewModel.userAdmin(true)
        appViewModel.isLoggedIn()
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }
}
