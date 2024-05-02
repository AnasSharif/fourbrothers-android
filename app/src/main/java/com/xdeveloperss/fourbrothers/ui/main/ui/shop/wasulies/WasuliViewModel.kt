package com.xdeveloperss.fourbrothers.ui.main.ui.shop.wasulies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xdeveloperss.fourbrothers.data.models.Person
import com.xdeveloperss.fourbrothers.data.responses.OrderItems
import com.xdeveloperss.fourbrothers.data.responses.VasuliItem
import com.xdeveloperss.fourbrothers.ui.main.MainRepo
import com.xdeveloperss.fourbrothers.ui.main.MainViewModel
import kotlinx.coroutines.launch

class WasuliViewModel(repository: MainRepo): MainViewModel(repository) {

    private val _wasuliList: MutableLiveData<MutableList<VasuliItem>> by lazy {
        MutableLiveData()
    }

    val wasulies: LiveData<MutableList<VasuliItem>>
        get() = _wasuliList

    fun setWasulies(list: MutableList<VasuliItem>) = viewModelScope.launch {
        _wasuliList.value = list
    }
    fun setAddItem(item: VasuliItem) = viewModelScope.launch {
        _wasuliList.value?.add(item)
    }
}