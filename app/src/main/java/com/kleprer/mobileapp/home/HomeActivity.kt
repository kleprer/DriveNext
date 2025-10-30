package com.kleprer.mobileapp.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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
        // Локальные данные об автомобилях
        allCars.clear()
        allCars.addAll(
            listOf(
                Car(
                    id = "1",
                    name = "S 500 Sedan",
                    brand = "Mercedes-Benz",
                    pricePerDay = 2500,
                    gearbox = "A/T",
                    fuelType = "Бензин"
                ),
                Car(
                    id = "2",
                    name = "X5",
                    brand = "BMW",
                    pricePerDay = 3000,
                    gearbox = "A/T",
                    fuelType = "Дизель"
                ),
                Car(
                    id = "3",
                    name = "A6",
                    brand = "Audi",
                    pricePerDay = 2800,
                    gearbox = "A/T",
                    fuelType = "Бензин"
                ),
                Car(
                    id = "4",
                    name = "Camry",
                    brand = "Toyota",
                    pricePerDay = 2000,
                    gearbox = "A/T",
                    fuelType = "Бензин"
                ),
                Car(
                    id = "5",
                    name = "Civic",
                    brand = "Honda",
                    pricePerDay = 1500,
                    gearbox = "M/T",
                    fuelType = "Бензин"
                ),
                Car(
                    id = "6",
                    name = "Solaris",
                    brand = "Hyundai",
                    pricePerDay = 1200,
                    gearbox = "A/T",
                    fuelType = "Бензин"
                ),
                Car(
                    id = "7",
                    name = "Granta",
                    brand = "Lada",
                    pricePerDay = 800,
                    gearbox = "M/T",
                    fuelType = "Бензин"
                ),
                Car(
                    id = "8",
                    name = "Model 3",
                    brand = "Tesla",
                    pricePerDay = 3500,
                    gearbox = "A/T",
                    fuelType = "Электричество"
                )
            )
        )
        filteredCars.addAll(allCars)
    }

    private fun setupViews() {
        setupSearchBar()
    }

    private fun setupSearchBar() {
        val searchEditText = binding.searchBar.findViewById<android.widget.EditText>(R.id.search_edit_text)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val searchQuery = s.toString().trim()
                filterCars(searchQuery)
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

    private fun filterCars(searchQuery: String) {
        filteredCars.clear()

        if (searchQuery.isEmpty()) {
            filteredCars.addAll(allCars)
        } else {
            val query = searchQuery.lowercase(Locale.getDefault())
            filteredCars.addAll(allCars.filter { car ->
                car.brand.lowercase(Locale.getDefault()).contains(query) ||
                        car.name.lowercase(Locale.getDefault()).contains(query)
            })
        }

        showCarsList()

        // Показываем Toast если поиск не дал результатов
        if (searchQuery.isNotEmpty() && filteredCars.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.no_cars_found, searchQuery),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onCarItemClick(car: Car) {
        // Можно открыть детали автомобиля при клике на карточку
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
}