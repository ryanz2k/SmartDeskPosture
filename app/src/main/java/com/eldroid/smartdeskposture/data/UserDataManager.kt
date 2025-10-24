package com.eldroid.smartdeskposture.data

import android.content.Context
import android.content.SharedPreferences
import com.eldroid.smartdeskposture.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object UserDataManager {
    
    private const val PREF_NAME = "user_data"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    
    private var currentUser: User? = null
    private lateinit var sharedPreferences: SharedPreferences
    
    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        loadUserFromPreferences()
    }
    
    fun saveUser(user: User) {
        currentUser = user
        saveUserToPreferences(user)
        
        // Save to Firebase Database
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseDatabaseService.saveUser(user)
        }
    }
    
    fun getCurrentUser(): User? {
        return currentUser
    }
    
    fun isUserLoggedIn(): Boolean {
        return FirebaseAuthService.isUserLoggedIn() && currentUser != null
    }
    
    fun clearUser() {
        currentUser = null
        clearUserFromPreferences()
        FirebaseAuthService.signOut()
    }
    
    private fun saveUserToPreferences(user: User) {
        sharedPreferences.edit().apply {
            putString(KEY_USER_ID, user.id)
            putString(KEY_USER_NAME, user.name)
            putString(KEY_USER_EMAIL, user.email)
            apply()
        }
    }
    
    private fun loadUserFromPreferences() {
        val userId = sharedPreferences.getString(KEY_USER_ID, null)
        val userName = sharedPreferences.getString(KEY_USER_NAME, null)
        val userEmail = sharedPreferences.getString(KEY_USER_EMAIL, null)
        
        if (userId != null && userName != null && userEmail != null) {
            currentUser = User(
                id = userId,
                name = userName,
                email = userEmail
            )
        }
    }
    
    private fun clearUserFromPreferences() {
        sharedPreferences.edit().clear().apply()
    }

    suspend fun saveCurrentUser(updatedUser: User) {
        currentUser = updatedUser
        saveUserToPreferences(updatedUser)
        FirebaseDatabaseService.updateUser(updatedUser)
    }
}
