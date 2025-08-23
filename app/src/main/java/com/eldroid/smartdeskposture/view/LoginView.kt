package com.eldroid.smartdeskposture.view

import com.eldroid.smartdeskposture.model.User

interface LoginView {
    fun showLoading()
    fun hideLoading()
    fun showEmailError(message: String)
    fun showPasswordError(message: String)
    fun clearErrors()
    fun onLoginSuccess(user: User)
    fun onLoginError(message: String)
}
