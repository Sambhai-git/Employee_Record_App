package com.example.employeerecordapp

import android.app.Application
import com.example.employeerecordapp.viewmodel.AppViewModel

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppViewModel.getViewModel(this)
    }
}