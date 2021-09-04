package com.tromian.test.weather.data

import com.google.android.libraries.places.api.model.Place
import com.tromian.test.weather.data.daily.Daily
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi
) {

    suspend fun loadTodayByCityName(cityName: String): CityWeather =
        weatherApi.getWeatherByCityName(cityName)

    suspend fun loadWeeklyWeatherList(place: Place): List<Daily> =
        weatherApi.getDailyWeatherByCoordinate(
            place.latLng?.latitude.toString(),
            place.latLng?.longitude.toString()
        ).dailyList

}