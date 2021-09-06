package com.tromian.test.weather.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkConnection {

    fun isNetworkAvailable(context: Context): Boolean {
        val conManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = conManager.activeNetwork ?: return false
            val capabilities = conManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }

        } else {
            conManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    else -> false
                }
            }
        }
        return false
    }

}