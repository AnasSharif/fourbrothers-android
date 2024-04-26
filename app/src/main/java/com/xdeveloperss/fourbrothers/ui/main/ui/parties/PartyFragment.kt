package com.xdeveloperss.fourbrothers.ui.main.ui.parties

import com.kongzue.dialogx.dialogs.WaitDialog
import com.xdeveloperss.fourbrothers.adapters.AdapterType
import com.xdeveloperss.fourbrothers.adapters.GenericAdapter
import com.xdeveloperss.fourbrothers.data.models.Person
import com.xdeveloperss.fourbrothers.data.responses.ShopItemData
import com.xdeveloperss.fourbrothers.databinding.FragmentPartyBinding
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.ShopViewModel
import com.xdeveloperss.fourbrothers.utils.formattedDate
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Date

class PartyFragment : XBaseFragment<FragmentPartyBinding>(FragmentPartyBinding::inflate) {

    private val partyViewModel: PartyViewModel by sharedViewModel()
    override fun onViewCreated() {

        partyViewModel.getData.observe { response->
            response.getValueFromResponse()?.data?.let {
                binding.partiesRV.adapter = GenericAdapter(type = AdapterType.PERSON, it.persons)
                { i, action, person ->
                }
            }
        }
        this.loadData()
    }
    private fun loadData(){
        WaitDialog.show("Load Data...")
        partyViewModel.setData(types = listOf("persons"))
    }
}