package com.xdeveloperss.fourbrothers.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xdeveloperss.fourbrothers.data.BaseResponseRepo
import com.xdeveloperss.fourbrothers.xnetwork.config.response.XNetworkResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import kotlinx.coroutines.launch
import java.io.File

open class MainViewModel(private val repository: MainRepo): ViewModel() {

    private val _saveData: MutableLiveData<BaseResponseRepo> by lazy {
        MutableLiveData()
    }
    fun <T> saveData(type: Class<T>, data: T) =
        viewModelScope.launch {
            _saveData.value = repository.saveData(type, data).getValueFromResponse()
        }

    private val _getData: MutableLiveData<XNetworkResponse<BaseResponseRepo>> by lazy {
        MutableLiveData()
    }
    val getData: LiveData<XNetworkResponse<BaseResponseRepo>>
        get() = _getData

    fun setData(date: String? = null, types: List<String>) = viewModelScope.launch {
        _getData.value = repository.getData(date, types)
    }

    private val _storeFile: MutableLiveData<BaseResponseRepo> by lazy {
        MutableLiveData()
    }

    fun storeFile(type: String, itemId: String, fileName: String, file: File) =
        viewModelScope.launch {
            _storeFile.value = repository.store(type, itemId, fileName, arrayListOf(file)).getValueFromResponse()
        }
}