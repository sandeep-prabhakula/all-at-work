package com.sandeepprabhakula.smartindiahackathon

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.sandeepprabhakula.smartindiahackathon.daos.WorkersDao
import com.sandeepprabhakula.smartindiahackathon.models.Worker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterForServiceProvider : Fragment() {
    private val workersDao:WorkersDao = WorkersDao()
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
        registerWorker.setOnClickListener {
            val name = newWorkerName.text.toString()
            val mobile = newWorkerMobile.text.toString()
            val age = newWorkerAge.text.toString()
            val location = newWorkerLocation.text.toString()
            val skills = newWorkerSkills.text.toString()
            if (!TextUtils.isEmpty(name) &&
                !TextUtils.isEmpty(age) &&
                !TextUtils.isEmpty(mobile) && mobile.length==10 &&
                !TextUtils.isEmpty(location) &&
                !TextUtils.isEmpty(skills)
            ) {
                workersDao.addWorker(Worker(name,age,mobile,skills,0,location.lowercase()))
            } else {
                Toast.makeText(activity, "Empty credentials not accepted", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return view
    }
}