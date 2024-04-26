package com.xdeveloperss.fourbrothers.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xdeveloperss.fourbrothers.data.BaseResponseRepo
import com.xdeveloperss.fourbrothers.data.responses.ShopItemData
import com.xdeveloperss.fourbrothers.xnetwork.config.response.XNetworkResponse
import kotlinx.coroutines.launch

open class MainViewModel(private val repository: MainRepo): ViewModel() {

    private val _getData: MutableLiveData<XNetworkResponse<BaseResponseRepo>> by lazy {
        MutableLiveData()
    }

    val getData: LiveData<XNetworkResponse<BaseResponseRepo>>
        get() = _getData

    fun setData(date: String? = null, types: List<String>) = viewModelScope.launch {
        _getData.value = repository.getData(date, types)
    }

    private val _customersList: MutableLiveData<List<ShopItemData>> by lazy {
        MutableLiveData()
    }
}