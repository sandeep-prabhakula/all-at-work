package com.sandeepprabhakula.smartindiahackathon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.sandeepprabhakula.smartindiahackathon.daos.UserDao
import com.sandeepprabhakula.smartindiahackathon.daos.WorkersDao
import com.sandeepprabhakula.smartindiahackathon.models.Worker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SearchFragment : Fragment(), ServiceRequest {
    private lateinit var workersDao: WorkersDao
    private lateinit var adapter: WorkersAdapter
    private lateinit var filteredWorkers: RecyclerView
    private lateinit var userDao: UserDao
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val mumbai: TextView = view.findViewById(R.id.mumbai)
        val hyderabad: TextView = view.findViewById(R.id.hyderabad)
        val chennai: TextView = view.findViewById(R.id.chennai)
        val bangalore: TextView = view.findViewById(R.id.bangalore)
        val kochin: TextView = view.findViewById(R.id.kochin)
        val delhi: TextView = view.findViewById(R.id.delhi)
        val kolkata: TextView = view.findViewById(R.id.kolkata)
        val jaipur: TextView = view.findViewById(R.id.jaipur)
        filteredWorkers = view.findViewById(R.id.filteredWorkers)
        mumbai.setOnClickListener {
            setupRecyclerView(mumbai.text.toString())
        }
        hyderabad.setOnClickListener {
            setupRecyclerView(hyderabad.text.toString())
        }
        chennai.setOnClickListener {
            setupRecyclerView(chennai.text.toString())
        }
        bangalore.setOnClickListener {
            setupRecyclerView(bangalore.text.toString())
        }
        kochin.setOnClickListener {
            setupRecyclerView(kochin.text.toString())
        }
        delhi.setOnClickListener {
            setupRecyclerView(delhi.text.toString())
        }
        kolkata.setOnClickListener {
            setupRecyclerView(kolkata.text.toString())
        }
        jaipur.setOnClickListener {
            setupRecyclerView(jaipur.text.toString())
        }
        return view
    }

    private fun setupRecyclerView(city:String) {
        workersDao = WorkersDao()
        val query =
            workersDao.workersCollection.whereEqualTo("workerLocation",city.lowercase()).orderBy("worksDoneThisWeek",Query.Direction.ASCENDING)
        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<Worker>().setQuery(query, Worker::class.java).build()
        filteredWorkers.layoutManager = LinearLayoutManager(activity)
        adapter = WorkersAdapter(recyclerViewOptions, this)
        filteredWorkers.adapter = adapter
        adapter.startListening()
    }

    override fun onServiceRequest(workerId: String) {
        userDao = UserDao()
        GlobalScope.launch(Dispatchers.IO) {
            val worker = workersDao.getWorkerById(workerId).await().toObject(Worker::class.java)
            if (worker != null) {
                userDao.addUsedServices(worker)
            }
        }
    }
}