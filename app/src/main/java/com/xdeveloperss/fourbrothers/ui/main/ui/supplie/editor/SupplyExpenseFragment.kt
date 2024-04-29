package com.xdeveloperss.fourbrothers.ui.main.ui.supplie.editor

import android.graphics.Bitmap
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.xdeveloperss.fourbrothers.adapters.AdapterAction
import com.xdeveloperss.fourbrothers.adapters.AdapterType
import com.xdeveloperss.fourbrothers.adapters.GenericAdapter
import com.xdeveloperss.fourbrothers.custom.XDialogBuilder
import com.xdeveloperss.fourbrothers.custom.XDialogType
import com.xdeveloperss.fourbrothers.data.models.VendorSupplieExpense
import com.xdeveloperss.fourbrothers.data.models.VendorSupplieItems
import com.xdeveloperss.fourbrothers.databinding.FragmentSupplyExpenseBinding
import com.xdeveloperss.fourbrothers.ui.main.ui.supplie.SupplieViewModel
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SupplyExpenseFragment : XBaseFragment<FragmentSupplyExpenseBinding>(FragmentSupplyExpenseBinding::inflate) {
    private val supplyViewModel: SupplieViewModel by sharedViewModel()
    override fun onViewCreated() {
        supplyViewModel.getVendorItemExpense.observe {
            binding.expensesRV.adapter = GenericAdapter(type = AdapterType.SUPPLY_EXPENSE, it)
            { i, action, item ->
                XDialogBuilder(requireActivity(), item).setData(type = XDialogType.SUPPLY_EXPENSE) {
                    supplyViewModel.saveData(null,"vendorSupplieExpenses", it as VendorSupplieExpense)
                    binding.expensesRV.adapter?.notifyItemChanged(i)
                }.show()
            }
        }
        binding.addExpense.setOnClickListener {

        }
    }
}