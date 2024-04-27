package com.xdeveloperss.fourbrothers.ui.main.ui.shop.buyers

import com.xdeveloperss.fourbrothers.adapters.AdapterType
import com.xdeveloperss.fourbrothers.adapters.GenericAdapter
import com.xdeveloperss.fourbrothers.data.responses.OrderItems
import com.xdeveloperss.fourbrothers.databinding.FragmentBuyerBinding
import com.xdeveloperss.fourbrothers.databinding.FragmentSupplieBinding
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.ShopViewModel
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BuyerFragment : XBaseFragment<FragmentBuyerBinding>(FragmentBuyerBinding::inflate) {
    private val shopViewModel: ShopViewModel by sharedViewModel()
    override fun onViewCreated() {

        shopViewModel.customersList.observe {
            binding.buyersRV.adapter = GenericAdapter(type = AdapterType.SHOP, it)
            { i, action, shop ->
                val inv = shop
            }
        }
    }
}