package com.xdeveloperss.fourbrothers.ui.main.ui.supplie

import android.graphics.Bitmap
import android.net.Uri
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.kongzue.dialogx.dialogs.WaitDialog
import com.xdeveloperss.fourbrothers.adapters.AdapterAction
import com.xdeveloperss.fourbrothers.adapters.AdapterType
import com.xdeveloperss.fourbrothers.adapters.GenericAdapter
import com.xdeveloperss.fourbrothers.data.models.Supply
import com.xdeveloperss.fourbrothers.data.responses.Data
import com.xdeveloperss.fourbrothers.databinding.FragmentSupplieBinding
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.ShopViewModel
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.customers.CustomerFragmentDirections
import com.xdeveloperss.fourbrothers.utils.FileManager
import com.xdeveloperss.fourbrothers.utils.dateMilliSec
import com.xdeveloperss.fourbrothers.utils.formattedDate
import com.xdeveloperss.fourbrothers.utils.showDateDialogWithDate
import com.xdeveloperss.fourbrothers.utils.string
import com.xdeveloperss.fourbrothers.utils.text
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.utlis.Prefs
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Date

class SupplieFragment : XBaseFragment<FragmentSupplieBinding>(FragmentSupplieBinding::inflate) {

    private val supplieViewModel: SupplieViewModel by sharedViewModel()

    private lateinit var supply: Supply
    override fun onViewCreated() {

        this.loadData(Prefs.getString("selectedSupplieDate") ?: Date().formattedDate())

        binding.textFieldSaleDate.editText?.setOnClickListener {
            requireActivity().showDateDialogWithDate( binding.textFieldSaleDate.string().dateMilliSec(),completion = { string, date ->
                this.loadData(string)
            })
        }
        supplieViewModel.getData.observe { resp ->
            resp.getValueFromResponse()?.data?.let {
                binding.suppliesRV.adapter = GenericAdapter(type = AdapterType.SUPPLY, it.supplies)
                { i, action, shop ->
                    supply = it.supplies[i]
                    when(action){
                        AdapterAction.EDIT -> {}
                        AdapterAction.DELETE -> {}
                        AdapterAction.SELECT -> {
                            findNavController().navigate(SupplieFragmentDirections.actionNavSuppliesToSupplyDetailFragment())
                            supplieViewModel.setSupply(supply)
                        }
                        AdapterAction.PICKER -> {
                            cropImage.launch(
                                options{
                                    setGuidelines(CropImageView.Guidelines.ON)
                                    setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    override fun imagePick(bitmap: Bitmap, fileName: String, uri: Uri?) {
        super.imagePick(bitmap, fileName ,uri)
        WaitDialog.show("Uploading...")
        supplieViewModel.storeFile(
                "vendorSupplie",
                supply.vendorSupplie.id.toString(),
                fileName,
                FileManager.getFileWithName(fileName = fileName))
        this.onViewCreated()
    }
    private fun loadData(date: String){
        WaitDialog.show("Load Data...")
        supplieViewModel.setData(date, listOf("supplies"))
        binding.textFieldSaleDate.editText?.setText(date)
        Prefs.putString("selectedDate", date)
    }
}