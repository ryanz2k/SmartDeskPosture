package com.eldroid.smartdeskposture.presenter

import com.eldroid.smartdeskposture.model.User
import com.eldroid.smartdeskposture.view.RegisterView
import com.eldroid.smartdeskposture.data.UserDataManager

interface RegisterPresenter {
    fun register(name: String, email: String, password: String, confirmPassword: String)
    fun validateInput(name: String, email: String, password: String, confirmPassword: String): Boolean
}

class RegisterPresenterImpl(private val view: RegisterView) : RegisterPresenter {
    
    override fun register(name: String, email: String, password: String, confirmPassword: String) {
        if (validateInput(name, email, password, confirmPassword)) {
            view.showLoading()
            
            // Simulate registration process
            val user = User(
                id = System.currentTimeMillis().toString(),
                name = name,
                email = email,
                password = password
            )
            
            // Save user data temporarily
            UserDataManager.saveUser(user)
            
            // Mock successful registration
            view.onRegistrationSuccess(user)
            view.hideLoading()
        }
    }
    
    override fun validateInput(name: String, email: String, password: String, confirmPassword: String): Boolean {
        if (name.isEmpty()) {
            view.showNameError("Name is required")
            return false
        }
        
        if (name.length < 2) {
            view.showNameError("Name must be at least 2 characters")
            return false
        }
        
        if (email.isEmpty()) {
            view.showEmailError("Email is required")
            return false
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showEmailError("Please enter a valid email")
            return false
        }
        
        if (password.isEmpty()) {
            view.showPasswordError("Password is required")
            return false
        }
        
        if (password.length < 6) {
            view.showPasswordError("Password must be at least 6 characters")
            return false
        }
        
        if (confirmPassword.isEmpty()) {
            view.showConfirmPasswordError("Please confirm your password")
            return false
        }
        
        if (password != confirmPassword) {
            view.showConfirmPasswordError("Passwords do not match")
            return false
        }
        
        return true
    }
}
