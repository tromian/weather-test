package com.tromian.test.weather.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tromian.test.weather.data.EmptyEntity

/**
 * БД пока не используется, по хорошему нужно сперва определится
 * с доменной моделью, та что приходит от сети не очень,
 * нужно придумать как правильно их разделить и какие данные будут
 * моделью, эту же модель можно будет для вью использовать.
 * */

@Database(entities = [EmptyEntity::class], version = 1, exportSchema = false)
abstract class WeatherDB : RoomDatabase() {

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