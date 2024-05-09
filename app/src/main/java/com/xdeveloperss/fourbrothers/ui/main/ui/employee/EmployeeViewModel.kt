package com.xdeveloperss.fourbrothers.ui.main.ui.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xdeveloperss.fourbrothers.data.models.Employee
import com.xdeveloperss.fourbrothers.data.models.Person
import com.xdeveloperss.fourbrothers.ui.main.MainRepo
import com.xdeveloperss.fourbrothers.ui.main.MainViewModel
import kotlinx.coroutines.launch

class EmployeeViewModel(repository: MainRepo): MainViewModel(repository) {

    private val _selectedData: MutableLiveData<Employee> by lazy {
        MutableLiveData()
    }

    val employeeDetail: LiveData<Employee>
        get() = _selectedData

    fun setEmployee(person: Employee) = viewModelScope.launch {
        _selectedData.value = person
    }

}