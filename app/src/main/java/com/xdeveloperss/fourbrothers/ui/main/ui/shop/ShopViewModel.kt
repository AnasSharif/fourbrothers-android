package com.xdeveloperss.fourbrothers.ui.main.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xdeveloperss.fourbrothers.data.responses.ShopItemData
import com.xdeveloperss.fourbrothers.ui.main.MainRepo
import com.xdeveloperss.fourbrothers.ui.main.MainViewModel
import kotlinx.coroutines.launch

class ShopViewModel(private val repository: MainRepo): MainViewModel(repository) {

    private val _customersList: MutableLiveData<List<ShopItemData>> by lazy {
        MutableLiveData()
    }

    val customersList: LiveData<List<ShopItemData>>
        get() = _customersList

    fun setCustomsList(list: List<ShopItemData>) = viewModelScope.launch {
        _customersList.value = list
    }


    private val _selectedData: MutableLiveData<Map<Int,ShopItemData>> by lazy {
        MutableLiveData()
    }

    val selectedData: LiveData<Map<Int,ShopItemData>>
        get() = _selectedData

    fun setSelectData(item: ShopItemData) = viewModelScope.launch {
        _selectedData.value = mapOf(1 to item)
    }
}