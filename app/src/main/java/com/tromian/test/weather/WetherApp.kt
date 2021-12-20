package com.tromian.test.weather

import android.app.Application
import android.content.Context
import com.tromian.test.weather.di.AppComponent
import com.tromian.test.weather.di.DaggerAppComponent
import com.tromian.test.weather.utils.AppConstants
import com.tromian.test.wether.BuildConfig
import com.tromian.test.wether.R
import timber.log.Timber


class WeatherApp : Application() {
    private var _appComponent: AppComponent? = null
    internal val appComponent: AppComponent
        get() = checkNotNull(_appComponent) {
            "AppComponent isn't initialized"
        }

    override fun onCreate() {
        super.onCreate()
        _appComponent = DaggerAppComponent.factory().create(
            this,
            AppConstants.WEATHER_API_KEY,
            getString(R.string.api_language)
        )
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is WeatherApp -> appComponent
        else -> this.applicationContext.appComponent
    }