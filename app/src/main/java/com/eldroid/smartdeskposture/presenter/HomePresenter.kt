package com.eldroid.smartdeskposture.presenter

import com.eldroid.smartdeskposture.model.PostureData
import com.eldroid.smartdeskposture.model.User
import com.eldroid.smartdeskposture.view.HomeView
import com.eldroid.smartdeskposture.data.UserDataManager

interface HomePresenter {
    fun loadUserData()
    fun loadPostureData()
    fun getUserName(): String
    fun getUserEmail(): String
}

class HomePresenterImpl(private val view: HomeView) : HomePresenter {
    
    private var currentUser: User? = null
    private var postureDataList: List<PostureData> = emptyList()
    
    override fun loadUserData() {
        // Get user data from UserDataManager
        currentUser = UserDataManager.getCurrentUser()
        
        if (currentUser != null) {
            view.displayUserInfo(currentUser!!)
        } else {
            // Fallback to mock data if no user is logged in
            currentUser = User(
                id = "1",
                name = "Guest User",
                email = "guest@example.com"
            )
            view.displayUserInfo(currentUser!!)
        }
    }
    
    override fun loadPostureData() {
        // Mock posture data - in real app this would come from database or API
        postureDataList = listOf(
            PostureData(
                id = "1",
                userId = "1",
                timestamp = System.currentTimeMillis() - 3600000, // 1 hour ago
                postureType = "Good",
                duration = 45,
                reminderCount = 2
            ),
            PostureData(
                id = "2",
                userId = "1",
                timestamp = System.currentTimeMillis() - 7200000, // 2 hours ago
                postureType = "Fair",
                duration = 30,
                reminderCount = 1
            ),
            PostureData(
                id = "3",
                userId = "1",
                timestamp = System.currentTimeMillis() - 10800000, // 3 hours ago
                postureType = "Poor",
                duration = 20,
                reminderCount = 3
            )
        )
        view.displayPostureData(postureDataList)
    }
    
    override fun getUserName(): String {
        return currentUser?.name ?: "Guest User"
    }
    
    override fun getUserEmail(): String {
        return currentUser?.email ?: "guest@example.com"
    }
}
