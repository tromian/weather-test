package com.tromian.test.weather.data.network

import com.tromian.test.weather.data.current.CurrentWeather
import com.tromian.test.weather.data.daily.DailyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    
    @GET("weather")
    suspend fun getCurrentWeatherByCoordinate(
        @Query(value = "lat") lat: String,
        @Query(value = "lon") lon: String,
        @Query(value = "units") units: String = "metric"
    ): CurrentWeather

    @GET("onecall")
    suspend fun getDailyWeatherByCoordinate(
        @Query(value = "lat") lat: String,
        @Query(value = "lon") lon: String,
        @Query(value = "exclude") exclude: String = "current",
        @Query(value = "units") units: String = "metric"
    ): DailyResponse

}