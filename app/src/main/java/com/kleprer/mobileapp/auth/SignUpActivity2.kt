package com.kleprer.mobileapp.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.R
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
            finish() // возврат на SignUpActivity1
        }
    }

    private fun validateInput(): Boolean {
        val lastName = binding.etLastName.text.toString().trim()
        val firstName = binding.etFirstName.text.toString().trim()
        val birthDate = binding.etBirthDate.text.toString().trim()
        val isGenderSelected = binding.rbMale.isChecked || binding.rbFemale.isChecked

        binding.etLastName.error = null
        binding.etFirstName.error = null
        binding.etBirthDate.error = null
        binding.tvGenderError.visibility = View.GONE

        if (lastName.isEmpty()) {
            binding.etLastName.error = getString(R.string.enter_last_name)
            return false
        }

        if (firstName.isEmpty()) {
            binding.etFirstName.error = getString(R.string.enter_first_name)
            return false
        }

        if (birthDate.isEmpty()) {
            binding.etBirthDate.error = getString(R.string.enter_birthdate)
            return false
        }

        if (!isValidDate(birthDate)) {
            binding.etBirthDate.error = getString(R.string.invalid_date_format)
            return false
        }

        if (!isGenderSelected) {
            binding.tvGenderError.text = getString(R.string.choose_gender)
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

    private fun goToNextStep() {
        val intent = Intent(this, SignUpActivity3::class.java)

        val emailFromPrevious = this.intent.getStringExtra("email") ?: ""
        val password = this.intent.getStringExtra("password") ?: ""
        println("DEBUG: SignUp2 -> SignUp3, email: $emailFromPrevious")

        intent.putExtra("email", emailFromPrevious)
        intent.putExtra("password", password)
        intent.putExtra("firstName", binding.etFirstName.text.toString())
        intent.putExtra("lastName", binding.etLastName.text.toString())
        intent.putExtra("middleName", binding.etMiddleName.text.toString())
        intent.putExtra("birthDate", binding.etBirthDate.text.toString())
        intent.putExtra("gender", if (binding.rbMale.isChecked) binding.rbMale.text else binding.rbFemale.text)

        startActivity(intent)
    }
}