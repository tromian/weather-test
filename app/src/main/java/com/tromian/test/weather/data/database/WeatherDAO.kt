package com.tromian.test.weather.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tromian.test.weather.data.entity.WeatherEntity

@Dao
interface WeatherDAO {

    /** Weather details */

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveWeatherDetails(details: WeatherEntity)

    @Query("select * from weather where id = :id")
    fun getWeatherDetailByID(id: Int): WeatherEntity
}