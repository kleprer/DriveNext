package com.kleprer.mobileapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarModel(
    val id: Long,
    val brand: String,
    val model: String,
    val pricePerDay: Int,
    val gearbox: String,
    val fuelType: String,
    val imageRes: Int
) : Parcelable