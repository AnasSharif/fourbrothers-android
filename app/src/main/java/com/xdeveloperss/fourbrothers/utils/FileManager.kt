package com.xdeveloperss.fourbrothers.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.TimeUtils
import com.xdeveloperss.fourbrothers.XBaseApplication
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.util.Date


object FileManager {

    fun getDocuments(): String{
        return XBaseApplication.xCon().getDir(Environment.DIRECTORY_DOCUMENTS, Context.MODE_PRIVATE).absolutePath
    }
    fun getFile(fileName: String): Uri{
        return Uri.parse("${getDocuments()}/$fileName")
    }
    fun getFileWithName(fileName: String): File{
        return File(Uri.parse("${getDocuments()}/$fileName").path ?: "")
    }
    fun getFileForGlide(fileName: String?): Uri?{
        return Uri.parse("file://${getDocuments()}/$fileName")
    }
    fun createFileFromPath(path: String): String{
        val fileName = TimeUtils.date2Millis(Date()).toString()+".jpg"
        val downloadedFile = File("${getDocuments()}/$fileName")
        FileUtils.createOrExistsFile(downloadedFile)
        FileUtils.copy(File(path), downloadedFile)
        return fileName
    }
    fun createFileFromPath(withName: String? = null, bitmap: Bitmap): String{
        var fileName = TimeUtils.date2Millis(Date()).toString()+".jpg"
        if(withName != null){
            fileName = withName
        }
        val downloadedFile = File("${getDocuments()}/$fileName")
        FileUtils.createOrExistsFile(downloadedFile)
        ImageUtils.save(bitmap, downloadedFile, Bitmap.CompressFormat.JPEG, 30)
        return fileName
    }
    fun createFileFromPath(uri:Uri,bitmap: Bitmap): File{
        val downloadedFile = File(uri.path ?: "${getDocuments()}/${TimeUtils.date2Millis(Date()).toString()+".jpg"}")
        FileUtils.createOrExistsFile(downloadedFile)
        ImageUtils.save(bitmap, downloadedFile, Bitmap.CompressFormat.JPEG, 30)
        return downloadedFile
    }
    fun createFile(): Pair<Uri,String>{
        val fileName = TimeUtils.date2Millis(Date()).toString()+".jpg"
        val downloadedFile = File("${getDocuments()}/$fileName")
        FileUtils.createOrExistsFile(downloadedFile)
        return Pair(downloadedFile.toUri(), fileName)
    }
    fun getImageString(fileName: String): String {
        getFile(fileName).path?.let {
            val file = File(it)
            val inputStream = FileInputStream(file)
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } != -1) {
                outputStream.write(buffer, 0, length)
            }
            inputStream.close()

            return EncodeUtils.base64Encode2String(outputStream.toByteArray())
        }
        return ""
    }
}