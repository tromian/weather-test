package com.tromian.test.weather.model

typealias Mapper<Input, Output> = (Input) -> Output

sealed class WeatherResult<T> {
    fun <R> map(mapper: Mapper<T, R>? = null): WeatherResult<R> = when (this) {
        is PendingResult -> PendingResult()
        is ErrorResult -> ErrorResult(this.exception)
        is SuccessResult -> {
            if (mapper == null) throw IllegalArgumentException("Mapper should not be null")
            SuccessResult(mapper(this.data))
        }
    }
}

class PendingResult<T> : WeatherResult<T>()

class SuccessResult<T>(
    val data: T
) : WeatherResult<T>()

class ErrorResult<T>(
    val exception: Exception
) : WeatherResult<T>()

fun <T> WeatherResult<T>.takeSuccess(): T? {
    return if (this is SuccessResult)
        this.data
    else
        null
}