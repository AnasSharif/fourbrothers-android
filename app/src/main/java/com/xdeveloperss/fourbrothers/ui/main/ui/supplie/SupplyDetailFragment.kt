package com.xdeveloperss.fourbrothers.ui.main.ui.supplie

import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ToastUtils
import com.kongzue.dialogx.dialogs.WaitDialog
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.custom.XDialogBuilder
import com.xdeveloperss.fourbrothers.custom.XDialogType
import com.xdeveloperss.fourbrothers.data.models.Supply
import com.xdeveloperss.fourbrothers.data.responses.DailyRates
import com.xdeveloperss.fourbrothers.data.responses.Data
import com.xdeveloperss.fourbrothers.databinding.FragmentSupplyDetailBinding
import com.xdeveloperss.fourbrothers.utils.addPresent
import com.xdeveloperss.fourbrothers.utils.formattedDate
import com.xdeveloperss.fourbrothers.utils.rounded
import com.xdeveloperss.fourbrothers.utils.text
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.utlis.Prefs
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Date

class SupplyDetailFragment : XBaseFragment<FragmentSupplyDetailBinding>(FragmentSupplyDetailBinding::inflate) {

    private val supplyViewModel: SupplieViewModel by sharedViewModel()
    lateinit var supply: Supply
    override fun onViewCreated() {

        supplyViewModel.getSupply.observe {
            supply = it
            binding.mandiRateField.text(it.vendorSupplie.rate.toString())
            binding.supplyRateField.text(it.vendorSupplie.mandiRate.toString())
            binding.supplierName.text = it.supplier.name
            binding.supplierRate.text = getString(R.string.rate, it.rate)
            binding.supplierWeight.text = getString(R.string.total_weight, it.weight)

            binding.totalSupplyCustomer.text = getString(R.string.total, it.vendorSupplie.items.size)
            binding.supplyWeight.text = getString(R.string.total_weight, it.vendorSupplie.items.sumOf { it.weight })

            binding.totalExpense.text = getString(R.string.total, it.vendorSupplie.expenses.size)
            binding.expenseAmount.text = getString(R.string.total, it.vendorSupplie.expenses.sumOf { it.amount.toLong() })

            supply.supplieVan?.let {
                binding.pickupNumber.text = getString(R.string.number, it.van?.number.toString())
                binding.driverName.text = getString(R.string.driver, it.driver?.name.toString())
            }
            binding.weightLoss.text = getString(R.string.weight_loss, (it.weight-it.vendorSupplie.items.sumOf { it.weight }).rounded)
        }
        supplyViewModel.getData.observe { resp ->
            resp.getValueFromResponse()?.data?.let {

            }
        }
        binding.supplierContainer.setOnClickListener {
            showRateAlert()
        }
        binding.pickupContainer.setOnClickListener {
            ToastUtils.showShort("Pick up")
        }
        binding.customerDetail.setOnClickListener {
            supplyViewModel.setVendorItems(supply.vendorSupplie.items)
            findNavController().navigate(SupplyDetailFragmentDirections.actionSupplyDetailFragmentToSupplyCustomerFragment())

        }
        binding.expenseDetail.setOnClickListener {
            supplyViewModel.setVendorItemExpense(supply.vendorSupplie.expenses)
            findNavController().navigate(SupplyDetailFragmentDirections.actionSupplyDetailFragmentToSupplyExpenseFragment())
        }
    }
    private fun showRateAlert(){
        XDialogBuilder(requireActivity(), supply).setData(type = XDialogType.SUPPLER) {
            supplyViewModel.saveData(null,"supplies", it as Supply)
            supplyViewModel.setSupply(it)
        }.show()

    }

}