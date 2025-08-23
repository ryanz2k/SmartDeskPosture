package com.eldroid.smartdeskposture.presenter

import com.eldroid.smartdeskposture.model.User
import com.eldroid.smartdeskposture.view.LoginView
import com.eldroid.smartdeskposture.data.UserDataManager

interface LoginPresenter {
    fun login(email: String, password: String)
    fun validateInput(email: String, password: String): Boolean
}

class LoginPresenterImpl(private val view: LoginView) : LoginPresenter {
    
    override fun login(email: String, password: String) {
        if (validateInput(email, password)) {
            // Simulate login process
            view.showLoading()
            
            // Mock login validation
            if (email == "test@gmail.com" && password == "123123") {
                val user = User(
                    id = "1",
                    name = "Test User",
                    email = email,
                    password = password
                )
                
                // Save user data temporarily
                UserDataManager.saveUser(user)
                
                view.onLoginSuccess(user)
            } else {
                view.onLoginError("Invalid email or password")
            }
            
            view.hideLoading()
        }
    }
    
    override fun validateInput(email: String, password: String): Boolean {
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
        
        return true
    }
}
