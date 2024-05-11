package com.xdeveloperss.fourbrothers.ui.main.ui.parties

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xdeveloperss.fourbrothers.data.models.Person
import com.xdeveloperss.fourbrothers.ui.main.MainRepo
import com.xdeveloperss.fourbrothers.ui.main.MainViewModel
import kotlinx.coroutines.launch

class PartyViewModel(private val repository: MainRepo): MainViewModel(repository) {

    private val _selectedParty: MutableLiveData<Person?> by lazy {
        MutableLiveData()
    }

    val selectedParty: LiveData<Person?>
        get() = _selectedParty

    fun setSelectParty(item: Person? = null) = viewModelScope.launch {
        _selectedParty.value = item
    }

    private val _detailParty: MutableLiveData<Person> by lazy {
        MutableLiveData()
    }

    val detailParty: LiveData<Person>
        get() = _detailParty

    fun setDetailParty(item: Person) = viewModelScope.launch {
        _detailParty.value = item
    }
}