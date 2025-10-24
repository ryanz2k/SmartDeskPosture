package com.eldroid.smartdeskposture

import android.app.Application
import com.eldroid.smartdeskposture.data.UserDataManager
import com.google.firebase.FirebaseApp

class SmartDeskPostureApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        // Initialize UserDataManager
        UserDataManager.initialize(this)
    }
}
