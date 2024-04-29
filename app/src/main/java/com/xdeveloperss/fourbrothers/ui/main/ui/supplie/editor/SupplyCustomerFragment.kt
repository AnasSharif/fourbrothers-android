package com.xdeveloperss.fourbrothers.ui.main.ui.supplie.editor

import android.graphics.Bitmap
import android.net.Uri
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.kongzue.dialogx.dialogs.WaitDialog
import com.xdeveloperss.fourbrothers.adapters.AdapterAction
import com.xdeveloperss.fourbrothers.adapters.AdapterType
import com.xdeveloperss.fourbrothers.adapters.GenericAdapter
import com.xdeveloperss.fourbrothers.custom.XDialogBuilder
import com.xdeveloperss.fourbrothers.custom.XDialogType
import com.xdeveloperss.fourbrothers.data.models.Media
import com.xdeveloperss.fourbrothers.data.models.Supply
import com.xdeveloperss.fourbrothers.data.models.VendorSupplieItems
import com.xdeveloperss.fourbrothers.databinding.FragmentSupplyCustomerBinding
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.buyers.BuyerFragmentDirections
import com.xdeveloperss.fourbrothers.ui.main.ui.supplie.SupplieFragmentDirections
import com.xdeveloperss.fourbrothers.ui.main.ui.supplie.SupplieViewModel
import com.xdeveloperss.fourbrothers.utils.FileManager
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SupplyCustomerFragment : XBaseFragment<FragmentSupplyCustomerBinding>(FragmentSupplyCustomerBinding::inflate) {

    private val supplyViewModel: SupplieViewModel by sharedViewModel()

    private lateinit var item: VendorSupplieItems
    override fun onViewCreated() {
        supplyViewModel.getVendorItems.observe {
            binding.supplyCustomersRV.adapter = GenericAdapter(type = AdapterType.SUPPLY_PARTY, it)
            { i, action, item ->
                this.item = item
                when(action){
                    AdapterAction.EDIT -> {}
                    AdapterAction.DELETE -> {}
                    AdapterAction.SELECT -> {
                        XDialogBuilder(requireActivity(), item).setData(type = XDialogType.VENDOR_ITEM) {
                            supplyViewModel.saveData(VendorSupplieItems::class.java,null, it as VendorSupplieItems)
                            binding.supplyCustomersRV.adapter?.notifyDataSetChanged()
                        }.show()
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

    override fun imagePick(bitmap: Bitmap, fileName: String, uri: Uri?) {
        super.imagePick(bitmap, fileName ,uri)
        WaitDialog.show("Uploading...")
        supplyViewModel.storeFile(
            "vendorSupplieItems",
            item.id.toString(),
            fileName,
            FileManager.getFileWithName(fileName = fileName))
        Media().also { m->
            m.file_name = fileName
            item.media?.add(m)
            binding.supplyCustomersRV.adapter?.notifyDataSetChanged()
        }
        this.onViewCreated()
    }
}