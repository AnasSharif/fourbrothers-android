package com.xdeveloperss.fourbrothers.ui.main.ui.shop.editor

import android.graphics.Bitmap
import android.net.Uri
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
import com.xdeveloperss.fourbrothers.data.responses.DailyRates
import com.xdeveloperss.fourbrothers.data.responses.OrderItems
import com.xdeveloperss.fourbrothers.databinding.FragmentShopEditorBinding
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.ShopViewModel
import com.xdeveloperss.fourbrothers.utils.AppExecutors
import com.xdeveloperss.fourbrothers.utils.FileManager
import com.xdeveloperss.fourbrothers.utils.double
import com.xdeveloperss.fourbrothers.utils.rounded
import com.xdeveloperss.fourbrothers.utils.text
import com.xdeveloperss.fourbrothers.utils.value
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ShopEditorFragment : XBaseFragment<FragmentShopEditorBinding>(FragmentShopEditorBinding::inflate) {

    private val shopViewModel: ShopViewModel by sharedViewModel()

    private lateinit var itemData: OrderItems

    private var productRate: PersonRate? = null
    override fun onViewCreated() {
        shopViewModel.selectedData.observe {
            itemData = it
            binding.textFieldSelectParty.editText?.setText(itemData.personName)
            binding.fieldAmount.editText?.setText(itemData.total.toString())
            binding.fieldWeight.editText?.setText(itemData.weight.toString())
            binding.textFieldSelectPartyProduct.editText?.setText(itemData.product.name)
            binding.textFieldRateType.editText?.setText(itemData.itemRate.type?.name)
            binding.textFieldRates.editText?.setText(itemData.itemRate.amount.toString())
            this.productRate = itemData.itemRate
            itemData.person.products?.let { prods ->
                this.loadPersonProduct(prods.map { it.product.name }.toList(), binding.textFieldSelectPartyProduct)
                { s, i ->
                    binding.textFieldRateType.text(prods[i].rate.type?.name)
                    binding.textFieldRates.text(prods[i].rate.amount.toString())
                    this.productRate = prods[i].rate
                    itemData.productsId = prods[i].product.id
                    this.updatedDataItemWeight(prods[i].rate)
                }
            }
            this.loadAdapter(itemData.media?.map { it.file_name.toString() }?.toList() ?: listOf(), binding.imagePager)
        }

        binding.fieldWeight.editText?.addTextChangedListener { text ->
            productRate?.let {
                this.updatedDataItemWeight(it, text.toString().toDouble())
            }
        }

        binding.imagePicker.setOnClickListener {
            cropImage.launch(
                options{
                    setGuidelines(CropImageView.Guidelines.ON)
                    setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                }
            )
        }
        binding.saveBtn.setOnClickListener {
            shopViewModel.saveData(null, typeString = itemData.modelType, itemData)
        }
    }
    override fun imagePick(bitmap: Bitmap, fileName: String, uri: Uri?) {
        super.imagePick(bitmap, fileName ,uri)
        itemData.let {
            shopViewModel.storeFile(
                it.modelType,
                it.id.toString(),
                fileName,
                FileManager.getFileWithName(fileName = fileName)
            )
            adapter?.addItem(fileName)
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
                total = weight*(itemData.dailyRate.chickenrate.value() + rate.amount)
            }
            2->{
                total = weight*(itemData.dailyRate.zindarate.value()  * rate.amount)
            }
            3->{
                total = weight*rate.amount
            }
            5->{
                total = weight*(itemData.dailyRate.zindarate.value()  + rate.amount)
            }
        }
        binding.fieldAmount.text(total.rounded.toString())
        itemData.weight = weight
        itemData.total = total.rounded
        itemData.personRatesId = rate.id
    }
}