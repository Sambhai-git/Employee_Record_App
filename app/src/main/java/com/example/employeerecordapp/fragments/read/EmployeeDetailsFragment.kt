package com.example.employeerecordapp.fragments.read

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.employeerecordapp.R
import com.example.employeerecordapp.databinding.FragmentEmployeeDetailsBinding
import com.example.employeerecordapp.fragments.homepage.EmployeeMenu
import com.example.employeerecordapp.model.EmployeeR
import com.example.employeerecordapp.viewmodel.AppViewModel
import com.example.employeerecordapp.viewmodel.MainViewModel
import com.example.employeerecordapp.viewmodel.MainViewModelFactory
import java.util.Random


class EmployeeDetailsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var appViewModel: AppViewModel
    private lateinit var binding: FragmentEmployeeDetailsBinding
    private lateinit var employeeDetail: EmployeeR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getParcelable<EmployeeR>("employeeDetail")?.let {
            employeeDetail = it
        }
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(requireContext())
        )[MainViewModel::class.java]

        appViewModel = AppViewModel.getViewModel(requireContext())

        appViewModel.changeAppBarTitle("Details of ${employeeDetail.name}")

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEmployeeDetailsBinding.inflate(inflater, container, false)

        val firstLetter = employeeDetail.name.firstOrNull()?.toUpperCase()
        val drawable = createTextDrawable(firstLetter)

        // Find the ImageView in the layout and set the drawable
        binding.employeeimage.setImageDrawable(drawable)
        binding.textName.text = "Name: ${employeeDetail.name}"
        binding.textDesignation.text = "Designation: ${employeeDetail.designation}"
        binding.textPhone.text = "Phone No : ${employeeDetail.phoneNo}"
        binding.textEmail.text = "Email ID :${employeeDetail.emailId}"
        binding.textTokenID.text = "Token ID : ${employeeDetail.tokenId}"
        binding.textBloodGroup.text = "Blood Group: ${employeeDetail.bloodGroup}"
        binding.textDOB.text = "Date of Birth: ${employeeDetail.dob}"
        binding.textCity.text = "City: ${employeeDetail.city}"
        binding.joiningDate.text = "Date of Joining : ${employeeDetail.doj}"
        binding.addedBy.text = "Record Added by: ${employeeDetail.emp}"
        binding.modifiedBy.text = "Record Modified by: ${employeeDetail.lastmod}"




        binding.closeBtn.apply {
            setOnClickListener {
                val fragmentManager = parentFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.main_nav_host, EmployeeMenu())
                    .addToBackStack(null)
                    .commit()
            }
        }

        return binding.root
    }

    private fun createTextDrawable(text: Char?): Drawable {
        val randomColor = generateRandomColor()

        // Create a square bitmap to cover the whole CardView
        val imageSize = 1200 // Adjust the size as needed
        val textBitmap = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(textBitmap)
        val paint = Paint().apply {
            color = randomColor
            textAlign = Paint.Align.CENTER
            textSize = 400f // Adjust the text size as needed
        }

        // Draw background color
        canvas.drawColor(randomColor)

        // Draw text in white
        paint.color = Color.WHITE
        canvas.drawText(text.toString(), imageSize / 2f, imageSize / 2f + paint.textSize / 2, paint)

        return BitmapDrawable(resources, textBitmap)
    }


    private fun generateRandomColor(): Int {
        // Generate a random color for the background
        val random = Random()
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }

}