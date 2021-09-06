package com.tromian.test.weather.data

import android.content.Context
import android.util.Log
import com.google.android.libraries.places.api.model.Place
import com.tromian.test.weather.data.current.CurrentWeather
import com.tromian.test.weather.data.daily.DailyWeather
import com.tromian.test.weather.data.database.WeatherDB
import com.tromian.test.weather.data.network.NetworkConnection
import com.tromian.test.weather.data.network.WeatherApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
class WeatherRepository @Inject constructor(
    private val db: WeatherDB,
    private val weatherApi: WeatherApi,
    private val context: Context
) {

    suspend fun loadCurrentWeatherByCoord(place: Place): CurrentWeather? {
        return if (NetworkConnection.isNetworkAvailable(context)) {
            try {
                weatherApi.getCurrentWeatherByCoordinate(
                    place.latLng?.latitude.toString(),
                    place.latLng?.longitude.toString()
                )
            } catch (e: HttpException) {
                e.message?.let { Log.e("http", it) }
                null
            } catch (e: IOException) {
                e.message?.let { Log.e("IOException", it) }
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

