package com.eldroid.smartdeskposture.presenter

import com.eldroid.smartdeskposture.model.User
import com.eldroid.smartdeskposture.view.RegisterView
import com.eldroid.smartdeskposture.data.UserDataManager
import com.eldroid.smartdeskposture.data.FirebaseAuthService
import com.eldroid.smartdeskposture.data.FirebaseDatabaseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface RegisterPresenter {
    fun register(name: String, email: String, password: String, confirmPassword: String)
    fun validateInput(name: String, email: String, password: String, confirmPassword: String): Boolean
}

class RegisterPresenterImpl(private val view: RegisterView) : RegisterPresenter {
    
    override fun register(name: String, email: String, password: String, confirmPassword: String) {
        if (validateInput(name, email, password, confirmPassword)) {
            view.showLoading()
            
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // First, create the user with Firebase Authentication
                    val authResult = FirebaseAuthService.createUserWithEmailAndPassword(email, password)
                    
                    authResult.fold(
                        onSuccess = { firebaseUser ->
                            // Update the user profile with display name
                            val profileResult = FirebaseAuthService.updateUserProfile(name)
                            
                            profileResult.fold(
                                onSuccess = {
                                    // Create user object for database
                                    val user = User(
                                        id = firebaseUser.uid,
                                        name = name,
                                        email = email
                                    )
                                    
                                    // Save user to Firebase Database
                                    val dbResult = FirebaseDatabaseService.saveUser(user)
                                    
                                    dbResult.fold(
                                        onSuccess = {
                                            // Save user data locally
                                            UserDataManager.saveUser(user)
                                            
                                            CoroutineScope(Dispatchers.Main).launch {
                                                view.onRegistrationSuccess(user)
                                                view.hideLoading()
                                            }
                                        },
                                        onFailure = { exception ->
                                            CoroutineScope(Dispatchers.Main).launch {
                                                view.onRegistrationError("Failed to save user data: ${exception.message}")
                                                view.hideLoading()
                                            }
                                        }
                                    )
                                },
                                onFailure = { exception ->
                                    CoroutineScope(Dispatchers.Main).launch {
                                        view.onRegistrationError("Failed to update profile: ${exception.message}")
                                        view.hideLoading()
                                    }
                                }
                            )
                        },
                        onFailure = { exception ->
                            CoroutineScope(Dispatchers.Main).launch {
                                view.onRegistrationError(FirebaseAuthService.getErrorMessage(exception as Exception))
                                view.hideLoading()
                            }
                        }
                    )
                } catch (e: Exception) {
                    CoroutineScope(Dispatchers.Main).launch {
                        view.onRegistrationError("An unexpected error occurred: ${e.message}")
                        view.hideLoading()
                    }
                }
            }
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
