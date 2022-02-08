package com.sandeepprabhakula.smartindiahackathon

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.sandeepprabhakula.smartindiahackathon.daos.UserDao

class AddMoreDetails : Fragment() {
    companion object{
        var lat: Double = 17.3850
        var lon: Double = 78.4867
    }
    private lateinit var userDao: UserDao


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        val useCurrentLocation: TextView = view.findViewById(R.id.useCurrentLocation)
        val address: EditText = view.findViewById(R.id.userAddress)
        val mobile: EditText = view.findViewById(R.id.userMobileNumber)
        val saveChanges: Button = view.findViewById(R.id.saveChanges)
        val pinCode: EditText = view.findViewById(R.id.userPINCode)
        userDao = UserDao()
        saveChanges.setOnClickListener {
            if (mobile.text.isBlank() && address.text.isBlank() && pinCode.text.isBlank())
                Toast.makeText(activity, "empty credentials not accepted", Toast.LENGTH_SHORT)
                    .show()
            else {
                userDao.updateDetails(
                    mobile.text.toString(),
                    address.text.toString(),
                    pinCode.text.toString()
                )
            }
            Toast.makeText(activity, "changes saved successfully", Toast.LENGTH_SHORT).show()
            mobile.setText("")
            address.setText("")
            pinCode.setText("")
        }
        useCurrentLocation.setOnClickListener {
            val geocoder = Geocoder(activity)
            val list: MutableList<Address> = geocoder.getFromLocation(
                lat,
                lon,
                1
            )
            address.setText(list[0].locality)
            pinCode.setText(list[0].postalCode)
        }
        return view
    }
}