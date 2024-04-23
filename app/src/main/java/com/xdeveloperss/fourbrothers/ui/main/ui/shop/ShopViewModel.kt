package com.xdeveloperss.fourbrothers.ui.main.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xdeveloperss.fourbrothers.data.responses.ShopItemData
import com.xdeveloperss.fourbrothers.data.responses.ShopResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.response.XNetworkResponse
import kotlinx.coroutines.launch

class ShopViewModel(private val repository: ShopRepo): ViewModel() {

    private val _shopData: MutableLiveData<XNetworkResponse<ShopResponse>> by lazy {
        MutableLiveData()
    }

    val shopData: LiveData<XNetworkResponse<ShopResponse>>
        get() = _shopData

    fun getShopData(date: String) = viewModelScope.launch {
        _shopData.value = repository.shopData(date)
    }

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