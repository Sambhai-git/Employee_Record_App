package com.example.employeerecordapp.fragments.read

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.employeerecordapp.R
import com.example.employeerecordapp.model.EmployeeR
import java.util.Locale
import java.util.Random

class EmployeeCardAdapter(
    private val clickListener: (employeeDetail: EmployeeR) -> Unit,
    private val highlightedWords: List<String> = emptyList()
) : RecyclerView.Adapter<EmployeeCardAdapter.EmployeeCardViewHolder>() {


    val list = mutableListOf<EmployeeR>()

    fun updateList(newList: List<EmployeeR>) {
        if (list.isNotEmpty()) list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }


    class EmployeeCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.nameCard)
        val tokenId: TextView = view.findViewById(R.id.tokenCard)
        val designation: TextView = view.findViewById(R.id.designationCard)
        val phoneNumber: TextView = view.findViewById(R.id.phoneNumberCard)
        val imageView: ImageView = view.findViewById(R.id.imageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeCardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_employee, parent, false)
        return EmployeeCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: EmployeeCardViewHolder, position: Int) {
        val employeeDetail = list[position]
        holder.name.text = highlightText(employeeDetail.name)
        holder.name.text = employeeDetail.name
        holder.designation.text = employeeDetail.designation
        holder.tokenId.text = employeeDetail.tokenId.toString()
        holder.phoneNumber.text = employeeDetail.phoneNo.toString()

        // Create a drawable with the initial letter and random background color
        val initialLetter = employeeDetail.name.substring(0, 1).uppercase(Locale.ROOT)
        val backgroundColor = getRandomColor()
        val drawable = getInitialLetterDrawable(holder, initialLetter, backgroundColor)

        // Set the drawable to the ImageView
        holder.imageView.setImageDrawable(drawable)

        holder.name.apply {
            setOnClickListener {
                clickListener.invoke(employeeDetail)
            }
        }
    }

    private fun highlightText(originalText: String): CharSequence {
        val spannableString = SpannableString(originalText)
        highlightedWords.forEach { word ->
            val startIndex = originalText.indexOf(word, ignoreCase = true)
            if (startIndex != -1) {
                val endIndex = startIndex + word.length
                spannableString.setSpan(
                    ForegroundColorSpan(Color.RED),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return spannableString
    }


    private fun getInitialLetterDrawable(
        holder: EmployeeCardViewHolder,
        initial: String,
        color: Int
    ): Drawable {
        val size = 100 // Set the size of the image as needed

        // Create a Bitmap with a transparent background
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Draw a rounded rectangle with the random background color
        val paint = Paint().apply {
            setColor(color)
            isAntiAlias = true
        }
        canvas.drawRoundRect(0f, 0f, size.toFloat(), size.toFloat(), 50f, 50f, paint)

        // Draw the initial letter in white
        paint.color = Color.WHITE
        paint.textSize = 40f // Set the text size as needed
        paint.textAlign = Paint.Align.CENTER

        // Adjust the vertical position based on the font metrics
        val fontMetrics = paint.fontMetrics
        val baseline = (size - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top
        canvas.drawText(initial, size / 2.toFloat(), baseline, paint)

        // Convert the Bitmap to a Drawable
        return BitmapDrawable(holder.itemView.context.resources, bitmap)
    }

    private fun getRandomColor(): Int {
        val random = Random()
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }

    fun getItemAtPosition(position: Int): EmployeeR {
        return list[position]
    }

}