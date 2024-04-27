package com.xdeveloperss.fourbrothers.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.blankj.utilcode.util.ToastUtils
import com.xdeveloperss.fourbrothers.databinding.ItemPagerBinding
import com.xdeveloperss.fourbrothers.utils.FileManager
import com.xdeveloperss.fourbrothers.utils.glideLoad
import com.xdeveloperss.ximagepreview.ImagePreviewActivity
import com.xdeveloperss.ximagepreview.PreviewFile

class PagerAdapter(private val context: Context, var images:MutableList<String>) : PagerAdapter() {
    override fun getCount(): Int {
        return images.size
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
       val binding =  ItemPagerBinding.inflate(LayoutInflater.from(context), container,false)
        binding.root.glideLoad(FileManager.getFileForGlide(images[position]))
        binding.root.setOnClickListener {
            val previewFiles = ArrayList<PreviewFile>()
            for (i in images){
                previewFiles.add(PreviewFile(i, "$i Image Description"))
            }
            val intent = Intent(context, ImagePreviewActivity::class.java)
            intent.putExtra(ImagePreviewActivity.IMAGE_LIST, previewFiles)
            intent.putExtra(ImagePreviewActivity.CURRENT_ITEM, position)
            context.startActivity(intent)
        }
        container.addView(binding.root)
        return binding.root
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
    fun addItem(item: String){
        images.add(item)
        this.notifyDataSetChanged()
    }
}
