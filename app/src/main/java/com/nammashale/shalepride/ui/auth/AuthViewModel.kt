package com.nammashale.shalepride.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nammashale.shalepride.data.model.User
import com.nammashale.shalepride.data.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> = _loginResult

    private val _signupResult = MutableLiveData<Result<User>>()
    val signupResult: LiveData<Result<User>> = _signupResult

    private val _forgotPasswordResult = MutableLiveData<Result<String>>()
    val forgotPasswordResult: LiveData<Result<String>> = _forgotPasswordResult

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String, role: String) {
        _isLoading.value = true
        val result = authRepository.login(email, password, role)
        _loginResult.value = result
        _isLoading.value = false
    }

    fun signup(name: String, email: String, password: String, role: String) {
        _isLoading.value = true
        val result = authRepository.signup(name, email, password, role)
        _signupResult.value = result
        _isLoading.value = false
    }

    fun forgotPassword(email: String) {
        _isLoading.value = true
        val result = authRepository.forgotPassword(email)
        _forgotPasswordResult.value = result
        _isLoading.value = false
    }
}
