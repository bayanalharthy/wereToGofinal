package com.tuwaiq.weretogo.network

import com.tuwaiq.weretogo.BuildConfig
import com.tuwaiq.weretogo.network.model.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "WeatherRepo"

class WeatherRepo {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val api = retrofit.create(WeatherApi::class.java)

    suspend fun getWeather(latitude: String,
                           longitude: String,): WeatherResponse {
        return api.getWeather(latitude = latitude , longitude =longitude)
    }
}