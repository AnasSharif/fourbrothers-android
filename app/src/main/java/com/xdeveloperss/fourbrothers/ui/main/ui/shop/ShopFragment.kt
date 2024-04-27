package com.xdeveloperss.fourbrothers.ui.main.ui.shop

import android.graphics.Bitmap
import android.net.Uri
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.kongzue.dialogx.dialogs.WaitDialog
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.adapters.PagerAdapter
import com.xdeveloperss.fourbrothers.data.BaseResponseRepo
import com.xdeveloperss.fourbrothers.data.responses.Data
import com.xdeveloperss.fourbrothers.databinding.FragmentShopBinding
import com.xdeveloperss.fourbrothers.ui.join.data.AuthViewModel
import com.xdeveloperss.fourbrothers.utils.FileManager
import com.xdeveloperss.fourbrothers.utils.formattedDate
import com.xdeveloperss.fourbrothers.utils.glideLoad
import com.xdeveloperss.fourbrothers.utils.showDateDialogWithDate
import com.xdeveloperss.fourbrothers.utils.text
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date

class ShopFragment : XBaseFragment<FragmentShopBinding>(FragmentShopBinding::inflate) {

    private val shopViewModel: ShopViewModel by sharedViewModel()

    private var adapter: PagerAdapter? = null

    private lateinit var shopData: Data
    override fun onViewCreated() {

        this.loadData(Date())
        binding.textFieldSaleDate.editText?.setOnClickListener {
            requireActivity().showDateDialogWithDate( completion = { _, date ->
                this.loadData(date)
            })
        }
        binding.customerDetail.setOnClickListener {
            findNavController().navigate(ShopFragmentDirections.actionNavShopToCustomerFragment())
            shopViewModel.setCustomsList(shopData.orderItems.sortedBy { it.personName })
        }
        binding.buyersDetail.setOnClickListener {
            findNavController().navigate(ShopFragmentDirections.actionNavShopToBuyerFragment())
            shopViewModel.setCustomsList(shopData.stockItems.sortedBy { it.personName })
        }
        binding.imagePicker.setOnClickListener {
            cropImage.launch(
                options{
                    setGuidelines(CropImageView.Guidelines.ON)
                    setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                }
            )
        }
        shopViewModel.getData.observe { resp ->
            resp.getValueFromResponse()?.data?.let {
                shopData = it
                if(shopData.dailyRates.isNotEmpty()){
                    val rate = shopData.dailyRates.first()
                    binding.chickenRateField.text(rate.chickenrate)
                    binding.zindaRateField.text(rate.zindarate)
                    this.loadAdapter(rate.media.map { it.file_name.toString() }.toList())
                }
                binding.totalCustomer.text = getString(R.string.total, it.orderItems.size)
                binding.totalBuyers.text = getString(R.string.total, it.stockItems.size)
                binding.customersWeight.text = getString(R.string.total_weight, it.orderItems.sumOf { it.weight })
                binding.buyersWeight.text = getString(R.string.total_weight, it.stockItems.sumOf { it.weight })
                WaitDialog.dismiss()
            }
        }
    }

    override fun imagePick(bitmap: Bitmap, fileName: String, uri: Uri?) {
        super.imagePick(bitmap, fileName ,uri)
        if (shopData.dailyRates.isNotEmpty()){
            shopViewModel.storeFile(
                "dailyRates",
                shopData.dailyRates.first().id.toString(),
                fileName,
                FileManager.getFileWithName(fileName = fileName)
            )
            adapter?.addItem(fileName)
        }
    }

    private fun loadData(date: Date){
        WaitDialog.show("Load Data...")
        shopViewModel.setData(date.formattedDate(), listOf("dailyRates","orderItems","stockItems"))
        binding.textFieldSaleDate.editText?.setText(date.formattedDate("MMM d, yyyy"))
    }

    private fun loadAdapter(list: List<String>){
        adapter = PagerAdapter(requireContext(), list.toMutableList())
        binding.viewPagerMain.adapter = adapter
    }
}