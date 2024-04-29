package com.xdeveloperss.fourbrothers.ui.main.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xdeveloperss.fourbrothers.data.responses.OrderItems
import com.xdeveloperss.fourbrothers.ui.main.MainRepo
import com.xdeveloperss.fourbrothers.ui.main.MainViewModel
import kotlinx.coroutines.launch

class ShopViewModel(repository: MainRepo): MainViewModel(repository) {

    private val _customersList: MutableLiveData<List<OrderItems>> by lazy {
        MutableLiveData()
    }

    val customersList: LiveData<List<OrderItems>>
        get() = _customersList

    fun setCustomsList(list: List<OrderItems>) = viewModelScope.launch {
        _customersList.value = list
    }


    private val _selectedData: MutableLiveData<OrderItems> by lazy {
        MutableLiveData()
    }

    val selectedData: LiveData<OrderItems>
        get() = _selectedData

    fun setSelectData(item: OrderItems) = viewModelScope.launch {
        _selectedData.value = item
    }
}