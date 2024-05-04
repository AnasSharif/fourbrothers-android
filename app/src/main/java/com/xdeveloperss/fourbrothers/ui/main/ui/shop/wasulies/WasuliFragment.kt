package com.xdeveloperss.fourbrothers.ui.main.ui.shop.wasulies

import android.graphics.Bitmap
import android.net.Uri
import android.text.InputType
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ToastUtils
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.dialogs.InputDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import com.kongzue.dialogx.style.MaterialStyle
import com.kongzue.dialogx.util.InputInfo
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.adapters.AdapterAction
import com.xdeveloperss.fourbrothers.adapters.AdapterType
import com.xdeveloperss.fourbrothers.adapters.GenericAdapter
import com.xdeveloperss.fourbrothers.data.models.Expense
import com.xdeveloperss.fourbrothers.data.models.ExpenseType
import com.xdeveloperss.fourbrothers.data.models.KachraPayment
import com.xdeveloperss.fourbrothers.data.models.Media
import com.xdeveloperss.fourbrothers.data.responses.VasuliItem
import com.xdeveloperss.fourbrothers.databinding.FragmentWasuliBinding
import com.xdeveloperss.fourbrothers.ui.main.ui.parties.PartyViewModel
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.ShopFragmentDirections
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.ShopViewModel
import com.xdeveloperss.fourbrothers.utils.FileManager
import com.xdeveloperss.fourbrothers.utils.K
import com.xdeveloperss.fourbrothers.utils.value
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import okhttp3.internal.filterList
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WasuliFragment : XBaseFragment<FragmentWasuliBinding>(FragmentWasuliBinding::inflate) {

    private val wasuliViewModel: WasuliViewModel by sharedViewModel()
    private val partyViewModel: PartyViewModel by sharedViewModel()

    private var selectedIndex: Int = 0

    private lateinit var wasuli: VasuliItem

    private var items: MutableList<VasuliItem> = mutableListOf()
    override fun onViewCreated() {

        wasuliViewModel.wasulies.observe {
            items = it
            setupAdapter(it)
        }
        wasuliViewModel.saveData.observe { resp->
            resp.data?.vasuliItems?.first()?.let { item ->
               if (items.any{it.id == item.id}){
                   binding.wasliesRV.adapter?.notifyItemChanged(selectedIndex)
               }else{
                   wasuliViewModel.setAddItem(item)
                   wasuliViewModel.setWasulies(wasuliViewModel.wasulies.value ?: mutableListOf())
               }
            }
        }
        binding.addWassuli.setOnClickListener {
            WasuliFragmentDirections.actionWasuliFragmentToNavParties().also {
                arguments?.putBoolean(K.SELECT_PARTY, true)
                findNavController().navigate(it)
            }
        }
        partyViewModel.selectedParty.observe { party->
            party?.let {
                showAdvanceAlert(VasuliItem(person = it, personsID = it.id))
            }
        }
    }
    override fun imagePick(bitmap: Bitmap, fileName: String, uri: Uri?) {
        super.imagePick(bitmap, fileName ,uri)
        wasuliViewModel.storeFile(
            "vasuliItems",
            wasuli.id.toString(),
            fileName,
            FileManager.getFileWithName(fileName = fileName))
        Media().also {
            it.file_name = fileName
            wasuli.media.add(it)
        }
        binding.wasliesRV.adapter?.notifyItemChanged(selectedIndex)
    }
    private fun setupAdapter(list: MutableList<VasuliItem>){
        binding.wasliesRV.adapter = GenericAdapter(type = AdapterType.CASH_RECEIVING, list.sortedByDescending { it.createdAt })
        { i, action, obj ->
            selectedIndex = i
            wasuli = obj
            when(action){
                AdapterAction.SELECT -> { showAdvanceAlert(obj) }
                AdapterAction.PICKER -> {
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

    private fun showAdvanceAlert(item: VasuliItem){
        InputDialog("Cash Wasuli", "", "Save", "Cancel", "Enter Amount")
            .setInputText( (item.amount ?:"").toString() )
            .setInputInfo(
                InputInfo()
                    .setInputType(InputType.TYPE_CLASS_NUMBER)
                    .setCursorColor(resources.getColor(R.color.seed))
                    .setBottomLineColor(
                        if (DialogX.globalStyle is MaterialStyle) resources.getColor(
                            R.color.seed
                        ) else 0
                    )
            ).setOkButton { _, _, inputStr ->
                item.amount = inputStr.toString().toDouble().toLong()
                wasuliViewModel.saveData(null,"vasuliItems", item)
                partyViewModel.setSelectParty(null)
                false
            }
            .setCancelButton{ _, _ ->
                partyViewModel.setSelectParty(null)
                false
            }

            .show()
    }
}