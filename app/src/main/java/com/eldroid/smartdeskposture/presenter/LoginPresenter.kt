package com.eldroid.smartdeskposture.presenter

import com.eldroid.smartdeskposture.model.User
import com.eldroid.smartdeskposture.view.LoginView
import com.eldroid.smartdeskposture.data.UserDataManager
import com.eldroid.smartdeskposture.data.FirebaseAuthService
import com.eldroid.smartdeskposture.data.FirebaseDatabaseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface LoginPresenter {
    fun login(email: String, password: String)
    fun validateInput(email: String, password: String): Boolean
}

class LoginPresenterImpl(private val view: LoginView) : LoginPresenter {
    
    override fun login(email: String, password: String) {
        if (validateInput(email, password)) {
            view.showLoading()
            
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val authResult = FirebaseAuthService.signInWithEmailAndPassword(email, password)
                    
                    authResult.fold(
                        onSuccess = { firebaseUser ->
                            // Get user data from database using UID instead of email
                            val userResult = FirebaseDatabaseService.getUserById(firebaseUser.uid)
                            
                            userResult.fold(
                                onSuccess = { user ->
                                    if (user != null) {
                                        // Save user data locally
                                        UserDataManager.saveUser(user)
                                        
                                        CoroutineScope(Dispatchers.Main).launch {
                                            view.onLoginSuccess(user)
                                            view.hideLoading()
                                        }
                                    } else {
                                        // Create user if not found in database
                                        val newUser = User(
                                            id = firebaseUser.uid,
                                            name = firebaseUser.displayName ?: "User",
                                            email = firebaseUser.email ?: email
                                        )
                                        
                                        // Save to database and locally
                                        FirebaseDatabaseService.saveUser(newUser)
                                        UserDataManager.saveUser(newUser)
                                        
                                        CoroutineScope(Dispatchers.Main).launch {
                                            view.onLoginSuccess(newUser)
                                            view.hideLoading()
                                        }
                                    }
                                },
                                onFailure = { exception ->
                                    CoroutineScope(Dispatchers.Main).launch {
                                        view.onLoginError("Failed to load user data: ${exception.message}")
                                        view.hideLoading()
                                    }
                                }
                            )
                        },
                        onFailure = { exception ->
                            CoroutineScope(Dispatchers.Main).launch {
                                view.onLoginError(FirebaseAuthService.getErrorMessage(exception as Exception))
                                view.hideLoading()
                            }
                        }
                    )
                } catch (e: Exception) {
                    CoroutineScope(Dispatchers.Main).launch {
                        view.onLoginError("An unexpected error occurred: ${e.message}")
                        view.hideLoading()
                    }
                }
            }
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
