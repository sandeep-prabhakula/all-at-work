package com.sandeepprabhakula.smartindiahackathon

import android.app.AlertDialog
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.Query
import com.sandeepprabhakula.smartindiahackathon.daos.UserDao
import com.sandeepprabhakula.smartindiahackathon.daos.WorkersDao
import com.sandeepprabhakula.smartindiahackathon.models.Worker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ListServicesFragment : Fragment(), ServiceRequest {
    private lateinit var services: RecyclerView
    private lateinit var workersDao: WorkersDao
    private lateinit var adapter: WorkersAdapter
    private lateinit var userDao: UserDao
    private val msg: String = "Need your services. Are you available for providing us the services"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_services, container, false)
        services = view.findViewById(R.id.services)
        val filters: FloatingActionButton = view.findViewById(R.id.filterFAB)
        filters.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Choose Occupation type")
            val options = arrayOf(
                "Construction",
                "Sanitation and Cleaning",
                "Electrical works",
                "Plumbing",
                "Carpentry",
                "Labour"
            )
            alertDialog.setSingleChoiceItems(options, 0) { _, i ->
                when (options[i]) {
                    "Construction" -> {
                        setupFilter("Construction")
                    }
                    "Sanitation and Cleaning" -> {
                        setupFilter("Sanitation and Cleaning")
                    }
                    "Electrical works" -> {
                        setupFilter("Electrical works")
                    }
                    "Plumbing" -> {
                        setupFilter("Plumbing")
                    }
                    "Carpentry" -> {
                        setupFilter("Carpentry")
                    }
                    "Labour" -> {
                        setupFilter("Labour")
                    }
                }
            }
            alertDialog.show()
        }
        setUpRecyclerView()
        return view
    }

    private fun setUpRecyclerView() {
        workersDao = WorkersDao()
        val query =
            workersDao.workersCollection.orderBy("worksDoneThisWeek", Query.Direction.ASCENDING)
        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<Worker>().setQuery(query, Worker::class.java).build()
        services.layoutManager = LinearLayoutManager(activity)
        adapter = WorkersAdapter(recyclerViewOptions, this)
        services.adapter = adapter
    }

    private fun setupFilter(profession: String) {
        workersDao = WorkersDao()
        val query =
            workersDao.workersCollection.whereEqualTo("workerSkills", profession)
                .orderBy("worksDoneThisWeek")
        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<Worker>().setQuery(query, Worker::class.java).build()
        services.layoutManager = LinearLayoutManager(activity)
        adapter = WorkersAdapter(recyclerViewOptions, this)
        services.adapter = adapter
        adapter.startListening()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }

    override fun onServiceRequest(workerId: String) {
        userDao = UserDao()
        val alert = AlertDialog.Builder(activity)
        alert.setTitle("Requested a Service")
        alert.setMessage("do you want to notify the worker with through the SMS ?")
        alert.setPositiveButton("YES") { _, _ ->
            CoroutineScope(Dispatchers.IO).launch {
                val worker = workersDao.getWorkerById(workerId).await().toObject(Worker::class.java)
                if (worker != null) {
                    userDao.addUsedServices(worker)
                    val smsManager: SmsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(worker.workerMobile, null, msg, null, null)
                }
            }
        }
        alert.setNegativeButton("Dismiss") { _, _ ->

        }
        alert.show()
    }
}