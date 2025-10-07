package com.kleprer.mobileapp.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.R
import android.widget.Button
import android.widget.ImageButton
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.kleprer.mobileapp.auth.SignUpActivity2

class SignUpActivity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up1)

        findViewById<Button>(R.id.btn_sign_up_next).setOnClickListener {
            if (validateInput()) goToNextStep()
        }

        findViewById<ImageButton>(R.id.ibtn_sign_up_back).setOnClickListener { finish() }
    }

    private fun validateInput(): Boolean {
        val email = this.findViewById<EditText>(R.id.et_email).text.toString()
        val password = this.findViewById<EditText>(R.id.et_password).text.toString()
        val confirmPassword = this.findViewById<EditText>(R.id.et_confirm_password).text.toString()
        val agreeChecked = findViewById<CheckBox>(R.id.cb_agree).isChecked

        if (email.isEmpty()) {
            findViewById<EditText>(R.id.et_email).error = "Введите email"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            findViewById<EditText>(R.id.et_email).error = "Введите корректный email"
            return false
        }

        if (password.length < 6) {
            findViewById<EditText>(R.id.et_password).error = "Пароль должен быть не менее 6 символов"
            return false
        }

        if (password != confirmPassword) {
            findViewById<EditText>(R.id.et_confirm_password).error = "Пароли не совпадают"
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
        intent.putExtra("email", findViewById<EditText>(R.id.et_email).text.toString())
        intent.putExtra("password", findViewById<EditText>(R.id.et_password).text.toString())
        startActivity(intent)
    }
}