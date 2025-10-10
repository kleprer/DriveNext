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
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        val agreeChecked = binding.cbAgree.isChecked

        // Clear previous errors
        binding.etEmail.error = null
        binding.etPassword.error = null
        binding.etConfirmPassword.error = null

        if (email.isEmpty()) {
            binding.etEmail.error = getString(R.string.enter_email)
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = getString(R.string.enter_valid_email)
            return false
        }

        if (password.length < 6) {
            binding.etPassword.error = getString(R.string.password_at_least_6)
            return false
        }

        if (confirmPassword != password) {
            binding.etConfirmPassword.error = getString(R.string.passwords_dont_match)
            return false
        }

        if (!agreeChecked) {
            Toast.makeText(this, getString(R.string.agreement_required), Toast.LENGTH_SHORT).show()
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