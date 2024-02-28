package com.example.employeerecordapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.employeerecordapp.Utility.Constants
import com.example.employeerecordapp.Utility.PrefHelper

class AppViewModel(val prefHelper: PrefHelper) : ViewModel() {

    private val _title = MutableLiveData("Welcome Admin")
    val title: LiveData<String>
        get() = _title

    companion object {
        private var viewModelInstance: AppViewModel? = null

        fun getViewModel(context: Context): AppViewModel {

            return if (viewModelInstance == null) {

                val prefHelper = PrefHelper(context)
                val viewModel = AppViewModel(prefHelper)
                viewModelInstance = viewModel
                viewModel
            } else {
                viewModelInstance!!
            }

        }

    }

    fun userAdmin(isAdmin: Boolean) {
        prefHelper.put(Constants.ADMIN, isAdmin)
        prefHelper.put(Constants.IS_LOGGED_IN, true)
    }

    fun isLoggedIn(): Boolean = prefHelper.getBoolean(Constants.IS_LOGGED_IN)


    fun isLoggedOut() {
        prefHelper.clear()
    }

    fun isAdmin(): Boolean {
        return prefHelper.getBoolean(Constants.ADMIN)
    }

    fun changeAppBarTitle(title: String) {
        _title.value = title
    }

    fun saveUserLoginState(username: String) {
        prefHelper.put(Constants.IS_LOGGED_IN, true)
        prefHelper.put(Constants.USER_NAME, username)
    }

    fun clearUserLoginState() {
        prefHelper.clear()
    }

    fun getSavedUsername(): String? {
        return prefHelper.getString(Constants.USER_NAME)
    }


}