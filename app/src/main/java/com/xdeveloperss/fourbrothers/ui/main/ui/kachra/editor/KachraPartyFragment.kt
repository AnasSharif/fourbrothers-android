package com.xdeveloperss.fourbrothers.ui.main.ui.kachra.editor

import android.graphics.Bitmap
import android.net.Uri
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.kongzue.dialogx.dialogs.WaitDialog
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.adapters.AdapterAction
import com.xdeveloperss.fourbrothers.adapters.AdapterType
import com.xdeveloperss.fourbrothers.adapters.GenericAdapter
import com.xdeveloperss.fourbrothers.data.models.KachraPayment
import com.xdeveloperss.fourbrothers.data.models.Media
import com.xdeveloperss.fourbrothers.data.models.Product
import com.xdeveloperss.fourbrothers.databinding.FragmentKachraPartyBinding
import com.xdeveloperss.fourbrothers.ui.main.MainActivity
import com.xdeveloperss.fourbrothers.ui.main.ui.kachra.KachraFragmentDirections
import com.xdeveloperss.fourbrothers.ui.main.ui.kachra.KachraViewModel
import com.xdeveloperss.fourbrothers.utils.FileManager
import com.xdeveloperss.fourbrothers.utils.value
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class KachraPartyFragment : XBaseFragment<FragmentKachraPartyBinding>(FragmentKachraPartyBinding::inflate) {

    private val paymentViewModel: KachraViewModel by sharedViewModel()

    private var selectedIndex: Int = 0

    private lateinit var payment: KachraPayment
    override fun onViewCreated() {

        paymentViewModel.selectedParty.observe {
            barTitle(it.name)

            val advance = it.kachraPayments.let {
                it.filter { it.paymentType=="advance" }.sumOf { it.amount.toInt() }
            }
            val cash = it.kachraPayments.let {
                it.filter { it.paymentType=="cash" }.sumOf { it.amount.toInt() }
            }
            binding.balanceAmount.text = getString(R.string.total, (advance.value()-cash.value()))

            binding.paymentsRV.adapter = GenericAdapter(type = AdapterType.KACHRA_PAYMENT, it.kachraPayments.sortedByDescending { it.createdAt })
            { i, action, pro ->
                payment = pro
                when (action) {
                    AdapterAction.SELECT -> {

                    }AdapterAction.PICKER->{
                    selectedIndex = i
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
    }
    override fun imagePick(bitmap: Bitmap, fileName: String, uri: Uri?) {
        super.imagePick(bitmap, fileName ,uri)
        WaitDialog.show("Uploading...")
        paymentViewModel.storeFile(
            "dailyKacharaPayment",
            payment.id.toString(),
            fileName,
            FileManager.getFileWithName(fileName = fileName))
        Media().also {
            it.file_name = fileName
            payment.media.add(it)
        }
        binding.paymentsRV.adapter?.notifyItemChanged(selectedIndex)
    }

}