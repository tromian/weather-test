package com.tromian.test.weather.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tromian.test.weather.data.entity.DailyWeatherEntity

@Dao
interface DailyWeatherDAO {

    @Query("select count(*) from daily_weather")
    fun checkDailyTableCount(): Int

    @Query("select unix_time from daily_weather limit 1")
    fun getLastUpdateTime(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDailyWeatherList(dailyWeatherEntity: List<DailyWeatherEntity>)

    @Query("select * from daily_weather")
    fun getDailyWeatherList(): List<DailyWeatherEntity>

    @Query("delete from daily_weather")
    fun clearTable()
}