package com.tromian.test.weather.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tromian.test.weather.data.entity.CurrentWeatherEntity

@Dao
interface CurrentWeatherDAO {
    @Query("select count(*) from current_weather where city_name like :city")
    fun checkCurrentTableCount(city: String): Int

    @Query("select unix_time from current_weather where city_name like :city")
    fun getLastUpdateTime(city: String): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCurrentWeather(currentWeatherEntity: CurrentWeatherEntity)

    @Query("select * from current_weather where city_name like :city")
    fun getCurrentWeatherByCityName(city: String): CurrentWeatherEntity
}