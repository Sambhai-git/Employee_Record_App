package com.example.employeerecordapp.fragments.add

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.employeerecordapp.R
import com.example.employeerecordapp.Utility.CustomSpinnerAdapter
import com.example.employeerecordapp.databinding.FragmentEmployeeAddBinding
import com.example.employeerecordapp.model.EmployeeR
import com.example.employeerecordapp.viewmodel.AppViewModel
import com.example.employeerecordapp.viewmodel.MainViewModel
import com.example.employeerecordapp.viewmodel.MainViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.employeerecordapp.fragments.homepage.EmployeeMenu
import com.google.android.material.snackbar.Snackbar

class EmployeeAddFragment : Fragment() {

    private lateinit var appViewModel: AppViewModel
    private lateinit var binding: FragmentEmployeeAddBinding
    private lateinit var mainViewModel: MainViewModel
    private var selectedDOB: Calendar? = null
    private var selectedDOJ: Calendar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(requireContext()))[MainViewModel::class.java]
        binding = FragmentEmployeeAddBinding.inflate(inflater, container, false)
        appViewModel = AppViewModel.getViewModel(requireContext())
        appViewModel.changeAppBarTitle("New Record Addition")
        binding.btnSelectDOB.setOnClickListener {
            onSelectDOB()
        }

        mainViewModel.employeeAddedSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                // Show Snackbar here
                Snackbar.make(binding.root, "Employee record added successfully", Snackbar.LENGTH_SHORT).show()
                // Reset the success state in ViewModel
                mainViewModel.setEmployeeAddedSuccess(false)
            }
        }

        binding.btnSelectDOJ.setOnClickListener {
            showDOJDatePicker()
        }

        val bloodGroupList = resources.getStringArray(R.array.blood_groups)
        val bloodGroupAdapter = CustomSpinnerAdapter(requireContext(), bloodGroupList.toList())
        binding.bloodgroupEt.adapter = bloodGroupAdapter

        binding.bloodgroupEt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener { override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        setupTextChangeListeners()

        with(binding) {
            // Disable all fields initially
            addMobileEt.isEnabled = false
            addDesignationEt.isEnabled = false
            addEmailEt.isEnabled = false
            bloodgroupEt.isEnabled = false
            addTokenIDEt.isEnabled = false
            addCityEt.isEnabled = false
            btnSelectDOB.isEnabled = false
            btnSelectDOJ.isEnabled=false

            addFirstNameEt.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT ) {
                    addMobileEt.isEnabled = true
                    addMobileEt.requestFocus()
                    true
                } else {
                    false
                }
            }

            addMobileEt.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT ) {
                    addDesignationEt.isEnabled = true
                    addDesignationEt.requestFocus()
                    true
                } else {
                    false
                }
            }

            addDesignationEt.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    addEmailEt.isEnabled = true
                    addEmailEt.requestFocus()
                    true
                } else {
                    false
                }
            }

            addEmailEt.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT && Patterns.EMAIL_ADDRESS.matcher(addEmailEt.text.toString()).matches()) {
                    bloodgroupEt.isEnabled = true
                    bloodgroupEt.requestFocus()
                    true
                } else {
                    false
                }
            }

            bloodgroupEt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (position > 0) {
                        addTokenIDEt.isEnabled = true
                        addTokenIDEt.requestFocus()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected, if needed
                }
            }

            addTokenIDEt.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    addCityEt.isEnabled = true
                    addCityEt.requestFocus()
                    true
                } else {
                    false
                }
            }

            // Enable DOB field if all previous fields are valid
            addCityEt.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    btnSelectDOB.isEnabled = true
                    btnSelectDOB.requestFocus()
                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(addCityEt.windowToken, 0)
                    true
                } else {
                    false
                }
            }

            // Enable DOJ field if DOB is valid
            btnSelectDOB.setOnClickListener {
                onSelectDOB()
            }

            // Enable the button if all conditions are met
            btnSelectDOJ.setOnClickListener {
                showDOJDatePicker()
            }

        }


        return binding.root
    }

    var uniqueemaild: Boolean=true
    var uniquetokenid : Boolean =true
    var uniquephoneNo: Boolean =true



    private fun setupTextChangeListeners() {
        with(binding) {
            addFirstNameEt.doOnTextChanged { _, _, _, _ ->
                updateAddButton()
            }
            addMobileEt.doOnTextChanged { _, _, _, _ ->
                updateAddButton()
            }
            addDesignationEt.doOnTextChanged { _, _, _, _ ->
                updateAddButton()
            }
            addCityEt.doOnTextChanged { _, _, _, _ ->
                updateAddButton()
            }
            addTokenIDEt.doOnTextChanged { _, _, _, _ ->
                updateAddButton()
            }
            addEmailEt.doOnTextChanged { _, _, _, _ ->
                updateAddButton()
            }
        }
    }
    private fun updateAddButton() {
        with(binding) {
            val name = addFirstNameEt.text.toString()
            val mobile = addMobileEt.text.toString()
            val designation = addDesignationEt.text.toString()
            val email = addEmailEt.text.toString()
            val bloodGroup = bloodgroupEt.selectedItem.toString()
            val tokenId = addTokenIDEt.text.toString().toLongOrNull()
            val city = addCityEt.text.toString()
            val dob = selectedDOB?.let { SimpleDateFormat("dd-MM-yyyy", Locale.US).format(it.time) } ?: ""
            val doj = selectedDOJ?.let { SimpleDateFormat("dd-MM-yyyy", Locale.US).format(it.time) } ?: " "

            // Validate email format and show warning if invalid
            if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                addEmailEt.error = "Invalid email format"
            } else {
                addEmailEt.error = null  // Reset the error
            }

            // Check if email already exists in the database and show warning if found
            lifecycleScope.launch {
                if (email.isNotBlank() && mainViewModel.isEmailExists(email)) {
                    addEmailEt.error = "Email already exists"
                    uniqueemaild=false
                } else {
                    addEmailEt.error = null
                    uniqueemaild=true
                }
            }


            // Check if phone number already exists in the database and show warning if found
            lifecycleScope.launch {
                if (mobile.isNotBlank() && mainViewModel.isPhoneNoExists(mobile.toLong())) {
                    addMobileEt.error = "Phone number already exists"
                    uniquephoneNo=false
                } else {
                    addMobileEt.error = null
                    uniquephoneNo=true
                }
            }

            // Check if tokenId is already in the database and show warning if found
            if (tokenId != null && mainViewModel.isTokenIdExists(tokenId)) {
                addTokenIDEt.error = "Token ID already exists"
                uniquetokenid=false
            } else {
                addTokenIDEt.error = null
                uniquetokenid=true
            }

            // Check if all fields are valid and not empty
            val isNameValid = name.isNotBlank()
            val isMobileValid = mobile.isNotBlank() && uniquephoneNo
            val isEmailValid = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && uniqueemaild
            val isTokenIdValid = tokenId != null  && uniquetokenid
            val isCityValid = city.isNotBlank()
            val isDobValid = selectedDOB != null
            val isDojValid = selectedDOJ != null

            // Enable the button only if all conditions are met
            addBtn.isEnabled = isNameValid && isMobileValid && isEmailValid && isTokenIdValid && isCityValid && isDobValid && isDojValid

            if (addBtn.isEnabled) {
                // Handle the button click to add the employee to the database
                addBtn.setOnClickListener {
                    val adminName: String = appViewModel.getSavedUsername() ?: "Admin" // Default to "Admin" if not found
                    val employee = EmployeeR(tokenId = tokenId!!, name = name, phoneNo = mobile.toLong(), designation = designation, bloodGroup = bloodGroup, emailId = email, emp = adminName, dob = dob, city = city, doj = doj, lastmod = adminName)
                    mainViewModel.insertEmployee(employee)
                    requireActivity().onBackPressed()
                    mainViewModel.setEmployeeAddedSuccess(true)
                  //  Toast.makeText(requireContext(),"Record Added Successful",Toast.LENGTH_SHORT).show()
                }
            } else {
                // Disable the button if any condition is not met
                addBtn.setOnClickListener(null)
            }
        }
    }




    private fun onSelectDOB() {
        val currentDate = Calendar.getInstance()
        val minDate = Calendar.getInstance().apply { add(Calendar.YEAR, -100) }
        val maxDate = Calendar.getInstance().apply { add(Calendar.YEAR, -18) }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                selectedDOB = Calendar.getInstance().apply {
                    set(year, month, day)
                }

                // Update the TextView with the selected date
                val formattedDate =
                    selectedDOB?.time?.let {
                        SimpleDateFormat("dd-MM-YYYY", Locale.US).format(
                            it
                        )
                    }
                binding.btnSelectDOB.text = "DOB : ${formattedDate}"

                // Clear Date of Joining when Date of Birth is changed
                binding.btnSelectDOJ.text = "Date of Joining"
                binding.btnSelectDOJ.isEnabled = true
                binding.btnSelectDOJ.requestFocus()
                updateAddButton()

            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        )



        // Set the range to allow dates between 100 years ago and 18 years ago from today
        datePickerDialog.datePicker.minDate = minDate.timeInMillis
        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

        // Show the date picker dialog
        datePickerDialog.setTitle("Select Date of Birth")
        datePickerDialog.show()
    }

    private fun showDOJDatePicker() {
        val currentDate = Calendar.getInstance()

        // Check if a valid DOB is selected
        selectedDOB?.let { dob ->
            val minDOJDate = Calendar.getInstance().apply { timeInMillis = dob.timeInMillis }
            minDOJDate.add(Calendar.YEAR, 18) // Minimum DOJ date is 18 years after DOB
            val maxDOJDate = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val selectedDate = Calendar.getInstance().apply {
                        set(year, month, day)
                    }
                    // Update the TextView with the selected date
                    val formatdate = SimpleDateFormat("dd-MM-YYYY", Locale.US).format(selectedDate.time)
                    binding.btnSelectDOJ.text = "DOJ : $formatdate"
                    selectedDOJ = selectedDate
                    updateAddButton()
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
            )

            // Set the range for Date of Joining
            datePickerDialog.datePicker.minDate = minDOJDate.timeInMillis
            datePickerDialog.datePicker.maxDate = maxDOJDate.timeInMillis

            // Show the date picker dialog
            datePickerDialog.setTitle("Select Date of Joining")
            datePickerDialog.show()
        } ?: run {
            // If DOB is not selected, set DOJ to null
            binding.btnSelectDOJ.text = "Date of Joining"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appViewModel.changeAppBarTitle("Welcome ${appViewModel.getSavedUsername()}")
    }
}
