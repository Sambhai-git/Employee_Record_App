package com.example.employeerecordapp.fragments.read

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.employeerecordapp.R
import com.example.employeerecordapp.model.User


// UserAdapter.kt
class UserAdapter(private val context: Context, private val listener: OnUserClickListener) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    var userList = mutableListOf<User>()

    fun updateList(currentList: List<User>) {
        if (userList.isNotEmpty()) {
            userList.clear()
        }
        userList.addAll(currentList)
        notifyDataSetChanged()
    }


    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views in the user item layout
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val serialNumberTextView: TextView = itemView.findViewById(R.id.serialNumberTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        // Inflate the user item layout
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    interface OnUserClickListener {
        fun onEditUser(user: User)
        fun onDeleteUser(user: User)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        // Bind data to views in each user item
        val user = userList[position]
        holder.usernameTextView.text = user.userName
        holder.emailTextView.text = user.emailId
        holder.nameTextView.text = user.name
        holder.serialNumberTextView.text = context.getString(R.string.position, position + 1)

//        holder.itemView.setOnClickListener {
//            val user = userList[position]
//            listener.onEditUser(user)
//        }
//        holder.itemView.setOnLongClickListener {
//            val user = userList[position]
//            listener.onDeleteUser(user)
//            true
//        }
    }

    override fun getItemCount(): Int {
        return if (userList.isNotEmpty()) userList.size else 0
    }

    fun updateUserList(userList: List<User>?): List<User>? {
        return userList
    }

    fun getUserAtPosition(position: Int): User {
        return userList[position]
    }
}
