package com.xdeveloperss.fourbrothers.ui.main.ui.expense

import android.graphics.Bitmap
import android.net.Uri
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.kongzue.dialogx.dialogs.WaitDialog
import com.xdeveloperss.fourbrothers.adapters.AdapterAction
import com.xdeveloperss.fourbrothers.adapters.AdapterType
import com.xdeveloperss.fourbrothers.adapters.GenericAdapter
import com.xdeveloperss.fourbrothers.custom.XDialogBuilder
import com.xdeveloperss.fourbrothers.custom.XDialogType
import com.xdeveloperss.fourbrothers.data.models.Expense
import com.xdeveloperss.fourbrothers.data.models.ExpenseType
import com.xdeveloperss.fourbrothers.data.models.Media
import com.xdeveloperss.fourbrothers.data.models.Supply
import com.xdeveloperss.fourbrothers.data.models.VendorSupplieItems
import com.xdeveloperss.fourbrothers.databinding.FragmentExpenseBinding
import com.xdeveloperss.fourbrothers.ui.main.ui.supplie.SupplieFragmentDirections
import com.xdeveloperss.fourbrothers.utils.FileManager
import com.xdeveloperss.fourbrothers.utils.backWithDelay
import com.xdeveloperss.fourbrothers.utils.dateMilliSec
import com.xdeveloperss.fourbrothers.utils.formattedDate
import com.xdeveloperss.fourbrothers.utils.showDateDialogWithDate
import com.xdeveloperss.fourbrothers.utils.string
import com.xdeveloperss.fourbrothers.utils.value
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.utlis.Prefs
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Date

class ExpenseFragment: XBaseFragment<FragmentExpenseBinding>(FragmentExpenseBinding::inflate)  {
    private val expenseViewModel:ExpenseViewModel by sharedViewModel()

    private lateinit var expense: Expense
    private var expenseTypes: List<ExpenseType> = listOf()
    private var selectedIndex: Int = 0
    override fun onViewCreated() {
        this.loadData(Prefs.getString(this::class.java.name) ?: Date().formattedDate())

        binding.textFieldExpenseDate.editText?.setOnClickListener {
            requireActivity().showDateDialogWithDate(binding.textFieldExpenseDate.string().dateMilliSec(),completion = { string, date ->
                this.loadData(string)
            })
        }
        binding.addExpense.setOnClickListener {
            XDialogBuilder(requireActivity(),Expense()).setData(type = XDialogType.EXPENSE, expenseTypes.map { it.createdAt }) {
                val newExp = it as Expense
                newExp.expenseTypesID = expenseTypes[newExp.expenseTypesID.value()].id
                newExp.addedAt = binding.textFieldExpenseDate.string()
                expenseViewModel.saveData(null,"expenses", newExp)
                binding.root.backWithDelay {
                    this.onViewCreated()
                }
            }.show()
        }

        expenseViewModel.getData.observe { resp ->
            resp.getValueFromResponse()?.data?.let {
                this.expenseTypes = it.expenseType
                binding.expenseRV.adapter = GenericAdapter(type = AdapterType.EXPENSE, it.expenses)
                { i, action, exp ->
                    expense = exp
                    when(action){
                        AdapterAction.EDIT -> {}
                        AdapterAction.DELETE -> {}
                        AdapterAction.SELECT -> {
                            XDialogBuilder(requireActivity(), exp).setData(type = XDialogType.EXPENSE) {
                                expenseViewModel.saveData(null,"expenses", it as Expense)
                                binding.expenseRV.adapter?.notifyItemChanged(i)
                            }.show()
                        }
                        AdapterAction.PICKER -> {
                            selectedIndex = i
                            cropImage.launch(
                                options{
                                    setGuidelines(CropImageView.Guidelines.ON)
                                    setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun imagePick(bitmap: Bitmap, fileName: String, uri: Uri?) {
        super.imagePick(bitmap, fileName ,uri)
        expenseViewModel.storeFile(
            "expenses",
            expense.id.toString(),
            fileName,
            FileManager.getFileWithName(fileName = fileName))
        Media().also {
            it.file_name = fileName
            expense.media.add(it)
        }
        binding.expenseRV.adapter?.notifyItemChanged(selectedIndex)
    }

    private fun loadData(date: String){
        expenseViewModel.setData(date, listOf("expenses", "expenseType"))
        binding.textFieldExpenseDate.editText?.setText(date)
        Prefs.putString(this::class.java.name, date)
    }

}