package com.tuwaiq.weretogo.network

import com.tuwaiq.weretogo.BuildConfig
import com.tuwaiq.weretogo.network.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") units: String = "metric",
        @Query("appid") appId: String = BuildConfig.API_KEY,
    ): WeatherResponse

}