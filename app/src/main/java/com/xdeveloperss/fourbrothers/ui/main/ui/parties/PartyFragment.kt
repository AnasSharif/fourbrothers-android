package com.xdeveloperss.fourbrothers.ui.main.ui.parties

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
import com.xdeveloperss.fourbrothers.ui.main.ui.shop.wasulies.WasuliViewModel
import com.xdeveloperss.fourbrothers.utils.K
import com.xdeveloperss.fourbrothers.xbase.XBaseFragment
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Date

class PartyFragment : XBaseFragment<FragmentPartyBinding>(FragmentPartyBinding::inflate) {

    private val partyViewModel: PartyViewModel by sharedViewModel()
    private var parties: List<Person> = listOf()
    override fun onViewCreated() {

        partyViewModel.getData.observe { response->
            response.getValueFromResponse()?.data?.let {
                parties = it.persons
                setupAdapter(parties)
            }
        }
        arguments?.getBoolean(K.SELECT_PARTY)?.let {
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        this.loadData()
        this.searchBarView()
    }
    private fun setupAdapter(persons: List<Person>){
        binding.partiesRV.adapter = GenericAdapter(type = AdapterType.PERSON, persons)
        { i, action, person ->
            when (action) {
                AdapterAction.SELECT -> {
                    arguments?.getBoolean(K.SELECT_PARTY)?.let {
                        partyViewModel.setSelectParty(person)
                        this.backPressed()
                        return@GenericAdapter
                    }
                }
                else -> {}
            }
        }


    }
    private fun loadData(){
        WaitDialog.show("Load Data...")
        partyViewModel.setData(types = listOf("persons"))
    }

    private fun searchBarView(){

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.searchable_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                val searchManager =
                    requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

                menuItem.actionView?.let {
                    val searchView = it as SearchView
                    searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
                    val queryTextListener = object : SearchView.OnQueryTextListener {
                        override fun onQueryTextChange(newText: String?): Boolean {
                            val pp =  parties.filter { it.name.lowercase().contains(newText.toString().lowercase()) }.toMutableList()
                            setupAdapter(pp)
                            return true
                        }

                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return true
                        }
                    }
                    searchView.setOnQueryTextListener(queryTextListener)
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}