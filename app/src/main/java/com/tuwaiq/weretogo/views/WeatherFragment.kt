package com.tuwaiq.weretogo.views

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tuwaiq.weretogo.databinding.FragmentWeatherBinding
import com.tuwaiq.weretogo.utils.GpsTracker
import java.util.*
import kotlin.math.roundToInt

class WeatherFragment : Fragment() {

    private lateinit var binding: FragmentWeatherBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLocation()

    }

    @SuppressLint("SetTextI18n")
    private fun getLocation() {
        val gpsTracker = GpsTracker(requireContext())
        if (gpsTracker.canGetLocation()) {
            val latitude = gpsTracker.latitude
            val longitude = gpsTracker.longitude

            viewModel.getWeather(latitude.toString(), longitude.toString()).observe(this, {
                binding.loader.hide()
                binding.weather.text = it.main.temp.roundToInt().toString() + " " + DegreeSymbol.C.value
                setUpCity(latitude.toString(), longitude.toString())
            })

        } else {
            gpsTracker.showSettingsAlert()
        }
    }

    private fun setUpCity(lat: String, lng: String) {
        val geocoder = Geocoder(requireContext(), Locale.forLanguageTag("en"))
        val addresses: MutableList<Address> = geocoder.getFromLocation(
            lat.toDouble(),
            lng.toDouble(), 1
        )

        if (addresses.isNotEmpty()){
            val address = addresses[0].getAddressLine(0)
            val city = addresses[0].locality
            binding.city.text = city
            binding.loader.hide()
        }

    }
}

enum class DegreeSymbol(val value: String) {
    C("\u2103"),
    F("\u2109"),
    Degree("\u00B0"),

}