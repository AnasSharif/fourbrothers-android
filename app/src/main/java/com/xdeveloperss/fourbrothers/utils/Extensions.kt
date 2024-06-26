package com.xdeveloperss.fourbrothers.utils

import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.Editable
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
import java.text.Normalizer
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


fun FragmentActivity.showDateDialogWithDate(withSelection: Long = MaterialDatePicker.todayInUtcMilliseconds(),
                                            completion:(stringDate:String, date: Date)->Unit){
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
fun Date.format2(format: String = "yyyy-MM-dd HH:mm:ss"):String{
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(this)
}
fun Date.format1(format: String = "MMM d, yyyy"):String{
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(this)
}
fun Date.formattedServer(format: String = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"):String{
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(this)
}
fun Date.getCurrentMonthAndYear(): Pair<String, Int> {
    val calendar = Calendar.getInstance()
    val monthDateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
    val yearDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    val monthName = monthDateFormat.format(calendar.time)
    val year = yearDateFormat.format(calendar.time).toInt()
    return Pair(monthName, year)
}
fun TextInputLayout.text(text: String?){
    this.editText?.setText(text)
}
fun TextInputLayout.textOrEmpty(text: String?){
    this.editText?.setText(text ?: "")
}
fun TextInputLayout.text(intValue: Int?){
    val stringValue = intValue ?: ""
    this.editText?.setText(stringValue.toString())
}
fun TextInputLayout.text(double: Double?){
    val stringValue = double ?: ""
    this.editText?.setText(stringValue.toString())
}
fun TextInputLayout.text(double: Long?){
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
fun Double?.value():Double{
    return this ?: 0.0
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

val Editable?.double : Double
    get() = if (this.isNullOrEmpty()) 0.0 else this.toString().toDouble()

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

fun String.slug(replacement: String = "-") = Normalizer
    .normalize(this, Normalizer.Form.NFD)
    .replace("[^\\p{ASCII}]".toRegex(), "")
    .replace("[^a-zA-Z0-9\\s]+".toRegex(), "").trim()
    .replace("\\s+".toRegex(), replacement)
    .lowercase()

fun String.toDate(format: String = "yyyy-MM-dd"):Date{
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.parse(this) ?: Date()
}
