package com.xdeveloperss.fourbrothers.ui.main.ui.supplie

import com.kongzue.dialogx.dialogs.WaitDialog
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.data.responses.Data
import com.xdeveloperss.fourbrothers.databinding.FragmentSupplyDetailBinding
import com.xdeveloperss.fourbrothers.utils.addPresent
import com.xdeveloperss.fourbrothers.utils.formattedDate
import com.xdeveloperss.fourbrothers.utils.text
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.utlis.Prefs
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Date

class SupplyDetailFragment : XBaseFragment<FragmentSupplyDetailBinding>(FragmentSupplyDetailBinding::inflate) {

    private val supplieViewModel: SupplieViewModel by sharedViewModel()
    private lateinit var supply: Data
    override fun onViewCreated() {

        supplieViewModel.getSupply.observe {
            binding.mandiRateField.text(it.vendorSupplie.rate.toString())
            binding.supplyRateField.text(it.vendorSupplie.mandiRate.toString())
            binding.supplierName.text = it.supplier.name
            binding.supplierRate.text = getString(R.string.rate, it.rate)
            binding.supplierWeight.text = getString(R.string.total_weight, it.weight)

            binding.totalSupplyCustomer.text = getString(R.string.total, it.vendorSupplie.items.size)
            binding.supplyWeight.text = getString(R.string.total_weight, it.vendorSupplie.items.sumOf { it.weight })

            binding.totalExpense.text = getString(R.string.total, it.vendorSupplie.expenses.size)
            binding.expenseAmount.text = getString(R.string.total_amount, it.vendorSupplie.expenses.sumOf { it.amount })
        }
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