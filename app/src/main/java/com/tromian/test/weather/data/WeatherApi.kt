package com.tromian.test.weather.data

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getWeatherByCityName(
        @Query(value = "q") query: String,
        @Query(value = "units") units: String = "metric"
    ): CityWeather

    @GET("weather")
    suspend fun getWeatherByCoordinate(
        @Query(value = "lat") lat: String,
        @Query(value = "lon") lon: String,
        @Query(value = "units") units: String = "metric"
    ): CityWeather

    @GET("onecall")
    suspend fun getDailyWeatherByCoordinate(
        @Query(value = "lat") lat: String,
        @Query(value = "lon") lon: String,
        @Query(value = "units") units: String = "metric"
    ): DailyResponse

}