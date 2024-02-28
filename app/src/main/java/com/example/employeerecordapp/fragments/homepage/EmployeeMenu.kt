package com.example.employeerecordapp.fragments.homepage

import AppViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.employeerecordapp.R
import com.example.employeerecordapp.Utility.SwipeToEditAndDeleteCallback
import com.example.employeerecordapp.databinding.FragmentListRecordBinding
import com.example.employeerecordapp.fragments.add.EmployeeAddFragment
import com.example.employeerecordapp.fragments.read.EmployeeCardAdapter
import com.example.employeerecordapp.fragments.read.EmployeeDetailsFragment
import com.example.employeerecordapp.fragments.update.UpdateEmployee
import com.example.employeerecordapp.model.EmployeeR
import com.example.employeerecordapp.viewmodel.AppViewModel
import com.example.employeerecordapp.viewmodel.MainViewModel
import com.example.employeerecordapp.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar

class EmployeeMenu : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var appViewModel: AppViewModel
    private lateinit var binding: FragmentListRecordBinding
    private lateinit var adapter: EmployeeCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel = AppViewModel.getViewModel(requireContext())
        val username = appViewModel.getSavedUsername()
        appViewModel.changeAppBarTitle("Welcome $username")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListRecordBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(requireContext())
        )[MainViewModel::class.java]
        appViewModel =
            ViewModelProvider(this, AppViewModelFactory(requireContext()))[AppViewModel::class.java]

        setupViews()
        setupObservers()

        return binding.root
    }

    private fun setupViews() {
        binding.floatingActionButton.setOnClickListener {
            navigateToEmployeeAddFragment()
        }

        // SearchView setup
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchEmployees(newText.orEmpty(), 0)
                return true
            }
        })

        // Spinner setup
        val sortOptions = arrayOf("Sort by Name", "Sort by Token ID")
        val spinnerAdapter =ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sortSpinner.adapter = spinnerAdapter

        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {

                if(position==0)
                {
                    viewModel.getEmployeesSorted("Name")
                }
                else if(position==1)
                {
                    viewModel.getEmployeesSorted("TokenID")
                }
//                val selectedSortOption = sortOptions[position]
//                viewModel.getEmployeesSorted(selectedSortOption)
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing here
            }
        }
    }

    private fun setupObservers() {
        adapter = EmployeeCardAdapter(onEmployeeClick)
        ItemTouchHelper(object :
            SwipeToEditAndDeleteCallback<EmployeeCardAdapter>(requireContext(), adapter = adapter) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.absoluteAdapterPosition

                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                        // this method is called when we swipe our item to right direction.
                        // on below line we are getting the item at a particular position.
                        val employeeDetail: EmployeeR =
                            adapter.getItemAtPosition(viewHolder.absoluteAdapterPosition)
                        // this method is called when item is swiped.
                        // below line is to remove item from our array list.
                        adapter.list.removeAt(viewHolder.absoluteAdapterPosition)

                        // below line is to notify our item is removed from adapter.
                        adapter.notifyItemRemoved(viewHolder.absoluteAdapterPosition)


                        val snackbar = Snackbar.make(
                            binding.recyclerview,
                            "Deleted " + employeeDetail.name,
                            Snackbar.LENGTH_LONG
                        )
                            .setAction(
                                "Undo"
                            ) { _ ->
                                // adding on click listener to our action of snack bar.
                                // below line is to add our item to array list with a position.
                                adapter.list.add(position, employeeDetail)

                                // below line is to notify item is
                                // added to our adapter class.
                                adapter.notifyItemInserted(position)
                            }

                        snackbar.show()
                        snackbar.addCallback(object : Snackbar.Callback() {

                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                if (event == DISMISS_EVENT_TIMEOUT) {
                                    viewModel.deleteEmployee(employeeDetail)
                                }
                            }
                        })
                    }

                    ItemTouchHelper.LEFT -> {
                        val employeeToEdit: EmployeeR = adapter.getItemAtPosition(position)
                        editEmployee(employeeToEdit)

                    }

                }


            }

        }).attachToRecyclerView(binding.recyclerview)
        binding.recyclerview.adapter = adapter
        viewModel.getEmployeeList.observe(viewLifecycleOwner) { employee ->
            employee?.let {
                adapter.updateList(it)
            }
        }
    }

    private fun editEmployee(employeeToEdit: EmployeeR) {

        val updateEmployeeDetailsFragment = UpdateEmployee.newInstance(employeeToEdit)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_nav_host, updateEmployeeDetailsFragment)
            .addToBackStack(null)
            .commit()

    }

    private val onEmployeeClick: (employeeDetail: EmployeeR) -> Unit = {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val employeeDetailsFragment = EmployeeDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable("employeeDetail", it)
            }
        }
        appViewModel.changeAppBarTitle("Details of ${it.name}")
        fragmentTransaction.replace(R.id.main_nav_host, employeeDetailsFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToEmployeeAddFragment() {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_nav_host, EmployeeAddFragment())
            .addToBackStack(null)
            .commit()
    }

    fun showRecordAddedSnackbar() {
        Snackbar.make(binding.root, "Record added successfully", Snackbar.LENGTH_SHORT).show()
    }




}
