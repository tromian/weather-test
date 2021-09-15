package com.tromian.test.weather

import android.app.Application
import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.tromian.test.weather.data.WeatherRepository
import com.tromian.test.weather.data.database.WeatherDB
import com.tromian.test.weather.data.network.WeatherApi
import com.tromian.test.weather.ui.main.MainFragment
import com.tromian.test.weather.ui.splash.SplashFragment
import com.tromian.test.weather.ui.today.TodayFragment
import com.tromian.test.weather.ui.week.WeekFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    fun inject(fragment: TodayFragment)
    fun inject(fragment: WeekFragment)
    fun inject(fragment: MainFragment)
    fun inject(fragment: SplashFragment)

    @Component.Factory
    interface Builder {

        fun create(
            @BindsInstance
            appContext: Application,
            @BindsInstance
            @WeatherApiQualifier
            apiKey: String,
        ): AppComponent

    }
}

@Qualifier
annotation class WeatherApiQualifier

@Module(includes = [NetworkModule::class, LocalDBModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideRepository(
        localDB: WeatherDB,
        weatherApi: WeatherApi,
        appContext: Application,
    ): WeatherRepository {
        return WeatherRepository(localDB, weatherApi, appContext)
    }
}

@Module
class NetworkModule {
    @Provides
    fun provideWeatherService(@WeatherApiQualifier apiKey: String): WeatherApi {
        val authInterceptor = Interceptor { chain ->
            val newUrl = chain.request().url
                .newBuilder()
                .addQueryParameter("appid", apiKey)
                .addQueryParameter("lang", AppConstants.LANGUAGE)
                .build()

            val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)

        }

        val logger = HttpLoggingInterceptor(
            HttpLoggingInterceptor.Logger { Log.d("API", it) }
        ).apply {
            this.level = HttpLoggingInterceptor.Level.BASIC
        }

        val tmdbClient = OkHttpClient().newBuilder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logger)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .client(tmdbClient)
            .baseUrl(AppConstants.BASE_URL_OPENWEATHER)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        return retrofit.create(WeatherApi::class.java)
    }
}

@Module
class LocalDBModule {
    @Provides
    @Singleton
    fun provideLocalDB(appContext: Application): WeatherDB {
        return WeatherDB.getInstance(appContext)
    }

}