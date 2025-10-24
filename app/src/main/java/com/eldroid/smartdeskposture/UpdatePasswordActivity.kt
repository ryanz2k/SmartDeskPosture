package com.eldroid.smartdeskposture

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class UpdatePasswordActivity : Activity() {

    private lateinit var currentPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var updatePasswordButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_update_password)

        currentPasswordEditText = findViewById(R.id.currentPasswordEditText)
        newPasswordEditText = findViewById(R.id.newPasswordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        updatePasswordButton = findViewById(R.id.updatePasswordButton)
        cancelButton = findViewById(R.id.cancelButton)

        updatePasswordButton.setOnClickListener {
            updatePassword()
        }

        cancelButton.setOnClickListener {
            finish() // Close activity
        }
    }

    private fun updatePassword() {
        val currentPassword = currentPasswordEditText.text.toString()
        val newPassword = newPasswordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        // Assuming password is stored in SharedPreferences
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val storedPassword = sharedPref.getString("password", "")

        if (currentPassword != storedPassword) {
            Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword != confirmPassword) {
            Toast.makeText(this, "New password and confirmation do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        // Save new password
        with(sharedPref.edit()) {
            putString("password", newPassword)
            apply()
        }

        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
        finish() // Close activity after updating
    }
}
