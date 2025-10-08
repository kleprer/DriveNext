package com.kleprer.mobileapp.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.R
import android.widget.EditText
import android.widget.Toast
import com.kleprer.mobileapp.databinding.ActivitySignUp1Binding

class SignUpActivity1 : AppCompatActivity() {


    private lateinit var binding: ActivitySignUp1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUp1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUpNext.setOnClickListener {
            if (validateInput()) goToNextStep()
        }

        binding.ibtnSignUpBack.setOnClickListener {
            finish() // Возврат на предыдущий экран
        }
    }

    private fun validateInput(): Boolean {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        val agreeChecked = binding.cbAgree.isChecked

        if (email.isEmpty()) {
            binding.etEmail.error = "Введите email"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Введите корректный email"
            return false
        }

        if (password.length < 6) {
            binding.etPassword.error = "Пароль должен быть не менее 6 символов"
            return false
        }

        if (password != confirmPassword) {
            binding.etConfirmPassword.error = "Пароли не совпадают"
            return false
        }

        if (!agreeChecked) {
            Toast.makeText(this, "Необходимо согласие с условиями", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


    private fun goToNextStep() {
        val intent = Intent(this, SignUpActivity2::class.java)
        intent.putExtra("email", binding.etEmail.text.toString())
        intent.putExtra("password", binding.etPassword.text.toString())
        startActivity(intent)
    }
}