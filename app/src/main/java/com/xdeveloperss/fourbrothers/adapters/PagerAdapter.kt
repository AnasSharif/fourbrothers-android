package com.xdeveloperss.fourbrothers.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ToastUtils
import com.xdeveloperss.fourbrothers.BuildConfig
import com.xdeveloperss.fourbrothers.data.models.Media
import com.xdeveloperss.fourbrothers.databinding.ItemPagerBinding
import com.xdeveloperss.fourbrothers.utils.AppExecutors
import com.xdeveloperss.fourbrothers.utils.FileManager
import com.xdeveloperss.fourbrothers.utils.glideLoad
import com.xdeveloperss.ximagepreview.ImagePreviewActivity
import com.xdeveloperss.ximagepreview.PreviewFile
import java.io.File
import java.io.FileNotFoundException
import java.net.URL

class PagerAdapter(private val context: Context, var images:MutableList<Media>) : PagerAdapter() {
    override fun getCount(): Int {
        return images.size
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
       val binding =  ItemPagerBinding.inflate(LayoutInflater.from(context), container,false)
        val fileName = images[position].file_name
        if (FileUtils.isFileExists(FileManager.getFileForGlide(fileName)?.path)){
            binding.root.glideLoad(FileManager.getFileForGlide(fileName))
        }else{
            images[position].download(complete = {
                binding.root.glideLoad(FileManager.getFileForGlide(fileName))
            })
        }
        binding.root.setOnClickListener {
            val previewFiles = ArrayList<PreviewFile>()
            for (i in images){
                previewFiles.add(PreviewFile(i.file_name.toString(), "${i.name} Image Description", i.original_url))
            }
            val intent = Intent(context, ImagePreviewActivity::class.java)
            intent.putExtra(ImagePreviewActivity.IMAGE_LIST, previewFiles)
            intent.putExtra(ImagePreviewActivity.CURRENT_ITEM, position)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
        container.addView(binding.root)
        return binding.root
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
    fun addItem(item: String){
        val media = Media()
        media.file_name = item
        images.add(media)
        this.notifyDataSetChanged()
    }
}
