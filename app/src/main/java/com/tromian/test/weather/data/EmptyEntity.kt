package com.tromian.test.weather.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Временная таблица - заглушка
 * */

@Entity
data class EmptyEntity(
    @PrimaryKey
    val id: Int,
    val name: String
) {
}