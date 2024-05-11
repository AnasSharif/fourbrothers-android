package com.xdeveloperss.fourbrothers.ui.main.ui.parties.detail

import android.app.SearchManager
import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import com.kongzue.dialogx.dialogs.WaitDialog
import com.xdeveloperss.fourbrothers.R
import com.xdeveloperss.fourbrothers.adapters.AdapterAction
import com.xdeveloperss.fourbrothers.adapters.AdapterType
import com.xdeveloperss.fourbrothers.adapters.GenericAdapter
import com.xdeveloperss.fourbrothers.data.models.Person
import com.xdeveloperss.fourbrothers.databinding.FragmentPartyBinding
import com.xdeveloperss.fourbrothers.databinding.FragmentPartyDetailBinding
import com.xdeveloperss.fourbrothers.ui.main.ui.parties.PartyViewModel
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.wasulies.WasuliViewModel
import com.xdeveloperss.fourbrothers.utils.K
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Date

class PartyDetailFragment : XBaseFragment<FragmentPartyDetailBinding>(FragmentPartyDetailBinding::inflate) {

    private val partyViewModel: PartyViewModel by sharedViewModel()

    private lateinit var party: Person
    override fun onViewCreated() {

        partyViewModel.detailParty.observe {
            this.party = it
            barTitle(it.name, it.phonenumber)
        }
    }
}