package com.tromian.test.weather.data

import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.tromian.test.weather.data.current.CurrentWeatherResponse
import com.tromian.test.weather.data.current.Weather
import com.tromian.test.weather.data.daily.DailyWeatherResponse
import com.tromian.test.weather.data.entity.CurrentCityEntity
import com.tromian.test.weather.data.entity.CurrentWeatherEntity
import com.tromian.test.weather.data.entity.DailyWeatherEntity
import com.tromian.test.weather.data.entity.WeatherEntity
import com.tromian.test.weather.model.pojo.CurrentCity
import com.tromian.test.weather.model.pojo.CurrentWeather
import com.tromian.test.weather.model.pojo.DailyWeather
import com.tromian.test.weather.model.pojo.WeatherDetails

/**
 * Domain and DB Entity mappers
 * */

fun CurrentCityEntity.toDomain(): CurrentCity {
    return CurrentCity(
        cityName = this.city,
        lat = this.lat,
        lon = this.lon
    )
}

fun CurrentCity.toEntity(): CurrentCityEntity {
    return CurrentCityEntity(
        city = this.cityName,
        lat = this.lat,
        lon = this.lon
    )
}

fun DailyWeather.toEntity(): DailyWeatherEntity {

    return DailyWeatherEntity(
        unixTime = this.unixTime,
        clouds = this.clouds,
        humidity = this.humidity,
        pop = this.pop,
        pressure = this.pressure,
        dayTemp = this.dayTemp,
        eveTemp = this.eveTemp,
        maxTemp = this.maxTemp,
        minTemp = this.minTemp,
        mornTemp = this.mornTemp,
        nightTemp = this.nightTemp,
        weatherId = this.weather?.id,
        windSpeed = this.windSpeed,
        dayFeels = this.dayFeels,
        eveFeels = this.eveFeels,
        mornFeels = this.mornFeels,
        nightFeels = this.nightFeels
    )

}

fun CurrentWeather.toEntity(): CurrentWeatherEntity {
    return CurrentWeatherEntity(
        id = this.id,
        unixTime = this.unixTime,
        feelsLike = this.feelsLike,
        humidity = this.humidity,
        pressure = this.pressure,
        currentTemp = this.currentTemp,
        cityName = this.cityName,
        timezone = this.timezone,
        weatherId = this.weather?.id,
        clouds = this.clouds,
        windDeg = this.windDeg,
        windSpeed = this.windSpeed
    )
}

fun WeatherDetails.toEntity(): WeatherEntity {
    return WeatherEntity(
        id = this.id,
        description = this.description,
        icon = this.icon,
        name = this.name
    )
}

fun DailyWeatherEntity.toDomain(): DailyWeather {
    return DailyWeather(
        unixTime = this.unixTime,
        clouds = this.clouds,
        humidity = this.humidity,
        pop = this.pop,
        pressure = this.pressure,
        dayTemp = this.dayTemp,
        eveTemp = this.eveTemp,
        maxTemp = this.maxTemp,
        minTemp = this.minTemp,
        mornTemp = this.mornTemp,
        nightTemp = this.nightTemp,
        windSpeed = this.windSpeed,
        dayFeels = this.dayFeels,
        eveFeels = this.eveFeels,
        mornFeels = this.mornFeels,
        nightFeels = this.nightFeels
    )
}

fun CurrentWeatherEntity.toDomain(): CurrentWeather {

    return CurrentWeather(
        id = this.id,
        unixTime = this.unixTime,
        feelsLike = this.feelsLike,
        humidity = this.humidity,
        pressure = this.pressure,
        currentTemp = this.currentTemp,
        cityName = this.cityName,
        timezone = this.timezone,
        clouds = this.clouds,
        windDeg = this.windDeg,
        windSpeed = this.windSpeed
    )
}


fun WeatherEntity.toDomain(): WeatherDetails {
    return WeatherDetails(
        id = this.id,
        description = this.description,
        icon = this.icon,
        name = this.name
    )
}


/**
 * Api json response and domain pojo mappers
 * */
fun CurrentWeatherResponse.toDomain(): CurrentWeather {
    return CurrentWeather(
        id = this.id,
        unixTime = this.dt,
        feelsLike = this.main.feelsLike,
        humidity = this.main.humidity,
        pressure = this.main.pressure,
        currentTemp = this.main.temp,
        cityName = this.name,
        timezone = this.timezone,
        weather = this.weather[0].toDomain(),
        clouds = this.clouds.all,
        windDeg = this.wind.deg,
        windSpeed = this.wind.speed

    )
}

fun DailyWeatherResponse.toDomain(): DailyWeather {
    return DailyWeather(
        unixTime = this.dt,
        clouds = this.clouds,
        humidity = this.humidity,
        pop = this.pop,
        pressure = this.pressure,
        dayTemp = this.temp.day,
        eveTemp = this.temp.eve,
        maxTemp = this.temp.max,
        minTemp = this.temp.min,
        mornTemp = this.temp.morn,
        nightTemp = this.temp.night,
        weather = this.weather[0].toDomain(),
        windSpeed = this.wind_speed,
        dayFeels = this.feels_like.day,
        eveFeels = this.feels_like.eve,
        mornFeels = this.feels_like.morn,
        nightFeels = this.feels_like.night
    )
}

fun Weather.toDomain(): WeatherDetails {
    return WeatherDetails(
        id = this.id,
        description = this.description,
        icon = this.icon,
        name = this.main
    )
}

fun CurrentCity.toPlace(): Place {
    return Place.builder()
        .setName(this.cityName)
        .setLatLng(
            LatLng(this.lat, this.lon)
        ).build()
}

fun Place.toCity(): CurrentCity {
    return CurrentCity(
        this.name ?: "",
        this.latLng?.latitude ?: 0.0,
        this.latLng?.longitude ?: 0.0
    )

}
