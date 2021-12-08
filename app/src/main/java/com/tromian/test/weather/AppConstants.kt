package com.tromian.test.weather

import com.tromian.test.wether.BuildConfig


object AppConstants {
    const val BASE_URL_OPENWEATHER = "https://api.openweathermap.org/data/2.5/"
    const val LANGUAGE = "en"
    const val REQUEST_CODE_LOCATION_PERMISSION = 0
    const val WEATHER_API_KEY = BuildConfig.OpenWetherAPIKey
    const val KYIV_LAT = 50.4333
    const val KYIV_LON = 30.5167
    const val GOOGLE_API_KEY = BuildConfig.MAPS_API_KEY
    const val KYIV_NAME = "Kyiv"
}