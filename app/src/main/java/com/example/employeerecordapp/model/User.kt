package com.example.employeerecordapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user", indices = [Index(value = ["email_id"], unique = true)])
@Parcelize
data class User(
    @PrimaryKey
    @ColumnInfo(name = "UserName") val userName: String,
    @ColumnInfo(name = "email_id") val emailId: String,
    @ColumnInfo(name = "Name") val name: String,
    @ColumnInfo(name = "password") val password: String
) : Parcelable


@Entity(tableName = "employeeR")
@Parcelize
data class EmployeeR(
    @PrimaryKey
    @ColumnInfo(name = "TokenID") val tokenId: Long,
    @ColumnInfo(name = "Name") val name: String,
    @ColumnInfo(name = "PhoneNo") val phoneNo: Long,
    @ColumnInfo(name = "Designation") val designation: String,
    @ColumnInfo(name = "BloodGroup") val bloodGroup: String,
    @ColumnInfo(name = "Email Id") val emailId: String,
    @ColumnInfo(name = "Added by ") val emp: String,
    @ColumnInfo(name = "DOB") val dob: String,
    @ColumnInfo(name = "City") val city: String,
    @ColumnInfo(name = "DOJ") val doj: String,
    @ColumnInfo(name = "Last Modified by") val lastmod: String
) : Parcelable