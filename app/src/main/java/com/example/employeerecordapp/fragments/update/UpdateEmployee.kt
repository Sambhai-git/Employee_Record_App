package com.example.employeerecordapp.fragments.update

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.employeerecordapp.databinding.FragmentUpdateRecordBinding
import com.example.employeerecordapp.model.EmployeeR
import com.example.employeerecordapp.viewmodel.AppViewModel
import com.example.employeerecordapp.viewmodel.MainViewModel
import com.example.employeerecordapp.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar


class UpdateEmployee : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var appViewModel: AppViewModel
    private lateinit var binding: FragmentUpdateRecordBinding
    private lateinit var employee: EmployeeR

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpdateRecordBinding.inflate(inflater, container, false)
        appViewModel = AppViewModel.getViewModel(requireContext())
        appViewModel.changeAppBarTitle("Update Employee Details")
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(requireContext())
        )[MainViewModel::class.java]
        employee = arguments?.getParcelable("employee")!!
        setupUI(employee)
        setupTextChangeListeners()

        binding.addBtn.setOnClickListener {
            updateRecord(employee)
        }

        return binding.root
    }

    private fun setupTextChangeListeners() {

        binding.addEmailEt.doOnTextChanged { _, _, _, _ -> updateAddButton() }
        binding.addCityEt.doOnTextChanged { _, _, _, _ -> updateAddButton() }
        binding.addDesignationEt.doOnTextChanged { _, _, _, _ -> updateAddButton() }
        binding.addMobileEt.doOnTextChanged { _, _, _, _ -> updateAddButton() }

    }

    private fun updateRecord(employee: EmployeeR) {


        val phoneNo = binding.addMobileEt.text.toString().toLong()
        val designation = binding.addDesignationEt.text.toString()
        val emailId = binding.addEmailEt.text.toString()
        val city = binding.addCityEt.text.toString()
        val Modifiedby = appViewModel.getSavedUsername()

        Modifiedby?.let {
            employee.copy(
                phoneNo = phoneNo,
                designation = designation,
                emailId = emailId,
                city = city,
                lastmod = it
            )
        }
            ?.let {
                viewModel.updateEmployee(

                    it,
                    onSuccess = {
                        showUpdateSnackbar()
                        navigateBackToEmployeeMenu()

                    }

                ) {


                }
            }

    }

    private fun navigateBackToEmployeeMenu() {
        parentFragmentManager.popBackStack()
    }

    private fun showUpdateSnackbar() {
        Snackbar.make(binding.root, "Record updated successfully", Snackbar.LENGTH_SHORT).show()
    }


    private fun setupUI(employee: EmployeeR) {
        with(binding)
        {
            NameEt.setText(employee.name)
            addMobileEt.setText(employee.phoneNo.toString())
            //  addMobileEt.setText(employee.phoneNo as CharSequence)
            addDesignationEt.setText(employee.designation)
            addEmailEt.setText(employee.emailId)
            addCityEt.setText(employee.city)
            bloodgroupEt.setText(employee.bloodGroup)
            addTokenIDEt.setText(employee.tokenId.toString())
            btnSelectDOB.setText(employee.dob)
            btnSelectDOJ.setText(employee.doj)
        }


    }

    private fun updateAddButton() {
        with(binding) {

            val email = binding.addEmailEt.text.toString()
            val phone = binding.addMobileEt.text.toString()
            val designation = binding.addDesignationEt.text.toString()
            val city = binding.addCityEt.text.toString()


            if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                addEmailEt.error = "Invalid email format"
            } else {
                addEmailEt.error = null
            }

            if (phone.isNotBlank() && phone.length < 10) {
                addMobileEt.error = "Enter 10 digits"
            } else {
                addMobileEt.error = null
            }

            val isEmailValid = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
            val isPhoneValid = phone.isNotBlank() && phone.length == 10
            val isDesignationValid = designation.isNotBlank()
            val isCityValid = city.isNotBlank()
            addBtn.isEnabled = isEmailValid && isDesignationValid && isPhoneValid && isCityValid

        }
    }


    companion object {
        private const val ARG_EMPLOYEE = "employee"

        fun newInstance(employee: EmployeeR): UpdateEmployee {
            val args = Bundle().apply {
                putParcelable(ARG_EMPLOYEE, employee)
            }
            val fragment = UpdateEmployee()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appViewModel.changeAppBarTitle("Welcome ${appViewModel.getSavedUsername()}")
    }


}