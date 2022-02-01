package com.tromian.test.weather.data.daily

import com.google.gson.annotations.SerializedName
import com.tromian.test.weather.data.current.Weather


data class DailyResponse(
    @SerializedName("daily")
    val dailyWeatherList: List<DailyWeatherResponse>
)

/**
 * Ответ API данных о погоде на день
 * */

data class DailyWeatherResponse(
    @SerializedName("clouds")
    /** Облачность,% */
    val clouds: Int,
    @SerializedName("dt")
    /** Время прогнозируемых данных, Unix, UTC */
    val dt: Long,
    @SerializedName("feels_like")
    /** Температура по восприятию */
    val feels_like: FeelsLike,
    @SerializedName("humidity")
    /** Влажность, % */
    val humidity: Int,
    @SerializedName("pop")
    /** Вероятность выпадения осадков */
    val pop: Double,
    @SerializedName("pressure")
    /** Атмосферное давление на уровне моря, гПа */
    val pressure: Int,
    @SerializedName("temp")
    /** Температура на день С */
    val temp: Temp,
    @SerializedName("weather")
    /** Погодные условия */
    val weather: List<Weather>,
    @SerializedName("wind_speed")
    /** Скорость ветра */
    val wind_speed: Double
)

data class Hourly(
    @SerializedName("dt")
    val dt: Int,
    @SerializedName("temp")
    val temp: Double,
)


data class FeelsLike(
    @SerializedName("day")
    val day: Double,
    @SerializedName("eve")
    val eve: Double,
    @SerializedName("morn")
    val morn: Double,
    @SerializedName("night")
    val night: Double
)


data class Temp(
    @SerializedName("day")
    val day: Double,
    @SerializedName("eve")
    val eve: Double,
    @SerializedName("max")
    val max: Double,
    @SerializedName("min")
    val min: Double,
    @SerializedName("morn")
    val morn: Double,
    @SerializedName("night")
    val night: Double
)


