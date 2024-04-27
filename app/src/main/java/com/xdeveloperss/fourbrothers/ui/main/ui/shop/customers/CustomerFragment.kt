package com.xdeveloperss.fourbrothers.ui.main.ui.shop.customers

import SectionHeaderAdapter
import androidx.navigation.fragment.findNavController
import com.kongzue.dialogx.dialogs.PopMenu
import com.xdeveloperss.fourbrothers.adapters.AdapterType
import com.xdeveloperss.fourbrothers.adapters.GenericAdapter

import com.xdeveloperss.fourbrothers.data.Section
import com.xdeveloperss.fourbrothers.databinding.FragmentCustomerBinding
import com.xdeveloperss.fourbrothers.databinding.FragmentSupplieBinding
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.ShopFragmentDirections
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.ShopViewModel
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date

class CustomerFragment : XBaseFragment<FragmentCustomerBinding>(FragmentCustomerBinding::inflate) {
    private val shopViewModel: ShopViewModel by sharedViewModel()
    override fun onViewCreated() {

        shopViewModel.customersList.observe {
            binding.customersRV.adapter = GenericAdapter(type = AdapterType.SHOP, it)
            { i, action, shop ->
                findNavController().navigate(CustomerFragmentDirections.actionCustomerFragmentToShopEditorFragment())
                shopViewModel.setSelectData(shop)
            }
        }
    }
}