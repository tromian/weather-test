package com.tromian.test.weather.model.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherDetails(
    val id: Int,
    val description: String,
    val icon: String,
    val name: String
) : Parcelable
