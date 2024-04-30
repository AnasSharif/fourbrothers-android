package com.xdeveloperss.fourbrothers.ui.main.ui.kachra

import androidx.navigation.fragment.findNavController
import com.xdeveloperss.fourbrothers.adapters.AdapterAction
import com.xdeveloperss.fourbrothers.adapters.AdapterType
import com.xdeveloperss.fourbrothers.adapters.GenericAdapter
import com.xdeveloperss.fourbrothers.databinding.FragmentKachraBinding
import com.xdeveloperss.fourbrothers.ui.main.ui.supplie.SupplieFragmentDirections
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class KachraFragment : XBaseFragment<FragmentKachraBinding>(FragmentKachraBinding::inflate) {

    private val kachraViewModel: KachraViewModel by sharedViewModel()
    override fun onViewCreated() {

        kachraViewModel.setData(null, listOf("kachraBuyer"))
        kachraViewModel.getData.observe { resp ->
            resp.getValueFromResponse()?.data?.let {
                binding.root.adapter = GenericAdapter(type = AdapterType.PERSON, it.kachraBuyer)
                { i, action, pro ->
                    when (action) {
                        AdapterAction.SELECT -> {
                            findNavController().navigate(KachraFragmentDirections.actionNavKachraToKachraPartyFragment())
                            kachraViewModel.setSelectParty(pro)
                        }
                        else -> {}
                    }
                }
            }
        }

    }

}