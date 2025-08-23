package com.eldroid.smartdeskposture.view

import com.eldroid.smartdeskposture.model.User

interface RegisterView {
    fun showLoading()
    fun hideLoading()
    fun showNameError(message: String)
    fun showEmailError(message: String)
    fun showPasswordError(message: String)
    fun showConfirmPasswordError(message: String)
    fun clearErrors()
    fun onRegistrationSuccess(user: User)
    fun onRegistrationError(message: String)
}
