package com.example.employeerecordapp.fragments.homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.employeerecordapp.R
import com.example.employeerecordapp.Utility.SwipeToEditAndDeleteCallback
import com.example.employeerecordapp.databinding.FragmentAdminMenuBinding
import com.example.employeerecordapp.fragments.add.UserRegistrationFragment
import com.example.employeerecordapp.fragments.read.UserAdapter
import com.example.employeerecordapp.fragments.update.UpdateUser
import com.example.employeerecordapp.model.User
import com.example.employeerecordapp.viewmodel.AppViewModel
import com.example.employeerecordapp.viewmodel.MainViewModel
import com.example.employeerecordapp.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar

class AdminMenu : Fragment() {

    private lateinit var binding: FragmentAdminMenuBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var appViewModel: AppViewModel

    private val TAG = "AdminMenu"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminMenuBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(requireContext())
        )[MainViewModel::class.java]
        appViewModel = AppViewModel.getViewModel(requireContext())
        appViewModel.changeAppBarTitle("Welcome Admin")
        userAdapter = UserAdapter(requireContext(), object : UserAdapter.OnUserClickListener {
            override fun onEditUser(user: User) {
                // Handle edit action
                editUser(user)
            }

            override fun onDeleteUser(user: User) {
                // Handle delete action
                showDeleteConfirmationDialog(user)
            }
        })
        binding.recyclerview.adapter = userAdapter

        viewModel.getUserList.observe(viewLifecycleOwner) { userList ->
            Log.d(TAG, "onCreateView: getUserList ")
            userAdapter.updateList(userList)
        }

        ItemTouchHelper(object :
            SwipeToEditAndDeleteCallback<UserAdapter>(requireContext(), userAdapter) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.absoluteAdapterPosition

                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                        // this method is called when we swipe our item to right direction.
                        // on below line we are getting the item at a particular position.
                        val userDeletedData: User =
                            adapter.getUserAtPosition(viewHolder.absoluteAdapterPosition)
                        // this method is called when item is swiped.
                        // below line is to remove item from our array list.
                        adapter.userList.removeAt(viewHolder.absoluteAdapterPosition)

                        // below line is to notify our item is removed from adapter.
                        adapter.notifyItemRemoved(viewHolder.absoluteAdapterPosition)

                        val snackBar = Snackbar.make(
                            binding.recyclerview,
                            "Deleted " + userDeletedData.userName,
                            Snackbar.LENGTH_LONG
                        ).setAction(
                            "Undo"
                        ) { _ ->
                            // adding on click listener to our action of snack bar.
                            // below line is to add our item to array list with a position.
                            adapter.userList.add(position, userDeletedData)

                            // below line is to notify item is
                            // added to our adapter class.
                            adapter.notifyItemInserted(position)
                        }

                        snackBar.show()
                        snackBar.addCallback(object : Snackbar.Callback() {

                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                if (event == DISMISS_EVENT_TIMEOUT) {
                                    viewModel.deleteUser(userDeletedData)
                                }
                            }
                        });
                    }

                    ItemTouchHelper.LEFT -> {
                        // Swipe left: Edit item
                        // Implement your edit logic here
                        val userToEdit: User = adapter.getUserAtPosition(position)
                        editUser(userToEdit)

                    }

                }


            }

        }).attachToRecyclerView(binding.recyclerview)



        return binding.root
    }

    private fun deleteUser(user: User) {
        viewModel.deleteUser(user)
        showUndoDeleteSnackbar(user)
    }

    private fun editUser(user: User) {
        val updateFragment = UpdateUser.newInstance(user)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_nav_host, updateFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showDeleteConfirmationDialog(user: User) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete User")
            .setMessage("Are you sure you want to delete this user?")
            .setPositiveButton("Delete") { _, _ ->
                deleteUser(user)
            }
            .setNegativeButton("Cancel", null)
            .show()

    }

    private fun showUndoDeleteSnackbar(deletedUser: User) {
        val snackbar = Snackbar.make(
            binding.root,
            "User deleted",
            Snackbar.LENGTH_LONG
        )

        snackbar.setAction("Undo") {
            // Undo delete action
            viewModel.addUser(deletedUser)

        }

        snackbar.show()
    }

    override fun onResume() {
        super.onResume()
        binding.floatingActionButton.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_nav_host, UserRegistrationFragment())
                .addToBackStack(null).commit()
        }
    }
}
