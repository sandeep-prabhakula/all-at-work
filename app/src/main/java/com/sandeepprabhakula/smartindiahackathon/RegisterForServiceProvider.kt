package com.sandeepprabhakula.smartindiahackathon

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
        val spinner: Spinner = view.findViewById(R.id.locationSpinner)
        val cities = resources.getStringArray(R.array.Cities)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, cities
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
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

        registerWorker.setOnClickListener {
            val name = newWorkerName.text.toString()
            val mobile = newWorkerMobile.text.toString()
            val age = newWorkerAge.text.toString()
            val location = newWorkerLocation.text.toString()
            val skills = newWorkerSkills.text.toString()
            val pinCode = newWorkerPINCode.text.toString()
            if (!TextUtils.isEmpty(name) &&
                !TextUtils.isEmpty(age) &&
                !TextUtils.isEmpty(mobile) && mobile.length == 10 &&
                !TextUtils.isEmpty(location) && location != "Select a city" &&
                !TextUtils.isEmpty(skills) &&
                !TextUtils.isEmpty(pinCode)
            ) {
                workersDao.addWorker(
                    Worker(
                        name,
                        age,
                        mobile,
                        skills,
                        0,
                        location.lowercase(),
                        pinCode
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
            } else {
                Toast.makeText(activity, "Empty credentials not accepted", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return view
    }
}