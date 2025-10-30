package com.kleprer.mobileapp.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kleprer.mobileapp.R
import com.kleprer.mobileapp.databinding.ItemCarBinding
import com.kleprer.mobileapp.models.Car

class CarsAdapter(
    private val onItemClick: (Car) -> Unit
) : ListAdapter<Car, CarsAdapter.CarViewHolder>(CarDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding = ItemCarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = getItem(position)
        holder.bind(car)
    }

    inner class CarViewHolder(
        private val binding: ItemCarBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(adapterPosition))
                }
            }

            binding.btnBook.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val car = getItem(adapterPosition)
                    // Навигация к бронированию
                    val intent = Intent(binding.root.context, BookingDetailsActivity::class.java).apply {
                        putExtra("car_id", car.id)
                        putExtra("car_name", car.name)
                        putExtra("car_brand", car.brand)
                        putExtra("car_price", car.pricePerDay)
                    }
                    binding.root.context.startActivity(intent)
                }
            }

            binding.btnDetails.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val car = getItem(adapterPosition)
                    // Навигация к деталям
                    val intent = Intent(binding.root.context, CarDetailsActivity::class.java).apply {
                        putExtra("car_id", car.id)
                    }
                    binding.root.context.startActivity(intent)
                }
            }
        }

        fun bind(car: Car) {
            binding.tvCarName.text = car.name
            binding.tvCarBrand.text = car.brand
            binding.tvBookPrice.text = binding.root.context.getString(
                R.string.price_per_day_format,
                car.pricePerDay
            )
            binding.tvGearboxInfo.text = car.gearbox
            binding.tvFuelInfo.text = car.fuelType
        }
    }
}

class CarDiffCallback : DiffUtil.ItemCallback<Car>() {
    override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {
        return oldItem == newItem
    }
}