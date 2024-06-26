package com.xdeveloperss.fourbrothers.xbase

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.canhub.cropper.CropImageContract
import com.google.android.material.textfield.TextInputLayout
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.adapters.PagerAdapter
import com.xdeveloperss.fourbrothers.data.models.Media
import com.xdeveloperss.fourbrothers.utils.AppExecutors
import com.xdeveloperss.fourbrothers.utils.FileManager


/**
 *@Author: Anas Sharif
 *@Email: anassharif1992@gmail.com
 *@Date: 29/08/2022
 */
abstract class XBaseFragment<B : ViewBinding>(val bindingFactory: (LayoutInflater, ViewGroup?, Boolean) -> B) :
    Fragment()  {
    lateinit var binding: B
    lateinit var mInflater: LayoutInflater
    var adapter: PagerAdapter? = null

    fun barTitle(text: String, subTitle: String? = null){
        (activity as AppCompatActivity?)?.supportActionBar?.title = text
        (activity as AppCompatActivity?)?.supportActionBar?.subtitle = subTitle
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBackPress()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mInflater = inflater
        binding = bindingFactory(inflater, container, false)
        return binding.root
    }
    protected open fun registerBackPress() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(enableBackPress()) {
                override fun handleOnBackPressed() {
                    backPressed()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
    }

    open fun backPressed(){
        findNavController().popBackStack()
    }

    abstract fun onViewCreated()

    open fun enableBackPress(): Boolean { return false }

    open fun imagePick( bitmap: Bitmap, fileName: String ,uri: Uri?){}

    fun <T> LiveData<T>.observe(onChanged: (T) -> Unit) {
        observe(viewLifecycleOwner, onChanged)
    }

    val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val imageUri = result.uriContent
            AppExecutors().getInstance()?.diskIO()?.execute {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                val fileName = FileManager.createFileFromPath(bitmap=bitmap)
                runOnUiThread {
                    imagePick(bitmap,fileName, imageUri)
                }
            }
        }
    }
    fun loadAdapter(list: List<Media>, pager: ViewPager){
        adapter = PagerAdapter(requireContext(), list.toMutableList())
        pager.adapter = adapter
    }

    fun loadPersonProduct(strings: List<String>, inputLayout: TextInputLayout, clickListener:((String, Int)->Unit)){
        val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_list_item, strings)
        val specieMenu = inputLayout.editText as? AutoCompleteTextView
        specieMenu?.setAdapter(adapter)
        specieMenu?.setOnItemClickListener { _, _, i, _ ->
            clickListener(strings[i], i)
        }

    }
}