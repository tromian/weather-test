package com.tromian.test.weather.ui

import androidx.fragment.app.Fragment


fun Fragment.activityViewModel(): MainActivityViewModel {
    if (activity is MainActivity) {
        return (activity as MainActivity).viewModel
    } else throw Exception("MainActivity does not exist !!! ")
}