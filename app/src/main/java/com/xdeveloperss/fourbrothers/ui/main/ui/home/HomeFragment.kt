package com.xdeveloperss.fourbrothers.ui.main.ui.home

import android.text.InputType
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.dialogs.InputDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import com.kongzue.dialogx.style.MaterialStyle
import com.kongzue.dialogx.util.InputInfo
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.custom.XDialogBuilder
import com.xdeveloperss.fourbrothers.data.models.KachraPayment
import com.xdeveloperss.fourbrothers.data.models.MandiRate
import com.xdeveloperss.fourbrothers.data.responses.DailyRates
import com.xdeveloperss.fourbrothers.data.responses.Data
import com.xdeveloperss.fourbrothers.databinding.FragmentHomeBinding
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.ShopViewModel
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

class HomeFragment: XBaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate)  {

    private val shopViewModel: ShopViewModel by sharedViewModel()

    private lateinit var shopData: Data
    override fun onViewCreated() {

        this.loadData(Prefs.getString(this::class.java.name) ?: Date().formattedDate())

        shopViewModel.getData.observe { resp ->
            resp.getValueFromResponse()?.data?.let {
                shopData = it
                setRateData()
            }
        }
        shopViewModel.saveData.observe { resp ->
            resp.data?.let { data ->
                data.dailyRates?.let {
                    shopData.dailyRates = it
                }
                data.mandiRates?.let {
                    shopData.mandiRates = it
                }
                setRateData()
            }
        }

        binding.textFieldDate.editText?.setOnClickListener {
            requireActivity().showDateDialogWithDate( binding.textFieldDate.string().dateMilliSec(),completion = { string, date ->
                this.loadData(string)
            })
        }

        binding.chickenRateField.editText?.setOnClickListener {
            this.showRateAlert()
        }
        binding.zindaRateField.editText?.setOnClickListener {
            this.showRateAlert()
        }
        binding.mandiRateField.editText?.setOnClickListener {
            this.showAdvanceAlert(shopData.mandiRates ?: MandiRate())
        }
    }
    private fun setRateData(){
        binding.chickenRateField.text(shopData.dailyRates?.chickenrate)
        binding.zindaRateField.text(shopData.dailyRates?.zindarate)
        binding.mandiRateField.text(shopData.mandiRates?.rate)
    }

    private fun showAdvanceAlert(mandi: MandiRate){
        InputDialog("Supply Mandi Rate", "", "Save", "Cancel", "Enter Amount")
            .setInputText((mandi.rate ?: "").toString())
            .setInputInfo(
                InputInfo()
                    .setInputType(InputType.TYPE_CLASS_NUMBER)
                    .setCursorColor(resources.getColor(R.color.seed))
                    .setBottomLineColor(
                        if (DialogX.globalStyle is MaterialStyle) resources.getColor(
                            R.color.seed
                        ) else 0
                    )
            )
            .setOkButton { _, _, inputStr ->
                mandi.rate = inputStr.toString().toLong()
                mandi.addedAt = binding.textFieldDate.string()
                shopViewModel.saveData(null,"mandiRates", mandi)
                false
            }
            .show()
    }

    private fun showRateAlert(){
        XDialogBuilder(requireActivity(),shopData.dailyRates ?: DailyRates()).setData {
            shopViewModel.saveData(null, "dailyRates", it as DailyRates)
        }.show()

    }

    private fun loadData(date: String){
        shopViewModel.setData(date, listOf("dailyRates","mandiRates"))
        binding.textFieldDate.editText?.setText(date)
        Prefs.putString(this::class.java.name, date)
    }

}