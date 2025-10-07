package com.kleprer.mobileapp.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.R
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class SignUpActivity3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up3)

        findViewById<Button>(R.id.btn_sign_up_next3).setOnClickListener {
            if (validateInput()) completeRegistration()
        }

        findViewById<Button>(R.id.ibtn_sign_up_back).setOnClickListener { finish() }
    }

    private fun validateInput(): Boolean {
        val licenseNumber = findViewById<EditText>(R.id.et_license_number).text.toString()
        val issueDate = findViewById<EditText>(R.id.et_issue_date).text.toString()

        if (licenseNumber.isEmpty()) {
            findViewById<EditText>(R.id.et_license_number).error = "Введите номер удостоверения"
            return false
        }

        if (issueDate.isEmpty()) {
            findViewById<EditText>(R.id.et_issue_date).error = "Введите дату выдачи"
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
        finish()
    }


    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}