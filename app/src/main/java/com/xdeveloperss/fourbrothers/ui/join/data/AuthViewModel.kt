package com.xdeveloperss.fourbrothers.ui.join.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xdeveloperss.fourbrothers.xnetwork.config.models.AuthResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.response.XNetworkResponse
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository): ViewModel() {

    private val _apiLogin: MutableLiveData<XNetworkResponse<AuthResponse>> by lazy {
        MutableLiveData()
    }

    val apiLogin: LiveData<XNetworkResponse<AuthResponse>>
        get() = _apiLogin

    fun login(email: String, password: String) = viewModelScope.launch {
        _apiLogin.value = repository.login(email, password)
    }

    private val _apiLogout: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    val apiLogout: LiveData<Boolean>
        get() = _apiLogout

    fun logout() = viewModelScope.launch {
        _apiLogout.value = repository.logout()
    }
}