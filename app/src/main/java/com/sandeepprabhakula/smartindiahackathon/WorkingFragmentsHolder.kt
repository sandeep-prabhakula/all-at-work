package com.sandeepprabhakula.smartindiahackathon

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.sandeepprabhakula.smartindiahackathon.databinding.ActivityWorkingFragementsHolderBinding

class WorkingFragmentsHolder : AppCompatActivity() {

    private lateinit var context: Context
    private lateinit var locationManager: LocationManager
    var intent1: Intent? = null
    private var gpsStatus = false

    companion object {
        private lateinit var flpc: FusedLocationProviderClient
        var lat: String = ""
        var lon: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWorkingFragementsHolderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = applicationContext
        requestPermissions()
        checkGpsStatus()
        getLocationCoordinates()
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

    private fun hasBackgroundPermission() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            TODO("VERSION.SDK_INT < Q")
        }

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
        if (!hasBackgroundPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissionToRequest.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
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
        intent1 = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent1)
    }

    private fun getLocationCoordinates() {
        flpc = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        flpc.lastLocation.addOnSuccessListener {
            if (it == null) {
                newLocationRequest()
            } else {
                lat = it.latitude.toString()
                lon = it.longitude.toString()
            }
        }
    }

    private fun newLocationRequest() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 1000
        locationRequest.numUpdates = 1
        locationRequest.fastestInterval = 0
    }

    override fun onResume() {
        super.onResume()
        checkGpsStatus()
    }

    override fun onRestart() {
        super.onRestart()
    }
}


