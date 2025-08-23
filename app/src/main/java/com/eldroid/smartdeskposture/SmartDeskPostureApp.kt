package com.eldroid.smartdeskposture

import android.app.Application
import com.eldroid.smartdeskposture.data.UserDataManager

class SmartDeskPostureApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize UserDataManager
        UserDataManager.initialize(this)
    }
}
