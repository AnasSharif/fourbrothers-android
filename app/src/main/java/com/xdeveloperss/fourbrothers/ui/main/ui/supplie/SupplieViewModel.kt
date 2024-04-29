package com.xdeveloperss.fourbrothers.ui.main.ui.supplie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xdeveloperss.fourbrothers.data.models.Supply
import com.xdeveloperss.fourbrothers.data.responses.OrderItems
import com.xdeveloperss.fourbrothers.ui.main.MainRepo
import com.xdeveloperss.fourbrothers.ui.main.MainViewModel
import kotlinx.coroutines.launch

class SupplieViewModel(repository: MainRepo): MainViewModel(repository) {

    private val _selectedSupply: MutableLiveData<Supply> by lazy {
        MutableLiveData()
    }

    val getSupply: LiveData<Supply>
        get() = _selectedSupply

    fun setSupply(item: Supply) = viewModelScope.launch {
        _selectedSupply.value = item
    }

}