package com.tuwaiq.weretogo.utils

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.*
import android.os.Bundle
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment

class LocationTracker : LocationListener {
    private var locationManager // Declaring a Location Manager
            : LocationManager?

    var location: Location? = null
        private set
    private var mListener: Listener?

    @RequiresPermission(anyOf = [permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION])
    constructor(fragment: Fragment, callback: Listener?) {
        locationManager =
            fragment.context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mListener = callback
//        UIUtils.createInstance().addOnFragmentDestroyedObserver(fragment) { destroy() }
    }

    @RequiresPermission(anyOf = [permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION])
    constructor(activity: Activity, callback: Listener?) {
        locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mListener = callback
//        UIUtils.createInstance().addOnActivityDestroyed(activity) { destroy() }
    }

    val isGPSEnabled: Boolean?
        get() = if (locationManager != null) {
            locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } else null

    @SuppressLint("MissingPermission")
    fun startLocationUpdating() {
        try {
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled!!) {
                stopLocationUpdating()
                locationManager!!.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                    this
                )
                locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                    this
                )
                location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location == null) {
                    location =
                        locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }
                if (mListener != null && location != null) {
                    mListener!!.onLocationFounded(this, location!!)
                }
            } else {
                if (mListener != null) {
                    mListener!!.onErrorHappened(this, Listener.ERR_GPS_CLOSED, "")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (mListener != null) {
                mListener!!.onErrorHappened(this, Listener.ERR_EXCEPTION, e.message)
            }
        }
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    @SuppressLint("MissingPermission")
    fun stopLocationUpdating() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this@LocationTracker)
        }
    }

    fun getLocationDescription(context: Context?): Address? {
        val geocoder = Geocoder(context)
        try {
            val addresses = geocoder.getFromLocation(
                location!!.latitude, location!!.longitude, 1
            )
            return addresses[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @JvmOverloads
    fun showGPSDisabledAlert(
        context: Context, title: String? =
            "GPS", message: String? =
            "Enable GPS to complete", mainButtonTitle: String? =
            "Location Settings", cancelButtonTitle: String? =
            "Cancel", cancel: Runnable? =
            null
    ) {
//        MessageDialog.create(context)
//            .setMessage(message)
//            .setTitle(title)
//            .setButton1(mainButtonTitle) { dialog ->
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                context.startActivity(intent)
//                Handler(Looper.getMainLooper())
//                    .postDelayed(
//                        { startLocationUpdating() },
//                        10000
//                    )
//            }
//            .setButton2(cancelButtonTitle) { dialog -> cancel?.run() }
//            .setCancelable(false)
//            .show()

    }

    //----------------------------------------------------------------------------------------------
    fun destroy() {
        stopLocationUpdating()
        mListener = null
        locationManager = null
    }

    val isDestroyed: Boolean
        get() = locationManager == null

    //----------------------------------------------------------------------------------------------
    override fun onLocationChanged(location: Location) {
        if (location != null) {
            this.location = location
            if (mListener != null) {
                mListener!!.onLocationFounded(this, location)
            }
        }
    }

    override fun onProviderDisabled(provider: String) {
        if (mListener != null) mListener!!.onLocationProviderStatusChanged(this, provider, true)
    }

    override fun onProviderEnabled(provider: String) {
        if (mListener != null) mListener!!.onLocationProviderStatusChanged(this, provider, false)
    }

    @Deprecated("")
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
    }

    //----------------------------------------------------------------------------------------------
    interface Listener {
        fun onLocationFounded(obj: LocationTracker?, location: Location)

        /**
         * @param provider [LocationManager.GPS_PROVIDER] or [LocationManager.NETWORK_PROVIDER]
         */
        fun onLocationProviderStatusChanged(
            obj: LocationTracker?,
            provider: String?,
            disabled: Boolean
        )

        /**
         * @param errorCode [.ERR_GPS_CLOSED] or [.ERR_EXCEPTION]
         */
        fun onErrorHappened(obj: LocationTracker?, errorCode: Int, msg: String?)

        companion object {
            const val ERR_GPS_CLOSED = 1
            const val ERR_EXCEPTION = 2
        }
    }

    companion object {
        // The minimum distance to change Updates in meters
        var MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10

        // The minimum time between updates in milliseconds
        var MIN_TIME_BW_UPDATES: Long = 1000
    }
}