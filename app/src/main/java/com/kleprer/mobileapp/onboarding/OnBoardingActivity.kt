package com.kleprer.mobileapp.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kleprer.mobileapp.main.MainActivity
import com.kleprer.mobileapp.R
import com.kleprer.mobileapp.databinding.ActivityOnBoardingBinding
import com.kleprer.mobileapp.home.HomeActivity

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding
    private var currentPage = 0
    private val totalPages = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.viewOnBoarding) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupClickListeners()
        updatePageContent()
    }

    private fun setupClickListeners() {
        binding.btnBoardingNext.setOnClickListener {
            handleNextButtonClick()
        }

        binding.tvSkip.setOnClickListener {
            navigateToHome()
        }
    }

    private fun handleNextButtonClick() {
        if (currentPage < totalPages - 1) {
            currentPage++
            animateSlideLeft()
            updatePageContent()
        } else {
            navigateToHome()
        }
    }

    private fun animateSlideLeft() {
        binding.ivOnBoardingImage.animate()
            .translationX(-binding.ivOnBoardingImage.width.toFloat())
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                binding.ivOnBoardingImage.translationX = binding.ivOnBoardingImage.width.toFloat()
                binding.ivOnBoardingImage.alpha = 0f

                // Update the image content
                updateOnBoardingImage()

                binding.ivOnBoardingImage.animate()
                    .translationX(0f)
                    .alpha(1f)
                    .setDuration(300)
                    .start()
            }
            .start()
    }

    private fun updatePageContent() {
        updateTitleText()
        updateDescriptionText()
        updateTabSelector()
        updateOnBoardingImage()
    }

    private fun updateTitleText() {
        val titleText = when (currentPage) {
            0 -> getString(R.string.boarding_title1)
            1 -> getString(R.string.boarding_title2)
            2 -> getString(R.string.boarding_title3)
            else -> ""
        }
        binding.textView3.text = titleText
    }

    private fun updateDescriptionText() {
        val descriptionText = when (currentPage) {
            0 -> getString(R.string.boarding_description1)
            1 -> getString(R.string.boarding_description2)
            2 -> getString(R.string.boarding_description3)
            else -> ""
        }
        binding.textViewDescription.text = descriptionText
    }

    private fun updateTabSelector() {
        val selectorResId = when (currentPage) {
            0 -> R.drawable.ic_tab_selector1
            1 -> R.drawable.ic_tab_selector2
            2 -> R.drawable.ic_tab_selector3
            else -> R.drawable.ic_tab_selector1
        }
        binding.tabSelectorImageView.setImageResource(selectorResId)
    }

    private fun updateOnBoardingImage() {
        val imageResId = when (currentPage) {
            0 -> R.drawable.ic_on_boarding1
            1 -> R.drawable.ic_on_boarding2
            2 -> R.drawable.ic_on_boarding3
            else -> R.drawable.ic_on_boarding1
        }
        binding.ivOnBoardingImage.setImageResource(imageResId)
    }


    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}