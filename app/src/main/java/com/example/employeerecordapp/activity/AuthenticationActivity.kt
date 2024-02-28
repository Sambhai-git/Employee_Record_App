package com.example.employeerecordapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.employeerecordapp.R
import com.example.employeerecordapp.viewmodel.AppViewModel

class AuthenticationActivity : AppCompatActivity() {

    lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Your condition to decide whether to start AnotherActivity
        appViewModel = AppViewModel.getViewModel(this)

        if (appViewModel.isLoggedIn()) {
            // Start AnotherActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

            // Finish the current activity if you don't want to keep it in the back stack
            finish()
        } else {
            setContentView(R.layout.activity_authentication)


        }
    }
}