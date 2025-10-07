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
import com.kleprer.mobileapp.main.MainActivity
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private companion object { const val RC_GOOGLE_SIGN_IN = 1001 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupViews()
    }

    private fun setupViews() {
        // обычный вход
        findViewById<Button>(R.id.btn_login).setOnClickListener {
            if (validateInput()) performLogin()
        }

        // регистрация
        findViewById<Button>(R.id.btn_sign_up).setOnClickListener {
            startActivity(Intent(this, SignUpActivity1::class.java))
        }
    }

    private fun validateInput(): Boolean {
        val email = this.findViewById<EditText>(R.id.et_email).text.toString()
        val password = this.findViewById<EditText>(R.id.et_password).text.toString()

        if (email.isEmpty()) {
            findViewById<EditText>(R.id.et_email).error = "Введите email"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            findViewById<EditText>(R.id.et_email).error = "Введите корректный email"
            return false
        }

        if (password.isEmpty()) {
            findViewById<EditText>(R.id.et_password).error = "Введите пароль"
            return false
        }

        return true
    }

    private fun performLogin() {
        val email = findViewById<EditText>(R.id.et_email).text.toString()
        val password = findViewById<EditText>(R.id.et_password).text.toString()

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
        finish()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}