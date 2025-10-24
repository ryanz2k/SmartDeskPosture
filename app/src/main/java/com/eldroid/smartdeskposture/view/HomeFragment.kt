package com.eldroid.smartdeskposture.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.eldroid.smartdeskposture.R
import com.eldroid.smartdeskposture.data.UserDataManager

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val welcomeTextView = view.findViewById<TextView>(R.id.welcomeTextView)

        val currentUser = UserDataManager.getCurrentUser()
        val userName = currentUser?.name ?: "Guest"

        // ✅ Display the user's name in the welcome text
        welcomeTextView.text = "Welcome, $userName!"

        return view
    }
}
