package com.eldroid.smartdeskposture.view

import com.eldroid.smartdeskposture.model.PostureData
import com.eldroid.smartdeskposture.model.User

interface HomeView {
    fun displayUserInfo(user: User)
    fun displayPostureData(postureDataList: List<PostureData>)
    fun showLoading()
    fun hideLoading()
    fun showError(message: String)
}
