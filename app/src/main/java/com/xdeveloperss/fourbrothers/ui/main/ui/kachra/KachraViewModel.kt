package com.xdeveloperss.fourbrothers.ui.main.ui.kachra

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xdeveloperss.fourbrothers.data.models.KachraPayment
import com.xdeveloperss.fourbrothers.data.models.Person
import com.xdeveloperss.fourbrothers.data.models.PersonProduct
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

    private val _selectPayment: MutableLiveData<KachraPayment> by lazy {
        MutableLiveData()
    }
    val selectPayment: LiveData<KachraPayment>
        get() = _selectPayment

    fun setPayment(item: KachraPayment) = viewModelScope.launch {
        _selectPayment.value = item
    }

    private val _personProducts: MutableLiveData<List<PersonProduct>> by lazy {
        MutableLiveData()
    }
    val personProducts: LiveData<List<PersonProduct>>
        get() = _personProducts

    fun setPersonProducts(items: List<PersonProduct>) = viewModelScope.launch {
        _personProducts.value = items
    }
}