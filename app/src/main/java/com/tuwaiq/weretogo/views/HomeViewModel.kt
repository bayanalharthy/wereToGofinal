package com.tuwaiq.weretogo.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuwaiq.weretogo.network.WeatherRepo
import com.tuwaiq.weretogo.network.model.WeatherResponse
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private var weatherResponse = MutableLiveData<WeatherResponse>()

    fun getWeather(lat: String, lng: String): LiveData<WeatherResponse> {
        viewModelScope.launch {
            kotlin.runCatching {
                weatherResponse.postValue(WeatherRepo().getWeather(lat, lng))
            }.onFailure {

            }
        }
        return weatherResponse
    }
}