package com.eldroid.smartdeskposture

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.eldroid.smartdeskposture.data.UserDataManager
import com.eldroid.smartdeskposture.view.MainActivity

class WelcomePageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)

        // Check if user is already logged in
        if (UserDataManager.isUserLoggedIn()) {
            // User is already logged in, redirect to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }

        // Initialize buttons
        val btnCreateAccount: Button = findViewById(R.id.btnCreateAccount)
        val btnSignIn: Button = findViewById(R.id.btnSignIn)

        // Navigate to RegisterActivity when "Create Account" is clicked
        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Navigate to LoginActivity when "Sign In" is clicked
        btnSignIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
