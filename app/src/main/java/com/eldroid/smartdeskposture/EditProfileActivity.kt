package com.eldroid.smartdeskposture

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eldroid.smartdeskposture.data.UserDataManager
import com.eldroid.smartdeskposture.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_edit_profile)

        // Initialize UI components
        fullNameEditText = findViewById(R.id.fullNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        updateButton = findViewById(R.id.updateButton)
        cancelButton = findViewById(R.id.cancelButton)

        // ✅ Step 1: Load current user data
        val currentUser = UserDataManager.getCurrentUser()

        // ✅ Step 2: Pre-fill user details if available
        if (currentUser != null) {
            fullNameEditText.setText(currentUser.name)
            emailEditText.setText(currentUser.email)
        } else {
            Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show()
        }

        // ✅ Step 3: Update button functionality
        updateButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Step 4: Save updated data to UserDataManager
            val updatedUser = User(
                id = currentUser?.id ?: "1",
                name = fullName,
                email = email
            )
            
            CoroutineScope(Dispatchers.IO).launch {
                UserDataManager.saveCurrentUser(updatedUser)
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(this@EditProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    finish() // Go back to SettingsFragment
                }
            }
        }

        // ✅ Step 5: Cancel button — just go back
        cancelButton.setOnClickListener {
            finish()
        }
    }
}
