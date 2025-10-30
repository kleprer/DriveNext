package com.kleprer.mobileapp.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kleprer.mobileapp.databinding.ActivitySearchResultBinding
import com.kleprer.mobileapp.models.Car

class SearchResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var carsAdapter: CarsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchQuery = intent.getStringExtra("search_query") ?: ""
        val searchResults = intent.getParcelableArrayListExtra<Car>("search_results") ?: emptyList()

        setupViews(searchQuery, searchResults)
        setupBottomNavigation()
    }

    private fun setupViews(searchQuery: String, searchResults: List<Car>) {
        // Установка заголовка
        binding.tvSearchResults.text = "Результаты поиска \"$searchQuery\""

        // Настройка RecyclerView
        carsAdapter = CarsAdapter { car ->
            onCarItemClick(car)
        }

        binding.carsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchResultsActivity)
            adapter = carsAdapter
        }

        // Отображение результатов
        carsAdapter.submitList(searchResults)

        // Кнопка назад
        binding.ibtnBack.setOnClickListener {
            finish()
        }
    }

    private fun onCarItemClick(car: Car) {
        val intent = Intent(this, CarDetailsActivity::class.java).apply {
            putExtra("car_id", car.id)
        }
        startActivity(intent)
    }

    private fun setupBottomNavigation() {
        binding.ivHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.ivBookmark.setOnClickListener {
            startActivity(Intent(this, BookmarksActivity::class.java))
        }

        binding.ivSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}