package com.kleprer.mobileapp.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.databinding.ActivityLoaderBinding
import com.kleprer.mobileapp.models.Car

class LoaderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoaderBinding
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchQuery = intent.getStringExtra("search_query") ?: ""

        // Имитация загрузки данных (3 секунды)
        handler.postDelayed({
            navigateToSearchResults(searchQuery)
        }, 3000)
    }

    private fun navigateToSearchResults(searchQuery: String) {
        val filteredCars = filterCarsLocally(searchQuery)
        val intent = Intent(this, SearchResultsActivity::class.java).apply {
            putExtra("search_query", searchQuery)
            putParcelableArrayListExtra("search_results", ArrayList(filteredCars))
        }
        startActivity(intent)
        finish()
    }

    private fun filterCarsLocally(query: String): List<Car> {
        // Локальная фильтрация автомобилей (такая же как в HomeActivity)
        val allCars = listOf(
            Car("1", "S 500 Sedan", "Mercedes-Benz", 2500, "A/T", "Бензин"),
            Car("2", "X5", "BMW", 3000, "A/T", "Дизель"),
            Car("3", "A6", "Audi", 2800, "A/T", "Бензин"),
            Car("4", "Camry", "Toyota", 2000, "A/T", "Бензин"),
            Car("5", "Civic", "Honda", 1500, "M/T", "Бензин"),
            Car("6", "Solaris", "Hyundai", 1200, "A/T", "Бензин"),
            Car("7", "Granta", "Lada", 800, "M/T", "Бензин"),
            Car("8", "Model 3", "Tesla", 3500, "A/T", "Электричество")
        )

        return if (query.isEmpty()) {
            allCars
        } else {
            allCars.filter { car ->
                car.brand.contains(query, ignoreCase = true) ||
                        car.name.contains(query, ignoreCase = true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}