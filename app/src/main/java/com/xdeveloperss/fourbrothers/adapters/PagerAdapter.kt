package com.xdeveloperss.fourbrothers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.xdeveloperss.fourbrothers.databinding.ItemPagerBinding
import com.xdeveloperss.fourbrothers.utils.FileManager
import com.xdeveloperss.fourbrothers.utils.glideLoad

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
