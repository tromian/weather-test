package com.tromian.test.weather.model.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentCity(
    val cityName: String,
    val lat: Double,
    val lon: Double
) : Parcelable