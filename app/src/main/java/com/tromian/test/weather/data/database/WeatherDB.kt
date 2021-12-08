package com.tromian.test.weather.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tromian.test.weather.data.entity.CurrentCityEntity
import com.tromian.test.weather.data.entity.CurrentWeatherEntity
import com.tromian.test.weather.data.entity.DailyWeatherEntity
import com.tromian.test.weather.data.entity.WeatherEntity

@Database(
    entities = [
        CurrentCityEntity::class,
        CurrentWeatherEntity::class,
        DailyWeatherEntity::class,
        WeatherEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WeatherDB : RoomDatabase() {

    abstract fun weatherDAO(): WeatherDAO
    abstract fun currentWeatherDAO(): CurrentWeatherDAO
    abstract fun currentCityDAO(): CurrentCityDAO
    abstract fun dailyWeatherDAO(): DailyWeatherDAO

    companion object {
        private const val DATABASE_NAME = "weather.db"

        private var INSTANCE: WeatherDB? = null

        private val lock = Any()

        fun getInstance(appContext: Context): WeatherDB {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        appContext,
                        WeatherDB::class.java, DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
                return INSTANCE!!
            }
        }

    }
}