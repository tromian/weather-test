package com.tromian.test.weather.data

import android.content.Context
import android.util.Log
import com.google.android.libraries.places.api.model.Place
import retrofit2.HttpException
import javax.inject.Inject
class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val context: Context
) {

    suspend fun loadTodayByCityName(cityName: String): CityWeather? {
        return if (NetworkConnection.isNetworkAvailable(context)) {
            try {
                weatherApi.getWeatherByCityName(cityName)
            } catch (e: HttpException) {
                e.message?.let { Log.e("http", it) }
                null
            }
        } else null

    }

    suspend fun loadWeeklyWeatherList(place: Place): List<DailyWeather> {
        return if (NetworkConnection.isNetworkAvailable(context)) {
            try {
                return weatherApi.getDailyWeatherByCoordinate(
                    place.latLng?.latitude.toString(),
                    place.latLng?.longitude.toString()
                ).dailyWeatherList
            } catch (e: HttpException) {
                e.message?.let { Log.e("http", it) }
                emptyList()
            }

        } else return emptyList()
    }
}

