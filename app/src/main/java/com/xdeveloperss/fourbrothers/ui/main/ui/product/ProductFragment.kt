package com.xdeveloperss.fourbrothers.ui.main.ui.product

import android.graphics.Bitmap
import android.net.Uri
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.kongzue.dialogx.dialogs.WaitDialog
import com.xdeveloperss.fourbrothers.adapters.AdapterAction
import com.xdeveloperss.fourbrothers.adapters.AdapterType
import com.xdeveloperss.fourbrothers.adapters.GenericAdapter
import com.xdeveloperss.fourbrothers.custom.XDialogBuilder
import com.xdeveloperss.fourbrothers.custom.XDialogType
import com.xdeveloperss.fourbrothers.data.models.Expense
import com.xdeveloperss.fourbrothers.data.models.Media
import com.xdeveloperss.fourbrothers.data.models.Product
import com.xdeveloperss.fourbrothers.databinding.FragmentProductBinding
import com.xdeveloperss.fourbrothers.ui.main.ui.expense.ExpenseViewModel
import com.xdeveloperss.fourbrothers.utils.FileManager
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProductFragment: XBaseFragment<FragmentProductBinding>(FragmentProductBinding::inflate)  {

    private val productViewModel: ProductViewModel by sharedViewModel()

    private var selectedIndex: Int = 0

    private lateinit var product: Product
    override fun onViewCreated() {

        productViewModel.setData(null, listOf("products"))
        productViewModel.getData.observe { resp ->
            resp.getValueFromResponse()?.data?.let {
                binding.productsRV.adapter = GenericAdapter(type = AdapterType.PRODUCT, it.products)
                { i, action, pro ->
                    product = pro
                    when(action){
                        AdapterAction.EDIT -> {}
                        AdapterAction.DELETE -> {}
                        AdapterAction.SELECT -> {
//                            XDialogBuilder(requireActivity(), exp).setData(type = XDialogType.EXPENSE) {
//                                productViewModel.saveData(null,"expenses", it as Expense)
//                                binding.expenseRV.adapter?.notifyItemChanged(i)
//                            }.show()
                        }
                        AdapterAction.PICKER -> {
                            selectedIndex = i
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
        productViewModel.storeFile(
            "products",
            product.id.toString(),
            fileName,
            FileManager.getFileWithName(fileName = fileName))
        Media().also {
            it.file_name = fileName
            product.media.add(it)
        }
        binding.productsRV.adapter?.notifyItemChanged(selectedIndex)
    }
}