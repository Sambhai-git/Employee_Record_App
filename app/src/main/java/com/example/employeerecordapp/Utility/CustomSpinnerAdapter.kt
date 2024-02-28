package com.example.employeerecordapp.Utility

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomSpinnerAdapter(mContext: Context, val list: List<String>) : ArrayAdapter<String>(
    mContext,
    android.R.layout.simple_spinner_dropdown_item,
    list
) {

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: TextView = super.getDropDownView(position, convertView, parent) as TextView

        if (position == 0) {
            view.setTextColor(Color.GRAY)
        } else {
            view.setTextColor(Color.BLACK)
        }

        return view
    }

}