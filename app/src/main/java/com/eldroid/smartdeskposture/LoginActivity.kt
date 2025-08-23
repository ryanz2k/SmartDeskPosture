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
import com.eldroid.smartdeskposture.presenter.LoginPresenter
import com.eldroid.smartdeskposture.presenter.LoginPresenterImpl
import com.eldroid.smartdeskposture.view.LoginView
import com.eldroid.smartdeskposture.data.UserDataManager

class LoginActivity : AppCompatActivity(), LoginView {
    
    private lateinit var presenter: LoginPresenter
    
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerLink: TextView
    private lateinit var progressBar: ProgressBar
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        presenter = LoginPresenterImpl(this)
        initializeViews()
        setupListeners()
    }
    
    private fun initializeViews() {
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerLink = findViewById(R.id.registerLink)
        progressBar = findViewById(R.id.progressBar)
    }
    
    private fun setupListeners() {
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            presenter.login(email, password)
        }
        
        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    
    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        loginButton.isEnabled = false
    }
    
    override fun hideLoading() {
        progressBar.visibility = View.GONE
        loginButton.isEnabled = true
    }
    
    override fun showEmailError(message: String) {
        emailEditText.error = message
    }
    
    override fun showPasswordError(message: String) {
        passwordEditText.error = message
    }
    
    override fun clearErrors() {
        emailEditText.error = null
        passwordEditText.error = null
    }
    
    override fun onLoginSuccess(user: User) {
        Toast.makeText(this, "Login successful! Welcome ${user.name}", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
    
    override fun onLoginError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
