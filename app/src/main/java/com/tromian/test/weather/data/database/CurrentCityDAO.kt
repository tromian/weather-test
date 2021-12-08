package com.tromian.test.weather.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tromian.test.weather.data.entity.CurrentCityEntity

@Dao
interface CurrentCityDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveCurrentLocation(cityEntity: CurrentCityEntity)

    @Query("select count(*) from current_city")
    fun checkCurrentLocationCount(): Int

    @Query("select * from current_city limit 1")
    fun getCurrentLocation(): CurrentCityEntity

    @Query("delete from current_city")
    fun clearCurrentCityTable()
}