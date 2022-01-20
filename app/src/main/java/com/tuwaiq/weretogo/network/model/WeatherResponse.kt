package com.tuwaiq.weretogo.network.model


import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("base")
    var base: String, // stations
    @SerializedName("clouds")
    var clouds: Clouds,
    @SerializedName("cod")
    var cod: Int, // 200
    @SerializedName("coord")
    var coord: Coord,
    @SerializedName("dt")
    var dt: Int, // 1642074679
    @SerializedName("id")
    var id: Int, // 5375480
    @SerializedName("main")
    var main: Main,
    @SerializedName("name")
    var name: String, // Mountain View
    @SerializedName("sys")
    var sys: Sys,
    @SerializedName("timezone")
    var timezone: Int, // -28800
    @SerializedName("visibility")
    var visibility: Int, // 10000
    @SerializedName("weather")
    var weather: List<Weather>,
    @SerializedName("wind")
    var wind: Wind
) {
    data class Clouds(
        @SerializedName("all")
        var all: Int // 0
    )

    data class Coord(
        @SerializedName("lat")
        var lat: Double, // 37.4219
        @SerializedName("lon")
        var lon: Double // -122.0835
    )

    data class Main(
        @SerializedName("feels_like")
        var feelsLike: Double, // 282.97
        @SerializedName("humidity")
        var humidity: Int, // 82
        @SerializedName("pressure")
        var pressure: Int, // 1024
        @SerializedName("temp")
        var temp: Double, // 282.97
        @SerializedName("temp_max")
        var tempMax: Double, // 286.27
        @SerializedName("temp_min")
        var tempMin: Double // 280.16
    )

    data class Sys(
        @SerializedName("country")
        var country: String, // US
        @SerializedName("id")
        var id: Int, // 5122
        @SerializedName("sunrise")
        var sunrise: Int, // 1642087324
        @SerializedName("sunset")
        var sunset: Int, // 1642122700
        @SerializedName("type")
        var type: Int // 1
    )

    data class Weather(
        @SerializedName("description")
        var description: String, // mist
        @SerializedName("icon")
        var icon: String, // 50n
        @SerializedName("id")
        var id: Int, // 701
        @SerializedName("main")
        var main: String // Mist
    )

    data class Wind(
        @SerializedName("deg")
        var deg: Double, // 0
        @SerializedName("speed")
        var speed: Double // 0
    )
}