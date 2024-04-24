package com.xdeveloperss.fourbrothers.ui.main.ui.shop.editor

import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.android.material.textfield.TextInputLayout
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.data.models.PersonRate
import com.xdeveloperss.fourbrothers.data.responses.ShopItemData
import com.xdeveloperss.fourbrothers.databinding.FragmentShopEditorBinding
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.ShopViewModel
import com.xdeveloperss.fourbrothers.utils.AppExecutors
import com.xdeveloperss.fourbrothers.utils.FileManager
import com.xdeveloperss.fourbrothers.utils.double
import com.xdeveloperss.fourbrothers.utils.text
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ShopEditorFragment : XBaseFragment<FragmentShopEditorBinding>(FragmentShopEditorBinding::inflate) {

    private val shopViewModel: ShopViewModel by sharedViewModel()

    private lateinit var shopData: ShopItemData

    private var productRate: PersonRate? = null
    override fun onViewCreated() {
        shopViewModel.selectedData.observe {
            shopData = it[1]!!
            binding.textFieldSelectParty.editText?.setText(it[1]?.personName)
            binding.fieldAmount.editText?.setText(it[1]?.total.toString())
            binding.fieldWeight.editText?.setText(it[1]?.weight.toString())
            binding.textFieldSelectPartyProduct.editText?.setText(it[1]?.product?.name)
            binding.textFieldRateType.editText?.setText(it[1]?.itemRate?.type?.name)
            binding.textFieldRates.editText?.setText(it[1]?.itemRate?.amount.toString())
            this.productRate = it[1]?.itemRate
            it[1]?.person?.products?.let { prods ->
                this.loadPersonProduct(prods.map { it.product.name }.toList(), binding.textFieldSelectPartyProduct)
                { s, i ->
                    binding.textFieldRateType.text(prods[i].rate.type?.name)
                    binding.textFieldRates.text(prods[i].rate.amount.toString())
                    this.productRate = prods[i].rate
                    this.updatedDataItemWeight(prods[i].rate)
                }
            }
        }

        binding.fieldWeight.editText?.addTextChangedListener { text ->
            productRate?.let {
                this.updatedDataItemWeight(it, text.toString().toDouble())
            }
        }

        binding.imageCard.setOnClickListener {
            cropImage.launch(
                options{
                    setGuidelines(CropImageView.Guidelines.ON)
                    setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                }
            )
        }
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val imageUri = result.uriContent
            Glide.with(requireActivity()).load(imageUri).into(binding.itemImage)
            AppExecutors().getInstance()?.diskIO()?.execute {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                binding.itemImage.tag = FileManager.createFileFromPath(bitmap=bitmap)
            }
        }
    }

    private fun loadPersonProduct(strings: List<String>, inputLayout: TextInputLayout, clickListener:((String,Int)->Unit)){
        val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_list_item, strings)
        val specieMenu = inputLayout.editText as? AutoCompleteTextView
        specieMenu?.setAdapter(adapter)
        specieMenu?.setOnItemClickListener { _, _, i, _ ->
            clickListener(strings[i], i)
        }

    }

    private fun updatedDataItemWeight(rate:PersonRate, weight: Double= binding.fieldWeight.double())
    {
        var total = 0.0
        when(rate.type?.id?.toInt()){
            1->{
                total = weight*(shopData.dailyRate.chickenrate + rate.amount)
            }
            2->{
                total = weight*(shopData.dailyRate.zindarate * rate.amount)
            }
            3->{
                total = weight*rate.amount
            }
            5->{
                total = weight*(shopData.dailyRate.zindarate + rate.amount)
            }
        }
        binding.fieldAmount.text(total.toString())
    }
}