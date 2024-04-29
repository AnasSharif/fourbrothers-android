package com.xdeveloperss.fourbrothers.data.models

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import com.blankj.utilcode.util.ImageUtils
import com.xdeveloperss.fourbrothers.BuildConfig
import com.xdeveloperss.fourbrothers.utils.AppExecutors
import com.xdeveloperss.fourbrothers.utils.FileManager
import java.io.File
import java.io.FileNotFoundException
import java.net.URL
import java.util.UUID

class Media {
    var id: String = UUID.randomUUID().toString()
    var name: String? = null
    var file_name: String? = null
    var original_url: String? = null

    fun download() {
        AppExecutors().getInstance()?.diskIO()?.execute {
            try {
                original_url?.let {
                    var url = URL(it)
                    if (BuildConfig.DEBUG) {
                        val result = it.substringAfter("storage")
                        url = URL("${BuildConfig.API_URL}/storage$result")
                    }
                    val downloadedFile = File(FileManager.getDocuments(), file_name.toString())
                    val imageData = url.readBytes()
                    val bitmap = ImageUtils.bytes2Bitmap(imageData)
                    ImageUtils.compressByScale(bitmap, bitmap.width/2, bitmap.height/2)
                    ImageUtils.save(bitmap, downloadedFile, Bitmap.CompressFormat.JPEG)
                }
            } catch (e: FileNotFoundException) {
                println("File not found: ${e.message}")
            } catch (e: Exception) {
                println("An error occurred: ${e.message}")
            }
        }
    }
}
