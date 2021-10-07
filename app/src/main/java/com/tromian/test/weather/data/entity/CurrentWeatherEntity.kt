package com.tromian.test.weather.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CurrentWeatherEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    /** ID города */
    val id: Int,
    @ColumnInfo(name = "unix_time")
    /** Время расчета данных, unix, UTC */
    val unixTime: Long,
    @ColumnInfo(name = "feels_like")
    /** Температура по восприятию */
    val feelsLike: Double,
    @ColumnInfo(name = "humidity")
    /** Влажность, % */
    val humidity: Int,
    @ColumnInfo(name = "pressure")
    /** Атмосферное давление , гПа*/
    val pressure: Int,
    @ColumnInfo(name = "current_temp")
    /** Температура */
    val currentTemp: Double,
    @ColumnInfo(name = "city_name")
    /** Название города */
    val cityName: String,
    @ColumnInfo(name = "timezone")
    /** Сдвиг в секундах от UTC */
    val timezone: Int,
    @ColumnInfo(name = "weather_id")
    /** Погодные условия */
    val weatherId: List<Int>,
    @ColumnInfo(name = "clouds")
    /** Облачность,% */
    val clouds: Int,
    @ColumnInfo(name = "wind_deg")
    /** Направление ветра, градусы */
    val windDeg: Int,
    @ColumnInfo(name = "wind_speed")
    /** Скорость ветра метр/сек */
    val windSpeed: Double
)
