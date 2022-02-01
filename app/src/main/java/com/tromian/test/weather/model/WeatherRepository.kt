package com.tromian.test.weather.model

import com.google.android.libraries.places.api.model.Place
import com.tromian.test.weather.model.pojo.CurrentCity
import com.tromian.test.weather.model.pojo.CurrentWeather
import com.tromian.test.weather.model.pojo.DailyWeather

interface WeatherRepository {

    fun checkIfNeedUpdate(city: String): Boolean
    fun saveLocationToDB(currentCity: CurrentCity)
    fun getLocationFromDB(): CurrentCity?
    fun saveCurrentWeatherToDB(currentWeather: CurrentWeather)
    fun saveDailyWeatherListToDB(dailyList: List<DailyWeather>)
    fun getCurrentWeatherFromDB(city: String): CurrentWeather?
    fun getDailyWeatherListFromDB(): List<DailyWeather>
    suspend fun loadCurrentWeatherByCoord(place: Place): CurrentWeather?
    suspend fun loadWeeklyWeatherList(place: Place): List<DailyWeather>
}