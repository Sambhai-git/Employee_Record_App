package com.example.employeerecordapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.employeerecordapp.model.EmployeeR
import com.example.employeerecordapp.model.User
import com.example.employeerecordapp.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    val isAdded = MutableLiveData<Boolean>()

    private val _isAuthenticated = MutableLiveData<Result<User>>()
    val isAuthenticated: LiveData<Result<User>>
        get() = _isAuthenticated

    private val _isTokenIdExists = MutableLiveData<Boolean>()
    val isTokenIdExists: LiveData<Boolean>
        get() = _isTokenIdExists

    val getUserList: LiveData<List<User>> = repository.userList.asLiveData()
    private val _getEmployeeList = MutableLiveData<List<EmployeeR>?>()
    val getEmployeeList: MutableLiveData<List<EmployeeR>?>
        get() = _getEmployeeList

    companion object {
        fun getViewModel(context: Context): MainViewModel {
            return MainViewModel(MainRepository(context))
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "addUser:Coroutine ")
            repository.registerUser(user = user)
            withContext(Dispatchers.Main) {
                isAdded.value = true
            }
        }
    }

    fun authenticateUser(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("LogIn", "Credentials checking")
            val user = repository.searchUser(username, password)
            withContext(Dispatchers.Main) {
                if (user != null) {
                    _isAuthenticated.value = Result.success(user)
                } else {
                    _isAuthenticated.value = Result.failure(Exception("No user found"))
                }
            }
        }
    }

    private val TAG = "MainViewModel"

    suspend fun isUserNameUnique(userName: String): Boolean {
        return repository.isUserNameUnique(userName)
    }

    suspend fun isEmailUnique(email: String): Boolean {
        return repository.isEmailUnique(email)
    }

    suspend fun isPhoneNoExists(toLong: Long): Boolean {
        return repository.isPhoneNoExists(toLong)
    }

    suspend fun isEmailExists(email: String): Boolean {
        return repository.isEmailExists(email)
    }

    fun isTokenIdExists(tokenId: Long): Boolean {
        return runBlocking {
            repository.isTokenIdExists(tokenId)
        }
    }

    fun insertEmployee(employee: EmployeeR) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertEmployee(employee)
        }
    }

    fun searchEmployees(searchQuery: String, tokenID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val filteredEmployees = if (searchQuery.isNotEmpty()) {
                repository.searchEmployees(searchQuery, tokenID)
            } else {
                repository.getAllEmployees()
            }

            // Collect the values from the flow
            val employeeList = filteredEmployees.firstOrNull()

            withContext(Dispatchers.Main) {
                _getEmployeeList.value = employeeList
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(user)
        }
    }

    fun deleteEmployee(employee: EmployeeR) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEmployee(employee)
        }
    }

    fun getEmployeesSorted(sortBy: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val sortedEmployees = repository.getEmployeesSorted(sortBy)

            // Collect the values from the flow
            val employeeList = sortedEmployees.firstOrNull()

            withContext(Dispatchers.Main) {
                _getEmployeeList.value = employeeList
            }
        }
    }

    fun updateUser(updatedUser: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                async { repository.updateUser(updatedUser) }.await()
                onSuccess.invoke()
            } catch (e: Exception) {
                // Handle the error and provide an error message to the caller
                withContext(Dispatchers.Main) {
                    onError.invoke("Failed to update user: ${e.message}")
                }
            }
        }
    }

    fun updateEmployee(
        updatedEmployee: EmployeeR,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                async { repository.updateEmployee(updatedEmployee) }.await()
                onSuccess.invoke()
            } catch (e: Exception) {
                // Handle the error and provide an error message to the caller
                withContext(Dispatchers.Main) {
                    onError.invoke("Failed to update user: ${e.message}")
                }
            }
        }
    }

    private val _employeeAddedSuccess = MutableLiveData<Boolean>()
    val employeeAddedSuccess: LiveData<Boolean>
        get() = _employeeAddedSuccess

    // Function to set success state
    fun setEmployeeAddedSuccess(success: Boolean) {
        _employeeAddedSuccess.value = success
    }
}
