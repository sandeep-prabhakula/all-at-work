package com.sandeepprabhakula.smartindiahackathon

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.sandeepprabhakula.smartindiahackathon.daos.WorkersDao
import com.sandeepprabhakula.smartindiahackathon.models.Worker

class RegisterForServiceProvider : Fragment() {
    private val workersDao: WorkersDao = WorkersDao()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_register_for_service_provider, container, false)

        val newWorkerName: EditText = view.findViewById(R.id.newWorkerName)
        val newWorkerAge: EditText = view.findViewById(R.id.newWorkerAge)
        val newWorkerMobile: EditText = view.findViewById(R.id.newWorkerMobile)
        val newWorkerSkills: EditText = view.findViewById(R.id.newWorkerSkills)
        val newWorkerLocation: EditText = view.findViewById(R.id.newWorkerLocation)
        val registerWorker: Button = view.findViewById(R.id.registerWorker)
        val newWorkerPINCode: EditText = view.findViewById(R.id.newWorkerPINCode)
        val locationSpinner: Spinner = view.findViewById(R.id.locationSpinner)
        val skillsSpinner: Spinner = view.findViewById(R.id.skillsSpinner)
        val newWorkerAadhaerID: EditText = view.findViewById(R.id.newWorkerAadharID)
        val newWorkerAddress: EditText = view.findViewById(R.id.newWorkerAddress)
        val cities = resources.getStringArray(R.array.Cities)
        val skills = resources.getStringArray(R.array.works)
        if (locationSpinner != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, cities
            )
            locationSpinner.adapter = adapter

            locationSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    newWorkerLocation.setText(cities[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        if (skillsSpinner != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, skills
            )
            skillsSpinner.adapter = adapter

            skillsSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    newWorkerSkills.setText(skills[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        registerWorker.setOnClickListener {
            val name = newWorkerName.text.toString()
            val mobile = newWorkerMobile.text.toString()
            val age = newWorkerAge.text.toString()
            val location = newWorkerLocation.text.toString()
            val skillsTxt = newWorkerSkills.text.toString()
            val pinCode = newWorkerPINCode.text.toString()
            val aadharCardId = newWorkerAadhaerID.text.toString()
            val address = newWorkerAddress.text.toString()
            if (!TextUtils.isEmpty(name) &&
                !TextUtils.isEmpty(age) &&
                !TextUtils.isEmpty(mobile) && mobile.length == 10 &&
                !TextUtils.isEmpty(location) && location != "Select a city" &&
                !TextUtils.isEmpty(skillsTxt) &&
                !TextUtils.isEmpty(pinCode) &&
                !TextUtils.isEmpty(aadharCardId) &&
                aadharCardId.length == 16 &&
                !TextUtils.isEmpty(address)
            ) {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("Register Worker")
                alertDialog.setMessage("Once registered changes are not possible.Do you want to register?")
                alertDialog.setPositiveButton("Yes") { _, _ ->
                    workersDao.addWorker(
                        Worker(
                            name,
                            age,
                            mobile,
                            skillsTxt,
                            0,
                            location.lowercase(),
                            pinCode,
                            aadharCardId,
                            address
                        )
                    )
                    Toast.makeText(activity, "Registration Successful ", Toast.LENGTH_SHORT)
                        .show()
                    newWorkerAge.setText("")
                    newWorkerName.setText("")
                    newWorkerSkills.setText("")
                    newWorkerLocation.setText("")
                    newWorkerMobile.setText("")
                    newWorkerPINCode.setText("")
                    newWorkerAadhaerID.setText("")
                    newWorkerAddress.setText("")
                }
                alertDialog.setNegativeButton("No") { _, _ ->

                }
                alertDialog.show()
            } else {
                Toast.makeText(activity, "Empty credentials not accepted", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return view
    }
}