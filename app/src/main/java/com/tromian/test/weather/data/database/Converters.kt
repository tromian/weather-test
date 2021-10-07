package com.tromian.test.weather.data.database

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun weatherIdToString(weatherId: List<Int>): String {
        return weatherId.joinToString(",")
    }

    @TypeConverter
    fun stringToWeatherId(stringListId: String): List<Int> {
        return stringListId.split(",").map { it.toInt() }
    }

}