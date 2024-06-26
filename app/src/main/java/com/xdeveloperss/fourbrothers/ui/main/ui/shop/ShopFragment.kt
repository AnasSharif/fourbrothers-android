package com.xdeveloperss.fourbrothers.ui.main.ui.shop

import android.graphics.Bitmap
import android.net.Uri
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ToastUtils
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.kongzue.dialogx.dialogs.WaitDialog
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.adapters.PagerAdapter
import com.xdeveloperss.fourbrothers.custom.XDialogBuilder
import com.xdeveloperss.fourbrothers.data.BaseResponseRepo
import com.xdeveloperss.fourbrothers.data.responses.DailyRates
import com.xdeveloperss.fourbrothers.data.responses.Data
import com.xdeveloperss.fourbrothers.databinding.FragmentShopBinding
import com.xdeveloperss.fourbrothers.databinding.RateDialogViewBinding
import com.xdeveloperss.fourbrothers.ui.join.data.AuthViewModel
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.wasulies.WasuliViewModel
import com.xdeveloperss.fourbrothers.utils.FileManager
import com.xdeveloperss.fourbrothers.utils.Int
import com.xdeveloperss.fourbrothers.utils.backWithDelay
import com.xdeveloperss.fourbrothers.utils.dateMilliSec
import com.xdeveloperss.fourbrothers.utils.formattedDate
import com.xdeveloperss.fourbrothers.utils.glideLoad
import com.xdeveloperss.fourbrothers.utils.showDateDialogWithDate
import com.xdeveloperss.fourbrothers.utils.string
import com.xdeveloperss.fourbrothers.utils.text
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.utlis.Prefs
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date

class ShopFragment : XBaseFragment<FragmentShopBinding>(FragmentShopBinding::inflate) {

    private val shopViewModel: ShopViewModel by sharedViewModel()
    private val vasuliViewModel: WasuliViewModel by sharedViewModel()

    private lateinit var shopData: Data
    override fun onViewCreated() {

        binding.root.backWithDelay {
            this.loadData(Prefs.getString(this::class.java.name) ?: Date().formattedDate())
        }

        binding.textFieldSaleDate.editText?.setOnClickListener {
            requireActivity().showDateDialogWithDate( binding.textFieldSaleDate.string().dateMilliSec(),completion = { string, date ->
                this.loadData(string)
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
        binding.cashWasuliCard.setOnClickListener {
            findNavController().navigate(ShopFragmentDirections.actionNavShopToWasuliFragment())
            vasuliViewModel.setWasulies(shopData.vasuliItems.sortedBy { it.createdAt }.toMutableList())
        }
        binding.imagePicker.setOnClickListener {
            if (shopData.dailyRates == null){
                binding.chickenRateField.editText?.performClick()
                return@setOnClickListener
            }
            cropImage.launch(
                options{
                    setGuidelines(CropImageView.Guidelines.ON)
                    setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                }
            )
        }
        binding.chickenRateField.editText?.setOnClickListener {
           this.showRateAlert()
        }
        binding.zindaRateField.editText?.setOnClickListener {
            this.showRateAlert()
        }
        shopViewModel.getData.observe { resp ->
            resp.getValueFromResponse()?.data?.let {
                shopData = it
                binding.chickenRateField.text(shopData.dailyRates?.chickenrate)
                binding.zindaRateField.text(shopData.dailyRates?.zindarate)
                binding.totalCustomer.text = getString(R.string.total, it.orderItems.size)
                binding.totalBuyers.text = getString(R.string.total, it.stockItems.size)
                binding.customersWeight.text = getString(R.string.total_weight, it.orderItems.sumOf { it.weight })
                binding.buyersWeight.text = getString(R.string.total_weight, it.stockItems.sumOf { it.weight })
                binding.cashWasuli.text = getString(R.string.total, it.vasuliItems.count())

                this.loadAdapter(shopData.dailyRates?.media?.toMutableList() ?: listOf(), binding.viewPagerMain)
                WaitDialog.dismiss()
            }
        }
    }

    override fun imagePick(bitmap: Bitmap, fileName: String, uri: Uri?) {
        super.imagePick(bitmap, fileName ,uri)
        shopData.dailyRates?.let {
            shopViewModel.storeFile(
                "dailyRates",
                it.id.toString(),
                fileName,
                FileManager.getFileWithName(fileName = fileName)
            )
            adapter?.addItem(fileName)
        }
    }

    private fun showRateAlert(){
        XDialogBuilder(requireActivity(),shopData.dailyRates ?: DailyRates()).setData {
            shopViewModel.saveData(null, "dailyRates", it as DailyRates)
        }.show()
        shopViewModel.saveData.observe {resp ->
            resp.data?.dailyRates?.let {
                binding.chickenRateField.text(it.chickenrate)
                binding.zindaRateField.text(it.zindarate)
            }
        }
    }
    private fun loadData(date: String){
        val queryParams = mutableMapOf<String, String>()
        queryParams["added_at"] = date // Add the "added_at" parameter if needed
        shopViewModel.setData(queryParams, listOf("dailyRates","orderItems","stockItems","vasuliItems"))
        binding.textFieldSaleDate.editText?.setText(date)
        Prefs.putString(this::class.java.name, date)
    }
}