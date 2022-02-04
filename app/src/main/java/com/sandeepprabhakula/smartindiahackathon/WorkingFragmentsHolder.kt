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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWorkingFragementsHolderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = applicationContext
        requestPermissions()
        checkGpsStatus()
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
        intent1 = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent1)
    }

    override fun onResume() {
        super.onResume()
        checkGpsStatus()
    }

    override fun onRestart() {
        super.onRestart()
    }
}


