package com.xdeveloperss.fourbrothers.ui.main.ui.expense

import androidx.navigation.fragment.findNavController
import com.xdeveloperss.fourbrothers.data.models.Expense
import com.xdeveloperss.fourbrothers.data.models.ExpenseType
import com.xdeveloperss.fourbrothers.databinding.FragmentExpenseEditorBinding
import com.xdeveloperss.fourbrothers.utils.backWithDelay
import com.xdeveloperss.fourbrothers.utils.dateMilliSec
import com.xdeveloperss.fourbrothers.utils.double
import com.xdeveloperss.fourbrothers.utils.formattedDate
import com.xdeveloperss.fourbrothers.utils.showDateDialogWithDate
import com.xdeveloperss.fourbrothers.utils.string
import com.xdeveloperss.fourbrothers.utils.value
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Date

class ExpenseEditorFragment: XBaseFragment<FragmentExpenseEditorBinding>(FragmentExpenseEditorBinding::inflate)  {

    private val expenseViewModel:ExpenseViewModel by sharedViewModel()
    private var expense: Expense? = null
    private var expenseTypes: List<ExpenseType> = listOf()
    override fun onViewCreated() {

        binding.dateField.editText?.setOnClickListener {
            requireActivity().showDateDialogWithDate(binding.dateField.string().dateMilliSec(),completion = { string, _ ->
                binding.dateField.editText?.setText(string)
            })
        }
        binding.dateField.editText?.setText(Date().formattedDate())
        binding.root.backWithDelay {
            expenseViewModel.setData(null, listOf("expenseType"))
        }
        expenseViewModel.getData.observe { resp ->
            resp.getValueFromResponse()?.data?.let {
                this.expenseTypes = it.expenseType
                loadPersonProduct(it.expenseType.map { it.name }, binding.expenseTypeSelection){ s: String, i: Int ->
                    expense?.expenseTypesID = this.expenseTypes[i].id
                }
            }
        }
        expenseViewModel.selectedExpense.observe {
            this.expense = it
        }
        binding.saveBtn.setOnClickListener {
            expense?.let { exp->
                if(exp.expenseTypesID == null){
                    binding.expenseTypeSelection.error = "Expense type required!"
                    return@setOnClickListener
                }
                binding.expenseTypeSelection.error = null
                if(binding.expenseAmount.editText?.text.isNullOrEmpty()){
                    binding.expenseAmount.error = "Expense amount required!"
                    return@setOnClickListener
                }
                binding.expenseAmount.error = null
                exp.amount = binding.expenseAmount.double()
                exp.addedAt = binding.dateField.editText?.text.toString()
                expenseViewModel.saveData(null,"expenses", exp)
                findNavController().popBackStack()
            }
        }
    }

}