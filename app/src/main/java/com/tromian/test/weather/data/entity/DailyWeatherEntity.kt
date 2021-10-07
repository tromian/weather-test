package com.tromian.test.weather.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "daily_weather")
data class DailyWeatherEntity(
    @PrimaryKey
    @ColumnInfo(name = "unix_time")
    /** Время прогнозируемых данных, Unix, UTC */
    val unixTime: Long,
    @ColumnInfo(name = "clouds")
    /** Облачность,% */
    val clouds: Int,
    @ColumnInfo(name = "humidity")
    /** Влажность, % */
    val humidity: Int,
    @ColumnInfo(name = "pop")
    /** Вероятность выпадения осадков */
    val pop: Double,
    @ColumnInfo(name = "pressure")
    /** Атмосферное давление на уровне моря, гПа */
    val pressure: Int,
    @ColumnInfo(name = "day")
    val day: Double,
    @ColumnInfo(name = "eve")
    val eve: Double,
    @ColumnInfo(name = "max")
    val max: Double,
    @ColumnInfo(name = "min")
    val min: Double,
    @ColumnInfo(name = "morn")
    val morn: Double,
    @ColumnInfo(name = "night")
    val night: Double,
    @ColumnInfo(name = "weather_id")
    /** Погодные условия */
    val weatherId: List<Int>,
    @ColumnInfo(name = "wind_speed")
    /** Скорость ветра */
    val windSpeed: Double,
    @ColumnInfo(name = "day_feels")
    val dayFeels: Double,
    @ColumnInfo(name = "eve_feels")
    val eveFeels: Double,
    @ColumnInfo(name = "morn_feels")
    val mornFeels: Double,
    @ColumnInfo(name = "night_feels")
    val nightFeels: Double
)

