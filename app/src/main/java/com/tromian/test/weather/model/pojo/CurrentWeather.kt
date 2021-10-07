package com.tromian.test.weather.model.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentWeather(
    /** ID города */
    val id: Int,
    /** Время расчета данных, unix, UTC */
    val unixTime: Long,
    /** Температура по восприятию */
    val feelsLike: Double,
    /** Влажность, % */
    val humidity: Int,
    /** Атмосферное давление , гПа*/
    val pressure: Int,
    /** Температура */
    val currentTemp: Double,
    /** Название города */
    val cityName: String,
    /** Сдвиг в секундах от UTC */
    val timezone: Int,
    /** Погодные условия */
    val weather: List<WeatherDetails>,
    /** Облачность,% */
    val clouds: Int,
    /** Направление ветра, градусы */
    val windDeg: Int,
    /** Скорость ветра метр/сек */
    val windSpeed: Double
) : Parcelable
