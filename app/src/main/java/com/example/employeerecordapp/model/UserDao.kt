package com.example.employeerecordapp.model


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user WHERE UserName = :username AND password = :password")
    fun searchUser(username: String, password: String): User?

    @Query("SELECT COUNT(*) FROM user WHERE username = :userName")
    suspend fun isUserNameUnique(userName: String): Int

    @Query("SELECT COUNT(*) FROM user WHERE email_id = :email")
    suspend fun isEmailUnique(email: String): Int

    @Query("SELECT COUNT(*) FROM employeeR WHERE `Email Id` = :email")
    suspend fun isEmailExists(email: String): Int

    @Query("SELECT COUNT(*) FROM employeeR WHERE `PhoneNo` = :phoneNo")
    suspend fun isPhoneNoExists(phoneNo: Long): Int

    @Query("SELECT COUNT(*) FROM employeeR WHERE TokenID = :tokenId")
    suspend fun isTokenIdExists(tokenId: Long): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEmployee(employee: EmployeeR)

    @Query("SELECT * FROM employeeR")
    fun getAllEmployees(): Flow<List<EmployeeR>>

    @Query("SELECT * FROM employeeR WHERE Name LIKE :searchQuery OR TokenID = :tokenID")
    fun searchEmployees(searchQuery: String, tokenID: Long): Flow<List<EmployeeR>>


    @Query("SELECT * FROM employeeR ORDER BY CASE WHEN :sortBy = 'Name' THEN Name END, CASE WHEN :sortBy = 'TokenID' THEN TokenID END")
    fun getEmployeesSorted(sortBy: String): Flow<List<EmployeeR>>

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Delete
    suspend fun deleteEmployee(employee: EmployeeR)

    @Update
    suspend fun updateEmployee(updatedEmployee: EmployeeR)
}

