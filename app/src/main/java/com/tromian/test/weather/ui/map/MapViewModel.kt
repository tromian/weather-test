package com.tromian.test.weather.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tromian.test.weather.AppConstants
import com.tromian.test.weather.data.Coordinate

class MapViewModel : ViewModel() {

    private val _coordinate = MutableLiveData<Coordinate>().apply {
        value = Coordinate(
            lat = AppConstants.KYIV_LAT,
            lon = AppConstants.KYIV_LON
        )
    }
    val coordinate: LiveData<Coordinate> = _coordinate

    fun setCoordinate(coord: Coordinate) {
        _coordinate.postValue(coord)
    }

}