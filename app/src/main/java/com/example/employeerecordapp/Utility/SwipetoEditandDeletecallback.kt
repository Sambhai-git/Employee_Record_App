package com.example.employeerecordapp.Utility

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.employeerecordapp.R

abstract class SwipeToEditAndDeleteCallback<T>(val context: Context, val adapter: T) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        // Get the item view
        val itemView = viewHolder.itemView

        // Create a Paint object for drawing
        val paint = Paint()

        // Check if swiping to the right

        when {
            dX > 0 -> {
                paint.color = Color.parseColor("#FF0000") // Red color

                // Draw the background
                c.drawRect(
                    itemView.left.toFloat(),
                    itemView.top.toFloat(),
                    dX,
                    itemView.bottom.toFloat(),
                    paint
                )

                // Draw the delete icon on the right side
                val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)

                // Calculate icon position
                val iconMargin = (itemView.height - deleteIcon?.intrinsicHeight!!) / 2
                val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
                val iconBottom = iconTop + deleteIcon.intrinsicHeight
                val iconLeft = itemView.left + iconMargin
                val iconRight = itemView.left + iconMargin + deleteIcon.intrinsicWidth

                // Draw the delete icon
                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                deleteIcon.draw(c)
            }

            dX < 0 -> {
                val background = ColorDrawable(Color.parseColor("#388E3C"))
                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                background.draw(c)

                // Draw the edit icon on the green background
                val editIcon = ContextCompat.getDrawable(context, R.drawable.ic_edit)
                val iconMargin = (itemView.height - editIcon!!.intrinsicHeight) / 2
                val iconTop = itemView.top + (itemView.height - editIcon.intrinsicHeight) / 2
                val iconBottom = iconTop + editIcon.intrinsicHeight

                editIcon.setBounds(
                    itemView.right - iconMargin - editIcon.intrinsicWidth,
                    iconTop,
                    itemView.right - iconMargin,
                    iconBottom
                )
                editIcon.draw(c)
            }
        }


        // Call the superclass method for default behavior
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // this method is called
        // when the item is moved.
        return false
    }

}

