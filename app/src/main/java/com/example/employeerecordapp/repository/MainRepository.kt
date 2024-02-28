package com.example.employeerecordapp.repository

import android.content.Context
import com.example.employeerecordapp.model.EmployeeR
import com.example.employeerecordapp.model.User
import com.example.employeerecordapp.model.UserDao
import com.example.employeerecordapp.model.UserDatabase
import kotlinx.coroutines.flow.Flow

class MainRepository(context: Context) {

    private var dao: UserDao

    init {
        val database = UserDatabase.getDatabase(context)
        dao = database.userDao()
    }

    suspend fun registerUser(user: User) {
        dao.addUser(user)
    }

    fun searchUser(username: String, password: String): User? {
        return dao.searchUser(username, password)
    }

    suspend fun isUserNameUnique(userName: String): Boolean {
        return dao.isUserNameUnique(userName) == 0
    }

    suspend fun isEmailUnique(email: String): Boolean {
        return dao.isEmailUnique(email) == 0
    }

    suspend fun isEmailExists(email: String): Boolean {
        return dao.isEmailExists(email) > 0
    }

    suspend fun isPhoneNoExists(phoneNo: Long): Boolean {
        return dao.isPhoneNoExists(phoneNo) > 0
    }

    suspend fun isTokenIdExists(tokenId: Long): Boolean {
        return dao.isTokenIdExists(tokenId) > 0
    }

    suspend fun insertEmployee(employee: EmployeeR) {
        dao.insertEmployee(employee)
    }

    fun searchEmployees(searchQuery: String, tokenID: Long): Flow<List<EmployeeR>> {
        return dao.searchEmployees("%$searchQuery%", tokenID)
    }

    fun getEmployeesSorted(sortBy: String): Flow<List<EmployeeR>> {
        return dao.getEmployeesSorted(sortBy)
    }

    fun getAllEmployees(): Flow<List<EmployeeR>> {
        return dao.getAllEmployees()
    }

    suspend fun updateUser(user: User) {
        dao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        dao.deleteUser(user)
    }

    suspend fun deleteEmployee(employee: EmployeeR) {
        dao.deleteEmployee(employee)
    }


    suspend fun updateEmployee(updatedEmployee: EmployeeR) {
        dao.updateEmployee(updatedEmployee)

    }

    val userList: Flow<List<User>> = dao.getAllUsers()
}
