package com.kleprer.mobileapp.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.R
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.kleprer.mobileapp.AuthManager
import com.kleprer.mobileapp.databinding.ActivityLoginBinding
import com.kleprer.mobileapp.home.HomeActivity
import com.kleprer.mobileapp.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_login)
        setContentView(binding.root)
        setupViews()
    }

    private fun setupViews() {
        binding.btnLogin.setOnClickListener {
            if (validateInput()) performLogin()
        }

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity1::class.java))
        }

        // google вход
        binding.btnLoginGoogle.setOnClickListener {
            Toast.makeText(this, getString(R.string.later), Toast.LENGTH_SHORT).show()
        }

        // забыли пароль
        binding.btnForgotPassword.setOnClickListener {
            Toast.makeText(this, getString(R.string.later), Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(): Boolean {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        binding.etEmail.error = null
        binding.etPassword.error = null

        if (email.isEmpty()) {
            binding.etEmail.error = getString(R.string.enter_email)
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = getString(R.string.enter_valid_email)
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = getString(R.string.enter_password)
            return false
        }

        return true
    }


    private fun performLogin() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        lifecycleScope.launch {
            val result = AuthManager.loginUser(email, password)
            result.fold(
                onSuccess = { user ->
                    AuthManager.saveCurrentUserId(this@LoginActivity, user.id)
                    navigateToHomepage()
                },
                onFailure = { exception ->
                    showError(getString(R.string.login_error, exception.message))
                }
            )
        }
    }

    private fun navigateToHomepage() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish() // Finish login activity so user can't go back
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}