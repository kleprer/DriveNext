package com.kleprer.mobileapp.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kleprer.mobileapp.R
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.kleprer.mobileapp.AuthManager
import com.kleprer.mobileapp.databinding.ActivityLoginBinding
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
        // Используем binding вместо findViewById
        binding.btnLogin.setOnClickListener {
            if (validateInput()) performLogin()
        }

        // Добавляем обработчик для текста "Sign Up"
        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity1::class.java))
        }

        // Google вход
        binding.btnLoginGoogle.setOnClickListener {
            Toast.makeText(this, "Google login will be implemented", Toast.LENGTH_SHORT).show()
        }

        // Забыли пароль
        binding.btnForgotPassword.setOnClickListener {
            Toast.makeText(this, "Password recovery will be implemented", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(): Boolean {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty()) {
            binding.etEmail.error = "Введите email"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Введите корректный email"
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Введите пароль"
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
                    navigateToMain()
                },
                onFailure = { exception ->
                    showError("Ошибка входа: ${exception.message}")
                }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}