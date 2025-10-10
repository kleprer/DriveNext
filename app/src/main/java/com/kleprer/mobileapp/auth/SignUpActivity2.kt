package com.kleprer.mobileapp.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
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
        val lastName = binding.etLastName.text.toString().trim()
        val firstName = binding.etFirstName.text.toString().trim()
        val birthDate = binding.etBirthDate.text.toString().trim()
        val isGenderSelected = binding.rbMale.isChecked || binding.rbFemale.isChecked

        // Clear previous errors
        binding.etLastName.error = null
        binding.etFirstName.error = null
        binding.etBirthDate.error = null
        binding.tvGenderError.visibility = View.GONE

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

        // Validate date format (DD.MM.YYYY)
        if (!isValidDate(birthDate)) {
            binding.etBirthDate.error = "Неверный формат даты (ДД.ММ.ГГГГ)"
            return false
        }

        if (!isGenderSelected) {
            binding.tvGenderError.text = "Выберите пол"
            binding.tvGenderError.visibility = View.VISIBLE
            return false
        }

        return true
    }

    private fun isValidDate(date: String): Boolean {
        return try {
            val pattern = Regex("""^(0[1-9]|[12][0-9]|3[01])\.(0[1-9]|1[0-2])\.\d{4}$""")
            pattern.matches(date)
        } catch (e: Exception) {
            false
        }
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