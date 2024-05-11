package com.xdeveloperss.fourbrothers.ui.main.ui.kachra.editor

import android.graphics.Bitmap
import android.net.Uri
import android.text.InputType
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.dialogs.InputDialog
import com.kongzue.dialogx.dialogs.PopMenu
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener
import com.kongzue.dialogx.style.MaterialStyle
import com.kongzue.dialogx.util.InputInfo
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.adapters.AdapterAction
import com.xdeveloperss.fourbrothers.adapters.AdapterType
import com.xdeveloperss.fourbrothers.adapters.GenericAdapter
import com.xdeveloperss.fourbrothers.data.models.KachraPayment
import com.xdeveloperss.fourbrothers.data.models.Media
import com.xdeveloperss.fourbrothers.data.models.Person
import com.xdeveloperss.fourbrothers.databinding.FragmentKachraPartyBinding
import com.xdeveloperss.fourbrothers.ui.main.ui.kachra.KachraViewModel
import com.xdeveloperss.fourbrothers.utils.FileManager
import com.xdeveloperss.fourbrothers.utils.ListSorter
import com.xdeveloperss.fourbrothers.utils.backWithDelay
import com.xdeveloperss.fourbrothers.utils.getCurrentMonthAndYear
import com.xdeveloperss.fourbrothers.utils.value
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class KachraPartyFragment : XBaseFragment<FragmentKachraPartyBinding>(FragmentKachraPartyBinding::inflate) {

    private val paymentViewModel: KachraViewModel by sharedViewModel()

    private var selectedIndex: Int = 0

    private var payment: KachraPayment? = null

    private lateinit var party: Person

    override fun onViewCreated() {

        val yearMonth = Date().getCurrentMonthAndYear()
        binding.selectedMonth.text = yearMonth.first
        binding.selectedYear.text = yearMonth.second.toString()

        payment = null
        paymentViewModel.selectedParty.observe {
            this.party = it
            barTitle(it.name)
            this.paymentAdapter()
        }
        binding.advanceBtn.setOnClickListener {
            this.showAdvanceAlert(KachraPayment())
        }
        binding.kachraBtn.setOnClickListener {
            findNavController().navigate(KachraPartyFragmentDirections.actionKachraPartyFragmentToKachraEditorFragment())
            paymentViewModel.setPayment(payment ?: KachraPayment(personsID = party.personsID, paymentType = "cash"))
            paymentViewModel.setPersonProducts(this.party.products ?: listOf())
        }

        binding.monthsPicker.setOnClickListener {
            val months = resources.getStringArray(R.array.months_array)
            PopMenu.show(it, months).onMenuItemClickListener =
                OnMenuItemClickListener { _, text, index ->
                    binding.selectedMonth.text = text
                    this.paymentAdapter()
                    false
                }
        }
        binding.yearPicker.setOnClickListener {
            val years = resources.getStringArray(R.array.years_array)
            PopMenu.show(it, years).onMenuItemClickListener =
                OnMenuItemClickListener { _, text, index ->
                    binding.selectedYear.text = text
                    this.paymentAdapter()
                    false
                }
        }
    }
    private fun showAdvanceAlert(payment: KachraPayment){
        InputDialog("Advance Payment", "", "Save", "Cancel", "Enter Amount")
            .setInputText( (payment.amount ?:"").toString() )
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
                payment.amount = inputStr.toString().toDouble()
                payment.personsID = party.personsID
                payment.paymentType =  "advance"
                paymentViewModel.saveData(null,"dailyKacharaPayment", payment)
                if (payment.id == null){
                    this.party.kachraPayments?.add(payment)
                }else{
                    binding.paymentsRV.adapter?.notifyItemChanged(selectedIndex)
                }
                this.paymentAdapter()

                false
            }
            .show()
    }

    private fun paymentAdapter(){
        val payments = this.party.kachraPayments ?: mutableListOf()
        val advance = payments.let {
            it.filter { it.paymentType=="advance" }.sumOf { it.amount.value() }
        }
        val filterPayments = ListSorter.filterObjectsByMonth(objects = this.party.kachraPayments ?: mutableListOf(),
            targetMonth=binding.selectedMonth.text.toString(), targetYear = binding.selectedYear.text.toString())
        val cash = filterPayments.let {
            it.filter { it.paymentType=="cash" }.sumOf { it.amount.value() }
        }
        binding.balanceAmount.text = getString(R.string.total, (advance-cash).toLong())
        binding.paymentsRV.adapter = GenericAdapter(type = AdapterType.KACHRA_PAYMENT, filterPayments.sortedByDescending { it.createdAt })
        { i, action, pro ->
            payment = pro
            selectedIndex = i
            when (action) {
                AdapterAction.SELECT -> {
                    if (pro.paymentType == "advance"){
                        this.showAdvanceAlert(pro)
                    }else{
                        binding.kachraBtn.performClick()
                    }
                }AdapterAction.PICKER->{
                cropImage.launch(
                    options{
                        setGuidelines(CropImageView.Guidelines.ON)
                        setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                    }
                )
            }
                else -> {}
            }
        }
    }
    override fun imagePick(bitmap: Bitmap, fileName: String, uri: Uri?) {
        super.imagePick(bitmap, fileName ,uri)
        paymentViewModel.storeFile(
            "dailyKacharaPayment",
            payment?.id.toString(),
            fileName,
            FileManager.getFileWithName(fileName = fileName))
        Media().also {
            it.file_name = fileName
            payment?.media?.add(it)
        }
        binding.paymentsRV.adapter?.notifyItemChanged(selectedIndex)
    }

}