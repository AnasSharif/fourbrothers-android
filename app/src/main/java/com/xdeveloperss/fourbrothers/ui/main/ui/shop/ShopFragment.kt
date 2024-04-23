package com.xdeveloperss.fourbrothers.ui.main.ui.shop

import androidx.navigation.fragment.findNavController
import com.kongzue.dialogx.dialogs.WaitDialog
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.data.responses.ShopItemData
import com.xdeveloperss.fourbrothers.data.responses.ShopResponse
import com.xdeveloperss.fourbrothers.databinding.FragmentShopBinding
import com.xdeveloperss.fourbrothers.ui.join.data.AuthViewModel
import com.xdeveloperss.fourbrothers.utils.formattedDate
import com.xdeveloperss.fourbrothers.utils.showDateDialogWithDate
import com.xdeveloperss.fourbrothers.utils.text
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date

class ShopFragment : XBaseFragment<FragmentShopBinding>(FragmentShopBinding::inflate) {

    private val shopViewModel: ShopViewModel by sharedViewModel()

    private lateinit var shopData: ShopResponse
    override fun onViewCreated() {

        this.loadData(Date())
        binding.textFieldSaleDate.editText?.setOnClickListener {
            requireActivity().showDateDialogWithDate( completion = { _, date ->
                this.loadData(date)
            })
        }
        binding.customerDetail.setOnClickListener {
            findNavController().navigate(ShopFragmentDirections.actionNavShopToCustomerFragment())
            shopViewModel.setCustomsList(shopData.orders.sortedBy { it.personName })
        }
        binding.buyersDetail.setOnClickListener {
            findNavController().navigate(ShopFragmentDirections.actionNavShopToBuyerFragment())
            shopViewModel.setCustomsList(shopData.buyers.sortedBy { it.personName })
        }

        shopViewModel.shopData.observe { resp->
            resp.getValueFromResponse()?.let {
                binding.chickenRateField.text(it.rate?.chickenrate)
                binding.zindaRateField.text(it.rate?.zindarate)
                binding.totalCustomer.text = getString(R.string.total, it.orders.size)
                binding.totalBuyers.text = getString(R.string.total, it.buyers.size)

                binding.customersWeight.text = getString(R.string.total_weight, it.orders.sumOf { it.weight })
                binding.buyersWeight.text = getString(R.string.total_weight, it.buyers.sumOf { it.weight })
                shopData = it
                WaitDialog.dismiss()
            }
        }
    }

    fun loadData(date: Date){
        WaitDialog.show("Load Data...")
        shopViewModel.getShopData(date.formattedDate())
        binding.textFieldSaleDate.editText?.setText(date.formattedDate("MMM d, yyyy"))
    }
}