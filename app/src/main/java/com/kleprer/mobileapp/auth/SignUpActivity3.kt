package com.kleprer.mobileapp.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.R
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.kleprer.mobileapp.AuthManager
import com.kleprer.mobileapp.data.db.AppDatabase
import com.kleprer.mobileapp.data.repo.UserRepo
import com.kleprer.mobileapp.databinding.ActivitySignUp3Binding
import com.kleprer.mobileapp.utils.ImagePicker
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import androidx.core.content.edit
import com.bumptech.glide.Glide

class SignUpActivity3 : AppCompatActivity() {

    private lateinit var binding: ActivitySignUp3Binding
    private lateinit var imagePicker: ImagePicker
    private var currentImageType: String = "" // "profile", "license", "passport"

    private lateinit var preferences: android.content.SharedPreferences

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val uri = imagePicker.getImageFromCameraResult(result.data)
            handleImageSelected(uri, currentImageType)
        } else {
            Toast.makeText(this, "Camera cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val uri = imagePicker.getImageFromGalleryResult(result.data)
            handleImageSelected(uri, currentImageType)
        } else {
            Toast.makeText(this, "Gallery cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(imagePicker.createCameraIntent())
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUp3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        imagePicker = ImagePicker(this)
        preferences = getSharedPreferences("temp_images", MODE_PRIVATE)
        loadSavedImages()

        binding.btnSignUpNext3.setOnClickListener {
            if (validateInput()) completeRegistration()
        }

        binding.ibtnSignUpBack.setOnClickListener {
            finish()
        }

        binding.ivUploadPfp.setOnClickListener {
            currentImageType = "profile"
            showImageSourceDialog()
        }

        binding.tvUploadDriverLicense.setOnClickListener {
            currentImageType = "license"
            showImageSourceDialog()
        }

        binding.tvUploadPassport.setOnClickListener {
            currentImageType = "passport"
            showImageSourceDialog()
        }


        val email = intent.getStringExtra("email") ?: ""
        println("DEBUG: SignUp3 onCreate - received email: $email")
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Camera", "Gallery")
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Choose Image Source")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> launchCamera()
                    1 -> launchGallery()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun launchCamera() {
        if (imagePicker.hasCameraPermission()) {
            cameraLauncher.launch(imagePicker.createCameraIntent())
        } else {
            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun launchGallery() {
        galleryLauncher.launch(imagePicker.createGalleryIntent())
    }

    private fun handleImageSelected(uri: Uri?, imageType: String) {
        if (uri == null) {
            Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val imagePath = saveImageToInternalStorage(uri, imageType)
                when (imageType) {
                    "profile" -> {
                        Glide.with(this@SignUpActivity3)
                            .load(imagePath)
                            .circleCrop()
                            .override(128, 128)
                            .into(binding.ivUploadPfp)
                        Toast.makeText(this@SignUpActivity3, "Profile photo selected", Toast.LENGTH_SHORT).show()
                    }
                    "license" -> {
                        binding.tvUploadDriverLicense.text = "✓ ${getString(R.string.upload_file)}"
                        Toast.makeText(this@SignUpActivity3, "License photo selected", Toast.LENGTH_SHORT).show()
                    }
                    "passport" -> {
                        binding.tvUploadPassport.text = "✓ ${getString(R.string.upload_file)}"
                        Toast.makeText(this@SignUpActivity3, "Passport photo selected", Toast.LENGTH_SHORT).show()
                    }
                }

                when (imageType) {
                    "profile" -> preferences.edit().putString("temp_profile_path", imagePath).apply()
                    "license" -> preferences.edit().putString("temp_license_path", imagePath).apply()
                    "passport" -> preferences.edit().putString("temp_passport_path", imagePath).apply()
                }

            } catch (e: Exception) {
                Toast.makeText(this@SignUpActivity3, "Error saving image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImageToInternalStorage(uri: Uri, imageType: String): String {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val timeStamp = System.currentTimeMillis()
            val fileName = "${imageType}_image_$timeStamp.jpg"
            val file = File(filesDir, fileName)

            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }

            file.absolutePath
        } catch (e: Exception) {
            throw Exception("Failed to save image: ${e.message}")
        }
    }

    private fun loadSavedImages() {
        val profilePath = preferences.getString("temp_profile_path", null)
        val licensePath = preferences.getString("temp_license_path", null)
        val passportPath = preferences.getString("temp_passport_path", null)

        profilePath?.let { path ->
            Glide.with(this)
                .load(path)
                .circleCrop()
                .override(128, 128)
                .into(binding.ivUploadPfp)
        }

        licensePath?.let { path ->
            binding.tvUploadDriverLicense.text = "✓ ${getString(R.string.upload_file)}"
        }

        passportPath?.let { path ->
            binding.tvUploadPassport.text = "✓ ${getString(R.string.upload_file)}"
        }
    }
    private fun validateInput(): Boolean {
        val licenseNumber = binding.etLicenseNumber.text.toString()
        val issueDate = binding.etIssueDate.text.toString()

        if (licenseNumber.isEmpty()) {
            binding.etLicenseNumber.error = getString(R.string.driver_license)
            return false
        }

        if (issueDate.isEmpty()) {
            binding.etIssueDate.error = getString(R.string.enter_date_of_issue)
            return false
        }

        return true
    }

    private fun completeRegistration() {
        lifecycleScope.launch {
            val result = performRegistration()
            result.fold(
                onSuccess = { userId ->
                    saveUserImages(userId)
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
        val email = intent.getStringExtra("email") ?: ""
        println("DEBUG: Registration email: $email")

        return AuthManager.registerUser(
            email = email,
            password = intent.getStringExtra("password") ?: "",
            firstName = intent.getStringExtra("firstName") ?: "",
            lastName = intent.getStringExtra("lastName") ?: "",
            birthDate = intent.getStringExtra("birthDate") ?: "",
            gender = intent.getStringExtra("gender") ?: "",
            middleName = intent.getStringExtra("middleName"),
            driverLicense = binding.etLicenseNumber.text.toString(),
            licenseIssueDate = binding.etIssueDate.text.toString()
        )
    }

    private suspend fun saveUserImages(userId: Long) {
        val profilePath = preferences.getString("temp_profile_path", null)
        val licensePath = preferences.getString("temp_license_path", null)
        val passportPath = preferences.getString("temp_passport_path", null)

        profilePath?.let { path ->
            AuthManager.updateProfileImage(userId, path)
        }

        licensePath?.let { path ->
            AuthManager.updateLicenseImage(userId, path)
        }

        passportPath?.let { path ->
            AuthManager.updatePassportImage(userId, path)
        }

        preferences.edit { clear() }
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