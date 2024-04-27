package com.xdeveloperss.ximagepreview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView

class SlideAdapter(
    private val context: Context,
    private val list: List<PreviewFile>,
    private val listener: OnItemClickListener<PreviewFile>
) : PagerAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, anyObject: Any): Boolean {
        return view === anyObject
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater: LayoutInflater =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)!!
        val view: View = layoutInflater.inflate(R.layout.item_preview, container, false)
        val image: PhotoView = view.findViewById(R.id.iv_preview)
        val tvImageDescription = view.findViewById<TextView>(R.id.tvImageDescription)

        Glide.with(context)
            .load(Util.getFileForGlide(list[position].imageURL, context))
            .into(image)
        if (list[position].imageDescription.isEmpty()) {
            tvImageDescription.visibility = View.GONE
        } else {
            tvImageDescription.visibility = View.VISIBLE
            tvImageDescription.text = list[position].imageDescription
        }
        image.setOnClickListener {
            listener.onItemClick(list[position])
            if (!list[position].imageDescription.isEmpty()) {
                tvImageDescription.visibility =
                    if (tvImageDescription.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
            }
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, anyObject: Any) {
        container.removeView(anyObject as RelativeLayout?)
    }
}