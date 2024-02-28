package com.example.employeerecordapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import com.example.employeerecordapp.R
import com.example.employeerecordapp.databinding.ActivityMainBinding
import com.example.employeerecordapp.fragments.homepage.AdminMenu
import com.example.employeerecordapp.fragments.homepage.EmployeeMenu
import com.example.employeerecordapp.viewmodel.AppViewModel

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var appViewModel: AppViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        appViewModel = AppViewModel.getViewModel(this)

        if (appViewModel.isAdmin()) {
            supportFragmentManager.commit {
                replace(R.id.main_nav_host, AdminMenu())
                addToBackStack(null)
            }
        } else {
            supportFragmentManager.commit {
                replace(R.id.main_nav_host, EmployeeMenu())
                addToBackStack(null)
            }
        }

        binding.logout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        appViewModel.title.observe(this) { title ->
            binding.toolbarTitle.text = title
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.main_nav_host)

        if (currentFragment is AdminMenu || currentFragment is EmployeeMenu) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")

        // Positive button
        builder.setPositiveButton("Yes") { _, _ ->
            // User clicked Yes, perform logout
            startActivity(Intent(this, AuthenticationActivity::class.java))
            if (appViewModel.isAdmin()) {
                appViewModel.isLoggedOut()
            } else {
                appViewModel.clearUserLoginState()
            }
            finish()
        }
        // Negative button
        builder.setNegativeButton("No") { _, _ ->

        }
        // Show the dialog
        val dialog = builder.create()
        dialog.show()
    }

}