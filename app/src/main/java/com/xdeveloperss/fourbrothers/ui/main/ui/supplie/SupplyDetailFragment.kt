package com.xdeveloperss.fourbrothers.ui.main.ui.supplie

import com.kongzue.dialogx.dialogs.WaitDialog
import com.xdeveloperss.fourbrothers.data.responses.Data
import com.xdeveloperss.fourbrothers.databinding.FragmentSupplyDetailBinding
import com.xdeveloperss.fourbrothers.utils.formattedDate
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.utlis.Prefs
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Date

class SupplyDetailFragment : XBaseFragment<FragmentSupplyDetailBinding>(FragmentSupplyDetailBinding::inflate) {
    private val supplieViewModel: SupplieViewModel by sharedViewModel()

    private lateinit var supply: Data
    override fun onViewCreated() {

        this.loadData(Prefs.getString("selectedSupplieDate") ?: Date().formattedDate())
        supplieViewModel.getData.observe { resp ->
            resp.getValueFromResponse()?.data?.let {

            }
        }
    }
    private fun loadData(date: String){
        WaitDialog.show("Load Data...")
        supplieViewModel.setData(date, listOf("supplie"))
        Prefs.putString("selectedDate", date)
    }

}