package com.kleprer.mobileapp.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.kleprer.mobileapp.R
import com.kleprer.mobileapp.databinding.ActivityHomepageBinding
import com.kleprer.mobileapp.models.Car
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding
    private lateinit var carsAdapter: CarsAdapter
    private val allCars = mutableListOf<Car>()
    private var filteredCars = mutableListOf<Car>()

    private val searchHandler = Handler(Looper.getMainLooper())
    private val SEARCH_DELAY_MS = 2000L // 2 секунды задержки

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCarsData()
        setupViews()
        setupBottomNavigation()
        setupRecyclerView()
        showCarsList()
    }

    private fun setupCarsData() {
        allCars.clear()
        allCars.addAll(
            listOf(
                Car("1", "S 500 Sedan", "Mercedes-Benz", 2500, "A/T", "Бензин"),
                Car("2", "X5", "BMW", 3000, "A/T", "Дизель"),
                Car("3", "A6", "Audi", 2800, "A/T", "Бензин"),
                Car("4", "Camry", "Toyota", 2000, "A/T", "Бензин"),
                Car("5", "Civic", "Honda", 1500, "M/T", "Бензин"),
                Car("6", "Solaris", "Hyundai", 1200, "A/T", "Бензин"),
                Car("7", "Granta", "Lada", 800, "M/T", "Бензин"),
                Car("8", "Model 3", "Tesla", 3500, "A/T", "Электричество")
            )
        )
        filteredCars.addAll(allCars)
    }

    private fun setupViews() {
        setupSearchBar()
    }

    private fun setupSearchBar() {
        val searchEditText = binding.searchBar.findViewById<android.widget.EditText>(R.id.search_edit_text)

        searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val searchQuery = s.toString().trim()

                // Удаляем предыдущие отложенные задачи
                searchHandler.removeCallbacksAndMessages(null)

                if (searchQuery.length >= 2) {
                    // Запускаем новую отложенную задачу
                    searchHandler.postDelayed({
                        performSearch(searchQuery)
                    }, SEARCH_DELAY_MS)
                } else {
                    // Если поисковый запрос короткий, показываем все автомобили
                    filteredCars.clear()
                    filteredCars.addAll(allCars)
                    showCarsList()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        carsAdapter = CarsAdapter { car ->
            onCarItemClick(car)
        }

        binding.carsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = carsAdapter
        }
    }

    private fun showCarsList() {
        binding.loadingProgressBar.isVisible = false
        binding.carsRecyclerView.isVisible = true
        binding.errorLayout.isVisible = false
        binding.emptyStateLayout.isVisible = filteredCars.isEmpty()

        carsAdapter.submitList(filteredCars.toList())
    }

    private fun performSearch(searchQuery: String) {
        // Переходим на экран загрузки
        val intent = Intent(this, LoaderActivity::class.java).apply {
            putExtra("search_query", searchQuery)
        }
        startActivity(intent)
    }

    private fun onCarItemClick(car: Car) {
        val intent = Intent(this, CarDetailsActivity::class.java).apply {
            putExtra("car_id", car.id)
        }
        startActivity(intent)
    }

    private fun setupBottomNavigation() {
        binding.ivHome.setOnClickListener {
            // Прокрутка к началу списка при клике на домой
            binding.carsRecyclerView.smoothScrollToPosition(0)
        }

        binding.ivBookmark.setOnClickListener {
            startActivity(Intent(this, BookmarksActivity::class.java))
        }

        binding.ivSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchHandler.removeCallbacksAndMessages(null)
    }
}