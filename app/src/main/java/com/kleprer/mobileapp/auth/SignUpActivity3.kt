package com.kleprer.mobileapp.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.R
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.kleprer.mobileapp.AuthManager
import com.kleprer.mobileapp.databinding.ActivitySignUp3Binding
import kotlinx.coroutines.launch

class SignUpActivity3 : AppCompatActivity() {

    private lateinit var binding: ActivitySignUp3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUp3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUpNext3.setOnClickListener {
            if (validateInput()) completeRegistration()
        }

        binding.ibtnSignUpBack.setOnClickListener {
            finish() // Возврат на SignUpActivity2
        }

    }

    private fun validateInput(): Boolean {
        val licenseNumber = binding.etLicenseNumber.text.toString()
        val issueDate = binding.etIssueDate.text.toString()

        if (licenseNumber.isEmpty()) {
            binding.etLicenseNumber.error = "Введите номер удостоверения"
            return false
        }

        if (issueDate.isEmpty()) {
            binding.etIssueDate.error = "Введите дату выдачи"
            return false
        }

        return true
    }

    private fun completeRegistration() {
        lifecycleScope.launch {
            val result = performRegistration()

            result.fold(
                onSuccess = { userId ->
                    AuthManager.saveCurrentUserId(this@SignUpActivity3, userId)
                    navigateToCongratulations()
                },
                onFailure = { exception ->
                    showError("Ошибка регистрации: ${exception.message}")
                }
            )
        }
    }

    private suspend fun performRegistration(): Result<Long> {
        return AuthManager.registerUser(
            email = intent.getStringExtra("email") ?: "",
            password = intent.getStringExtra("password") ?: "",
            firstName = intent.getStringExtra("firstName") ?: "",
            lastName = intent.getStringExtra("lastName") ?: "",
            birthDate = intent.getStringExtra("birthDate") ?: "",
            gender = intent.getStringExtra("gender") ?: "",
            middleName = intent.getStringExtra("middleName"),
            driverLicense = findViewById<EditText>(R.id.et_license_number).text.toString(),
            licenseIssueDate = findViewById<EditText>(R.id.et_issue_date).text.toString()
        )
    }

    private fun navigateToCongratulations() {
        val intent = Intent(this, CongratulationsActivity::class.java)
        intent.putExtra("user_email", intent.getStringExtra("email"))
        startActivity(intent)
    }


    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}