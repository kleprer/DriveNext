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
import com.kleprer.mobileapp.databinding.ActivitySignUp2Binding

class SignUpActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivitySignUp2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUp2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUpNext.setOnClickListener {
            if (validateInput()) goToNextStep()
        }

        binding.ibtnSignUpBack.setOnClickListener {
            finish() // Возврат на SignUpActivity1
        }
    }

    private fun validateInput(): Boolean {
        val lastName = binding.etLastName.text.toString()
        val firstName = binding.etFirstName.text.toString()
        val birthDate = binding.etBirthDate.text.toString()

        if (lastName.isEmpty()) {
            binding.etLastName.error = "Введите фамилию"
            return false
        }

        if (firstName.isEmpty()) {
            binding.etFirstName.error = "Введите имя"
            return false
        }

        if (birthDate.isEmpty()) {
            binding.etBirthDate.error = "Введите дату рождения"
            return false
        }

        return true
    }

    private fun goToNextStep() {val intent = Intent(this, SignUpActivity3::class.java)

        // Передаем данные из предыдущего экрана + текущие
        intent.putExtra("email", intent.getStringExtra("email") ?: "")
        intent.putExtra("password", intent.getStringExtra("password") ?: "")
        intent.putExtra("firstName", binding.etFirstName.text.toString())
        intent.putExtra("lastName", binding.etLastName.text.toString())
        intent.putExtra("middleName", binding.etMiddleName.text.toString())
        intent.putExtra("birthDate", binding.etBirthDate.text.toString())
        intent.putExtra("gender", if (binding.rbMale.isChecked) "Мужской" else "Женский")

        startActivity(intent)
    }
}