package com.tromian.test.weather.data.current

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Обьект погоды по названию города в отвеете от АПИ
 * */

@Parcelize
data class CurrentWeather(
    @SerializedName("clouds")
    /** Облачность,% */
    val clouds: Clouds,
    @SerializedName("coord")
    /** Географическое положение города */
    val coordinate: Coordinate,
    @SerializedName("dt")
    /** Время расчета данных, unix, UTC */
    val dt: Long,
    @SerializedName("id")
    /** ID города */
    val id: Int,
    @SerializedName("main")
    /** Температура, ... */
    val main: Main,
    @SerializedName("name")
    /** Название города */
    val name: String,
    @SerializedName("timezone")
    /** Сдвиг в секундах от UTC */
    val timezone: Int,
    @SerializedName("weather")
    /** Погодные условия */
    val weather: List<Weather>,
    @SerializedName("wind")
    /** Скорость ветра ... */
    val wind: Wind
) : Parcelable

@Parcelize
data class Main(
    @SerializedName("feels_like")
    /** Температура по восприятию */
    val feelsLike: Double,
    @SerializedName("humidity")
    /** Влажность, % */
    val humidity: Int,
    @SerializedName("pressure")
    /** Атмосферное давление , гПа*/
    val pressure: Int,
    @SerializedName("temp")
    /** Температура */
    val temp: Double
) : Parcelable

@Parcelize
data class Clouds(
    @SerializedName("all")
    /** Облачность,% */
    val all: Int
) : Parcelable

@Parcelize
data class Weather(
    @SerializedName("description")
    /** Погодные условия в группе */
    val description: String,
    @SerializedName("icon")
    /** Идентификатор значка погоды */
    val icon: String,
    @SerializedName("id")
    /** Идентификатор погодных условий */
    val id: Int,
    @SerializedName("main")
    /** Группа погодных параметров (Дождь, Снег, Экстрим и др.) */
    val main: String
) : Parcelable

@Parcelize
data class Wind(
    @SerializedName("deg")
    /** Направление ветра, градусы */
    val deg: Int,
    @SerializedName("speed")
    /** Скорость ветра метр/сек */
    val speed: Double
) : Parcelable

@Parcelize
data class Coordinate(
    @SerializedName("lat")
    /** широта */
    val lat: Double,
    @SerializedName("lon")
    /** долгота */
    val lon: Double
) : Parcelable