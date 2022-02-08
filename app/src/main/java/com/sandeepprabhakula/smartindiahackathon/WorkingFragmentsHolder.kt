package com.sandeepprabhakula.smartindiahackathon

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.*
import com.sandeepprabhakula.smartindiahackathon.databinding.ActivityWorkingFragementsHolderBinding
import java.util.concurrent.TimeUnit

class WorkingFragmentsHolder : AppCompatActivity() {
    companion object{
        var lat:Double = 12.00
        var lon:Double = 14.00
    }
    private lateinit var context: Context
    private lateinit var locationManager: LocationManager
    var intent1: Intent? = null
    private var gpsStatus = false
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWorkingFragementsHolderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = applicationContext
        requestPermissions()
        checkGpsStatus()
        newLocationRequest()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper()!!)
        supportFragmentManager.beginTransaction().apply {
            replace(binding.workingFragmentsHost.id, ListServicesFragment())
            commit()
        }
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(binding.workingFragmentsHost.id, ListServicesFragment())
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        addToBackStack(null)
                        commit()
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.search -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(binding.workingFragmentsHost.id, SearchFragment())
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        addToBackStack(null)
                        commit()
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(binding.workingFragmentsHost.id, UserProfile())
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        addToBackStack(null)
                        commit()
                    }
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener true
                }
            }
        }
    }

    private fun hasLocationAccessPermission() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun hasSendSMSPermission() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        val permissionToRequest = mutableListOf<String>()
        if (!hasLocationAccessPermission()) {
            permissionToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if(!hasSendSMSPermission()){
            permissionToRequest.add(Manifest.permission.SEND_SMS)
        }
        if (permissionToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionToRequest.toTypedArray(), 200)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200 && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (i == PackageManager.PERMISSION_GRANTED) {
//                    Log.d("requestedPermission","${permissions[i]} granted")
                }
            }
        }
    }

    private fun checkGpsStatus() {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (gpsStatus) {
            Log.d("requestedPermission", "location enabled")
        } else {
            enableGPS()
        }
    }

    private fun enableGPS() {
        intent1 = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent1)
    }

    override fun onResume() {
        super.onResume()
        checkGpsStatus()
    }

    override fun onRestart() {
        super.onRestart()
    }
    private fun newLocationRequest() {
        locationRequest = LocationRequest.create().apply{
            interval = TimeUnit.SECONDS.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 2
            fastestInterval = TimeUnit.SECONDS.toMillis(1)
        }
        locationCallback = object:LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                currentLocation = p0.lastLocation
                lat = currentLocation!!.latitude
                lon = currentLocation!!.longitude
            }
        }
    }
}


