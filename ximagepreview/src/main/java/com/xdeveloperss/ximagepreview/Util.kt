package com.xdeveloperss.ximagepreview

import android.content.ContentValues
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

object Util {


    private fun getDocuments(context: Context): String{
        return context.getDir(Environment.DIRECTORY_DOCUMENTS, Context.MODE_PRIVATE).absolutePath
    }
    fun getFileForGlide(fileName: String?, context: Context): Uri?{
        return Uri.parse("file://${getDocuments(context)}/$fileName")
    }
    fun saveImageToGallery(context: Context, bitmap: Bitmap) {
        val photo = File(
            getAppFolder(context),
            SystemClock.currentThreadTimeMillis().toString() + ".jpg"
        )
        try {
            val fos = FileOutputStream(photo.path)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            if (photo.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                values.put(MediaStore.MediaColumns.DATA, photo.absolutePath)
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                Toast.makeText(context, "Image saved to Gallery", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("Picture", "Exception in photoCallback", e)
        }
    }

    fun shareImage(context: Context, bitmap: Bitmap): Uri? {
        val photo = File(
            getAppFolder(context),
            SystemClock.currentThreadTimeMillis().toString() + ".jpg"
        )
        try {
            val fos = FileOutputStream(photo.path)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            if (photo.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                values.put(MediaStore.MediaColumns.DATA, photo.absolutePath)
                return context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )
            }
        } catch (e: Exception) {
            Log.e("Picture", "Exception in photoCallback", e)
        }
        return null
    }

    private fun getAppName(context: Context): String {
        val pm = context.applicationContext.packageManager
        val ai: ApplicationInfo? = try {
            pm.getApplicationInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
        return (if (ai != null) pm.getApplicationLabel(ai) else "(unknown)") as String
    }

    private fun workingDirectory(context: Context): File{
        var directory =
            File(Environment.getExternalStorageDirectory(), "com.xdeveloperss.photoprisma")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            directory = File(context.cacheDir, "com.xdeveloperss.photoprisma")
        }
        if (!directory.exists()) {
            directory.mkdir()
        }
        return directory
    }

    fun getAppFolder(context: Context): File {
        val photoDirectory = File(
            workingDirectory(context).absolutePath,
            getAppName(context.applicationContext)
        )
        if (!photoDirectory.exists()) {
            photoDirectory.mkdir()
        }
        return photoDirectory
    }

    fun getCreationFolder(context: Context): String {
        return "${context.getDir(Environment.DIRECTORY_DOCUMENTS, Context.MODE_PRIVATE).absolutePath}/PhotoPrisma/"
    }
}