package com.tromian.test.weather.model.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyWeather(
    /** Время прогнозируемых данных, Unix, UTC */
    val unixTime: Long,
    /** Облачность,% */
    val clouds: Int,
    /** Влажность, % */
    val humidity: Int,
    /** Вероятность выпадения осадков */
    val pop: Double,
    /** Атмосферное давление на уровне моря, гПа */
    val pressure: Int,
    /** Температура */
    val dayTemp: Double,
    val eveTemp: Double,
    val maxTemp: Double,
    val minTemp: Double,
    val mornTemp: Double,
    val nightTemp: Double,
    /** Погодные условия */
    val weather: List<WeatherDetails>,
    /** Скорость ветра */
    val windSpeed: Double,
    /** Температура по ощущениям */
    val dayFeels: Double,
    val eveFeels: Double,
    val mornFeels: Double,
    val nightFeels: Double
) : Parcelable