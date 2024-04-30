package com.xdeveloperss.fourbrothers.utils

import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ImageUtils
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.xdeveloperss.fourbrothers.BuildConfig
import com.xdeveloperss.fourbrothers.R
import java.io.File
import java.io.FileNotFoundException
import java.net.URL
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


fun FragmentActivity.showDateDialogWithDate(withSelection: Long = MaterialDatePicker.todayInUtcMilliseconds(), completion:(Date:String, date: Date)->Unit){
    val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(withSelection)
            .build()
    datePicker.show(this.supportFragmentManager,"DataPicker")
    datePicker.addOnPositiveButtonClickListener {
        completion(Date(it).formattedDate(), Date(it))
    }
}

fun Date.formattedDate(format: String = "yyyy-MM-dd"):String{
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(this)
}
fun Date.format1(format: String = "MMM d, yyyy"):String{
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
fun TextInputLayout.text(double: Double?){
    val stringValue = double ?: ""
    this.editText?.setText(stringValue.toString())
}
fun TextInputLayout.double(): Double{
    val char = editText?.text ?: "0"
    return char.toString().toDouble()
}
fun TextInputLayout.Int(): Int?{
    return if (editText?.text.isNullOrEmpty()) null else editText?.text.toString().toInt()
}
fun TextInputLayout.string(): String{
    return if (editText?.text.isNullOrEmpty()) "" else editText?.text.toString()
}
fun String.dateMilliSec(format: String = "yyyy-MM-dd"):Long{
    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date: Date = dateFormat.parse(this) ?: return -1
        return date.time
    } catch (e: Exception) {
        e.printStackTrace()
        return -1
    }
}
fun ImageView.glideLoad(uri: Uri?) {
    Glide.with(this)
        .load(uri)
        .placeholder(R.drawable.side_nav_bar)
        .into(this)
}
fun Int.isZero():Boolean{
  return this == 0
}
fun Int?.value():Int{
    return this ?: 0
}
fun Double.exactly(): String {
    val formatter = NumberFormat.getInstance().apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 16 // maximum digits in Double after dot (maximum precision)
    }
    return formatter.format(this) ?: ""
}
fun String.removeSpecialChars(): Double {
    val cleanString = this.replace("[^\\d.]".toRegex(), "").replace(",", "")
    return cleanString.toDoubleOrNull() ?: 0.0
}

val Double.rounded : Double
    get() =  String.format("%.1f", this).toDouble()

fun Double.addPresent(present: Int = 4):Double{
    return this+this*present
}
fun View.hide(bool: Boolean){
    visibility = if (bool) View.GONE else View.VISIBLE
}

fun View.backWithDelay(delay: Long = 200, complete:()->Unit) {
    Looper.myLooper()?.let {
        Handler(it).postDelayed({
            complete()
        }, delay)
    }
}
