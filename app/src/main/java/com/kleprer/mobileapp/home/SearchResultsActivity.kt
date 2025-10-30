package com.kleprer.mobileapp.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kleprer.mobileapp.R
import com.kleprer.mobileapp.booking.BookmarkActivity
import com.kleprer.mobileapp.booking.CarDetailsActivity
import com.kleprer.mobileapp.data.models.CarModel
import com.kleprer.mobileapp.databinding.ActivitySearchResultBinding

class SearchResultsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Навигация назад
        binding.ibtnBack.setOnClickListener {
            finish()
        }

        // Навигация по нижнему меню
        binding.ivHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        binding.ivBookmark.setOnClickListener {
            startActivity(Intent(this, BookmarkActivity::class.java))
            finish()
        }

        binding.ivSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }

        setupCarData()
    }

    private fun setupCarData() {
        // Берем первый автомобиль из списка (или можно сделать логику поиска)
        val car = generateCarList().first()

        // Заполняем карточку данными
        binding.tvCarName.text = car.model
        binding.tvCarBrand.text = car.brand
        binding.tvBookPrice.text = "${car.pricePerDay}р в день"
        binding.tvGearboxInfo.text = car.gearbox
        binding.tvFuelInfo.text = car.fuelType
        binding.carImage.setImageResource(car.imageRes)

        // Обработка кликов на кнопки
        binding.btnBook.setOnClickListener {
            openCarDetails(car)
        }

        binding.btnDetails.setOnClickListener {
            openCarDetails(car)
        }
    }

    private fun openCarDetails(car: CarModel) {
        val intent = Intent(this, CarDetailsActivity::class.java).apply {
            putExtra("CAR_DATA", car)
        }
        startActivity(intent)
    }

    private fun generateCarList(): List<CarModel> {
        return listOf(
            CarModel(1, "Mercedes-Benz", "S 500 Sedan", 2500, "A/T", "Бензин", R.drawable.ic_car_img),
            CarModel(2, "BMW", "X5", 2200, "A/T", "Дизель", R.drawable.ic_car_img),
            CarModel(3, "Audi", "A6", 2000, "A/T", "Бензин", R.drawable.ic_car_img),
            CarModel(4, "Toyota", "Camry", 1500, "A/T", "Бензин", R.drawable.ic_car_img),
            CarModel(5, "Honda", "Accord", 1400, "M/T", "Бензин", R.drawable.ic_car_img)
        )
    }
}