package com.xdeveloperss.fourbrothers.utils

import android.app.Activity
import android.net.Uri
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.xdeveloperss.fourbrothers.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun FragmentActivity.showDateDialogWithDate(withSelection: Long = MaterialDatePicker.todayInUtcMilliseconds(), completion:(Date:String, date: Date)->Unit){
    val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(withSelection)
            .build()
    datePicker.show(this.supportFragmentManager,"DataPicker")
    datePicker.addOnPositiveButtonClickListener {
        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(it))
        val newDate = dateFormat.format(Date(it))
        completion(newDate, Date(it))
    }
}

fun Date.formattedDate(format: String = "yyyy-MM-dd"):String{
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(this)
}
fun TextInputLayout.text(text: String?){
    this.editText?.setText(text)
}
fun TextInputLayout.text(intValue: Int?){
    val stringValue = intValue ?: ""
    this.editText?.setText(stringValue.toString())
}
fun TextInputLayout.double(): Double{
    val char = editText?.text ?: "0"
    return char.toString().toDouble()
}
fun ImageView.glideLoad(uri: Uri?) {
    Glide.with(this)
        .load(uri)
        .placeholder(R.drawable.side_nav_bar)
        .into(this)
}