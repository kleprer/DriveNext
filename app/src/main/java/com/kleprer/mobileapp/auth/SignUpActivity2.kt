package com.kleprer.mobileapp.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.R
import android.widget.Button
import android.widget.ImageButton
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.kleprer.mobileapp.auth.SignUpActivity3
class SignUpActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up2)

        findViewById<Button>(R.id.btn_sign_up_next).setOnClickListener {
            if (validateInput()) goToNextStep()
        }

        findViewById<Button>(R.id.ibtn_sign_up_back).setOnClickListener { finish() }
    }

    private fun validateInput(): Boolean {
        val lastName = findViewById<EditText>(R.id.et_last_name).text.toString()
        val firstName = findViewById<EditText>(R.id.et_first_name).text.toString()
        val middleName = findViewById<EditText>(R.id.et_middle_name).text.toString()
        val birthDate = findViewById<EditText>(R.id.et_birth_date).text.toString()

        if (lastName.isEmpty()) {
            findViewById<EditText>(R.id.et_last_name).error = "Введите фамилию"
            return false
        }

        if (firstName.isEmpty()) {
            findViewById<EditText>(R.id.et_first_name).error = "Введите имя"
            return false
        }

        if (birthDate.isEmpty()) {
            findViewById<EditText>(R.id.et_birth_date).error = "Введите дату рождения"
            return false
        }

        return true
    }

    private fun goToNextStep() {
        val bundle = Bundle().apply {
            putString("email", intent.getStringExtra("email"))
            putString("password", intent.getStringExtra("password"))
            putString("firstName", findViewById<EditText>(R.id.et_first_name).text.toString())
            putString("lastName", findViewById<EditText>(R.id.et_last_name).text.toString())
            putString("middleName", findViewById<EditText>(R.id.et_middle_name).text.toString())
            putString("birthDate", findViewById<EditText>(R.id.et_birth_date).text.toString())
            putString("gender", if (findViewById<RadioButton>(R.id.rb_male).isChecked) "Мужской" else "Женский")
        }

        val intent = Intent(this, SignUpActivity3::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}