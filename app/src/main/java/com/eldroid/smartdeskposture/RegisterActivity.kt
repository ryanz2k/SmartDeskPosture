package com.eldroid.smartdeskposture

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eldroid.smartdeskposture.model.User
import com.eldroid.smartdeskposture.presenter.RegisterPresenter
import com.eldroid.smartdeskposture.presenter.RegisterPresenterImpl
import com.eldroid.smartdeskposture.view.RegisterView
import com.eldroid.smartdeskposture.data.UserDataManager
import com.eldroid.smartdeskposture.view.MainActivity

class RegisterActivity : AppCompatActivity(), RegisterView {

    private lateinit var presenter: RegisterPresenter

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        presenter = RegisterPresenterImpl(this)
        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        registerButton = findViewById(R.id.registerButton)
        loginLink = findViewById(R.id.loginLink)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupListeners() {
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()
            presenter.register(name, email, password, confirmPassword)
        }

        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        registerButton.isEnabled = false
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
        registerButton.isEnabled = true
    }

    override fun showNameError(message: String) {
        nameEditText.error = message
    }

    override fun showEmailError(message: String) {
        emailEditText.error = message
    }

    override fun showPasswordError(message: String) {
        passwordEditText.error = message
    }

    override fun showConfirmPasswordError(message: String) {
        confirmPasswordEditText.error = message
    }

    override fun clearErrors() {
        nameEditText.error = null
        emailEditText.error = null
        passwordEditText.error = null
        confirmPasswordEditText.error = null
    }

    override fun onRegistrationSuccess(user: User) {
        Toast.makeText(this, "Registration successful! Welcome ${user.name}", Toast.LENGTH_SHORT).show()
        // Navigate to MainActivity (which displays HomeFragment by default)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onRegistrationError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
