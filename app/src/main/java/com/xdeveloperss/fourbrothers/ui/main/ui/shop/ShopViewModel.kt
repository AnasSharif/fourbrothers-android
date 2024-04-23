package com.xdeveloperss.fourbrothers.ui.main.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}