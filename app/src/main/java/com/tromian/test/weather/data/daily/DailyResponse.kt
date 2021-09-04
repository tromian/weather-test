package com.tromian.test.weather.data.daily

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class DailyResponse(
    @SerializedName("daily")
    val dailyList: List<Daily>
)

/**
 * Ответ API данных о погоде на день
 * */
@Parcelize
data class Daily(
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
) : Parcelable

@Parcelize
data class FeelsLike(
    @SerializedName("day")
    val day: Double,
    @SerializedName("eve")
    val eve: Double,
    @SerializedName("morn")
    val morn: Double,
    @SerializedName("night")
    val night: Double
) : Parcelable

@Parcelize
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
) : Parcelable

@Parcelize
data class Weather(
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: String
) : Parcelable
