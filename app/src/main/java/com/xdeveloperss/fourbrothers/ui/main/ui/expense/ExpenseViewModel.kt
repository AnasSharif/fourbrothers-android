package com.xdeveloperss.fourbrothers.ui.main.ui.expense

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xdeveloperss.fourbrothers.data.models.Expense
import com.xdeveloperss.fourbrothers.data.models.Person
import com.xdeveloperss.fourbrothers.ui.main.MainRepo
import com.xdeveloperss.fourbrothers.ui.main.MainViewModel
import kotlinx.coroutines.launch

class ExpenseViewModel(repository: MainRepo): MainViewModel(repository) {
    private val _selectedExpense: MutableLiveData<Expense> by lazy {
        MutableLiveData()
    }
    val selectedExpense: LiveData<Expense>
        get() = _selectedExpense

    fun setExpense(item: Expense) = viewModelScope.launch {
        _selectedExpense.value = item
    }
}