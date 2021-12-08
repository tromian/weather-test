package com.tromian.test.weather.data

import android.content.Context
import android.util.Log
import com.google.android.libraries.places.api.model.Place
import com.tromian.test.weather.data.database.WeatherDB
import com.tromian.test.weather.data.network.NetworkConnection
import com.tromian.test.weather.data.network.WeatherApi
import com.tromian.test.weather.model.pojo.CurrentCity
import com.tromian.test.weather.model.pojo.CurrentWeather
import com.tromian.test.weather.model.pojo.DailyWeather
import com.tromian.test.weather.model.pojo.WeatherDetails
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val db: WeatherDB,
    private val weatherApi: WeatherApi,
    private val context: Context
) {
    private val currentTime = System.currentTimeMillis()

    /**-------------------------Operations with DB------------------------* */

    private fun getLastUpdateTimeByCityName(city: String): Long {
        return db.currentWeatherDAO().getLastUpdateTime(city)
    }

    fun checkIfNeedUpdate(city: String): Boolean {
        val lastUpdateTime = getLastUpdateTimeByCityName(city)
        val diff = (currentTime - lastUpdateTime * 1000) / (1000L * 60)
        return diff > 30
    }

    fun saveLocationToDB(currentCity: CurrentCity) {
        db.currentCityDAO().clearCurrentCityTable()
        db.currentCityDAO().saveCurrentLocation(currentCity.toEntity())
    }

    fun getLocationFromDB(): CurrentCity? {
        return if (db.currentCityDAO().checkCurrentLocationCount() > 0) {
            db.currentCityDAO().getCurrentLocation().toDomain()
        } else null
    }


    fun saveCurrentWeatherToDB(currentWeather: CurrentWeather) {
        db.currentWeatherDAO().saveCurrentWeather(currentWeather.toEntity())
        currentWeather.weather?.let { db.weatherDAO().saveWeatherDetails(it.toEntity()) }
    }

    private fun getWeatherDetailsFromDB(id: Int): WeatherDetails? {

        return try {
            db.weatherDAO().getWeatherDetailByID(id).toDomain()
        } catch (e: IOException) {
            null
        }

    }

    fun saveDailyWeatherListToDB(dailyList: List<DailyWeather>) {
        db.dailyWeatherDAO().clearTable()
        dailyList.forEach {
            val weatherDetails = it.weather
            if (weatherDetails != null) {
                db.weatherDAO().saveWeatherDetails(weatherDetails.toEntity())
            }
        }
        db.dailyWeatherDAO().saveDailyWeatherList(dailyList.map {
            it.toEntity()
        })
    }

    fun getCurrentWeatherFromDB(city: String): CurrentWeather? {
        return if (db.currentWeatherDAO().checkCurrentTableCount(city) > 0) {
            val entity = db.currentWeatherDAO().getCurrentWeatherByCityName(city)
            if (entity.weatherId != null) {
                val detailsDomain = getWeatherDetailsFromDB(entity.weatherId)
                entity.toDomain().apply {
                    weather = detailsDomain
                }
            } else entity.toDomain()

        } else null
    }

    fun getDailyWeatherListFromDB(): List<DailyWeather> {
        return if (db.dailyWeatherDAO().checkDailyTableCount() > 0) {
            val listEntity = db.dailyWeatherDAO().getDailyWeatherList()
            listEntity.map {
                val entity = it
                if (entity.weatherId != null) {
                    val detailsDomain = getWeatherDetailsFromDB(entity.weatherId)
                    entity.toDomain().apply {
                        weather = detailsDomain
                    }
                } else entity.toDomain()
            }
        } else emptyList()
    }

    /**-------------------------Operations with Network----------------* */

    suspend fun loadCurrentWeatherByCoord(place: Place): CurrentWeather? {
        return if (NetworkConnection.isNetworkAvailable(context)) {
            try {
                weatherApi.getCurrentWeatherByCoordinate(
                    place.latLng?.latitude.toString(),
                    place.latLng?.longitude.toString()
                ).toDomain()
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
                ).dailyWeatherList.map { it.toDomain() }
            } catch (e: HttpException) {
                e.message?.let { Log.e("http", it) }
                emptyList()
            } catch (e: IOException) {
                e.message?.let { Log.e("IOException", it) }
                emptyList()
            }

        } else return emptyList()
    }

}

