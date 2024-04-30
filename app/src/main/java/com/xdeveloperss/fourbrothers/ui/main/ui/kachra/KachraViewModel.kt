package com.xdeveloperss.fourbrothers.ui.main.ui.kachra

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xdeveloperss.fourbrothers.data.models.Person
import com.xdeveloperss.fourbrothers.data.responses.OrderItems
import com.xdeveloperss.fourbrothers.ui.main.MainRepo
import com.xdeveloperss.fourbrothers.ui.main.MainViewModel
import kotlinx.coroutines.launch

class KachraViewModel(repository: MainRepo): MainViewModel(repository) {

    private val _selectedParty: MutableLiveData<Person> by lazy {
        MutableLiveData()
    }

    val selectedParty: LiveData<Person>
        get() = _selectedParty

    fun setSelectParty(item: Person) = viewModelScope.launch {
        _selectedParty.value = item
    }
}