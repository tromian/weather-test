package com.tromian.test.weather.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tromian.test.weather.model.WeatherResult

typealias LiveResult<T> = LiveData<WeatherResult<T>>
typealias MutableLiveResult<T> = MutableLiveData<WeatherResult<T>>


fun Fragment.activityViewModel(): MainActivityViewModel {
    if (activity is MainActivity) {
        return (activity as MainActivity).viewModel
    } else throw Exception("MainActivity does not exist !!! ")
}