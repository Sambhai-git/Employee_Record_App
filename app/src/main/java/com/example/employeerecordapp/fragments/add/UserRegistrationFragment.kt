package com.example.employeerecordapp.fragments.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.employeerecordapp.R
import com.example.employeerecordapp.databinding.FragmentUserRegistraionBinding
import com.example.employeerecordapp.fragments.homepage.AdminMenu
import com.example.employeerecordapp.model.User
import com.example.employeerecordapp.viewmodel.AppViewModel
import com.example.employeerecordapp.viewmodel.MainViewModel
import com.example.employeerecordapp.viewmodel.MainViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.runBlocking

class UserRegistrationFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var appViewModel: AppViewModel
    private lateinit var binding: FragmentUserRegistraionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel = AppViewModel.getViewModel(requireContext())
        appViewModel.changeAppBarTitle("New Registration")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserRegistraionBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, MainViewModelFactory(requireContext()))[MainViewModel::class.java]

        setupTextChangeListeners()


        binding.registerButton.setOnClickListener {
            registerUser()
        }

        viewModel.isAdded.observe(viewLifecycleOwner) {
            if (it) {
                navigateBackToAdminMenu()
                viewModel.isAdded.postValue(false)
            }
        }

        return binding.root
    }

    private fun setupTextChangeListeners() {
        with(binding) {
            etName.doOnTextChanged { _, _, _, _ -> updateregisterbutton() }
            etEmail.doOnTextChanged { _, _, _, _ -> updateregisterbutton() }
            etUsername.doOnTextChanged { _, _, _, _ -> updateregisterbutton() }
            etPassword.doOnTextChanged { _, _, _, _ -> updateregisterbutton() }
            etConfirmPassword.doOnTextChanged { _, _, _, _ -> updateregisterbutton() }

            etName.addCharacterCountValidator(tilName, 3)
            etUsername.addCharacterCountValidator(tilUsername, 5)
            etEmail.addEmailValidator(tilEmail)
            etEmail.addUniquenessValidator(tilEmail, ::isUniqueEmail)
            etUsername.addUniquenessValidate(tilUsername, ::isUniqueUserName)
            etPassword.addCharacterCountValidator(tilPassword, 8)
            etConfirmPassword.addPasswordMatchValidator(tilConfirmPassword, etPassword)
        }
    }

    private fun updateregisterbutton() {
        val isNameValid = binding.etName.text.toString().length >= 3
        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()
        val isUserNameValid = binding.etUsername.text.toString().length >= 5 && binding.etUsername.error.isNullOrBlank()
        val isPasswordValid = binding.etPassword.text.toString().length >= 8
        val isConfirmPasswordValid = binding.etConfirmPassword.text.toString() == binding.etPassword.text.toString()

        runBlocking {
            val isUserNameUnique = isUniqueUserName(binding.etUsername.text.toString())
            val isEmailUnique = isUniqueEmail(binding.etEmail.text.toString())
            binding.registerButton.isEnabled = isNameValid && isEmailValid && isUserNameValid && isPasswordValid && isConfirmPasswordValid && isUserNameUnique && isEmailUnique
        }
    }

    private fun registerUser() {
        val name = binding.etName.text.toString()
        val username = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val user = User(username, email, name, password)
        viewModel.addUser(user)
    }

    private suspend fun isUniqueUserName(userName: String): Boolean {
        Log.d("Checking", "User name checked first")
        return viewModel.isUserNameUnique(userName)
    }

    private suspend fun isUniqueEmail(email: String): Boolean {

        return viewModel.isEmailUnique(email)
    }

    // Extensions for adding validators to EditText
    private fun EditText.addCharacterCountValidator(textInputLayout: TextInputLayout, minLength: Int) {
        doOnTextChanged { text, _, _, _ ->
            val isValid = (text?.length ?: 0) >= minLength
            textInputLayout.error = if (!isValid) {
                context.getString(R.string.error_min_character_count, minLength)
            } else {
                null
            }
        }
    }

    private fun EditText.addEmailValidator(textInputLayout: TextInputLayout) { doOnTextChanged { text, _, _, _ ->
            val isValid = text?.let { android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() }
            textInputLayout.error = if (!isValid!!) {
                context.getString(R.string.error_invalid_email)
            } else {
                null
            }
        }
    }

    fun EditText.addUniquenessValidator(textInputLayout: TextInputLayout, isUnique: suspend (String) -> Boolean) {
        doOnTextChanged { text, _, _, _ ->
            val isValid = text.isNullOrBlank() || runBlocking { isUnique(text.toString()) }
            textInputLayout.error = if (!isValid) {
                context.getString(R.string.error_email_not_unique)
            } else {
                null
            }
        }
    }

    fun EditText.addUniquenessValidate(textInputLayout: TextInputLayout, isUnique: suspend (String) -> Boolean) { doOnTextChanged { text, _, _, _ ->
            val isValid = text.isNullOrBlank() || runBlocking { isUnique(text.toString()) }
            textInputLayout.error = if (!isValid) {
                context.getString(R.string.error_not_unique)
            } else {
                null
            }
        }
    }

    private fun EditText.addPasswordMatchValidator(textInputLayout: TextInputLayout, otherEditText: EditText) { doOnTextChanged { text, _, _, _ ->
            val isValid = text.toString() == otherEditText.text.toString()
            textInputLayout.error = if (!isValid) {
                context.getString(R.string.error_passwords_do_not_match)
            } else {
                null
            }
        }
    }

    private fun navigateBackToAdminMenu() {
        // Use the FragmentManager to pop the current fragment from the back stack
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_nav_host, AdminMenu())
            .addToBackStack(null)
            .commit()
    }
}
