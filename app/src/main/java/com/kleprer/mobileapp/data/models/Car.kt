package com.kleprer.mobileapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Car(
    val id: String,
    val name: String,
    val brand: String,
    val pricePerDay: Int,
    val gearbox: String,
    val fuelType: String,
    val imageUrl: String? = null,
    val features: List<String> = emptyList()
) : Parcelable