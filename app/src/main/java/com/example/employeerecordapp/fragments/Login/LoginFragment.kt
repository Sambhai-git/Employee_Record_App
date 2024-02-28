package com.example.employeerecordapp.fragments.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.employeerecordapp.R
import com.example.employeerecordapp.databinding.FragmentHomeBinding
import com.example.employeerecordapp.viewmodel.MainViewModel
import com.example.employeerecordapp.viewmodel.MainViewModelFactory

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(requireContext())
        )[MainViewModel::class.java]

        binding.btnAdminLogin.setOnClickListener {
            Toast.makeText(context, "Hello, moving to admin login page", Toast.LENGTH_LONG).show()
            navController.navigate(R.id.action_loginFragment_to_adminLogin4)
        }

        binding.btnLoginuser.setOnClickListener {
            Toast.makeText(context, "Moving to User Login Page", Toast.LENGTH_LONG).show()
            navController.navigate(R.id.action_loginFragment_to_userLogin)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }
}
