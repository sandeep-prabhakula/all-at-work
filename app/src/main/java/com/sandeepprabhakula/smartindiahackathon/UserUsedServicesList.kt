package com.sandeepprabhakula.smartindiahackathon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.sandeepprabhakula.smartindiahackathon.daos.UserDao
import com.sandeepprabhakula.smartindiahackathon.daos.WorkersDao
import com.sandeepprabhakula.smartindiahackathon.models.User
import com.sandeepprabhakula.smartindiahackathon.models.Worker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserUsedServicesList : Fragment(), IComplete {
    private val userDao = UserDao()
    private lateinit var workerDao: WorkersDao
    private lateinit var adapter: UserUsedServicesAdapter
    private var services: ArrayList<Worker> = ArrayList()
    private val auth = FirebaseAuth.getInstance()
    private val firebaseUser = auth.currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_used_services_list, container, false)

        val userUsedServices: RecyclerView = view.findViewById(R.id.userUsedServices)
        userUsedServices.layoutManager = LinearLayoutManager(activity)
        GlobalScope.launch(Dispatchers.IO) {
            val user =
                userDao.getUserById(firebaseUser?.uid.toString()).await().toObject(User::class.java)
            services = user?.usedServices!!
            adapter = UserUsedServicesAdapter(services, this@UserUsedServicesList)
            withContext(Dispatchers.Main) {
                userUsedServices.adapter = adapter
            }
        }
        return view
    }

    override fun onClickOnComplete(aadharID: String, position: Int) {
        workerDao = WorkersDao()
        workerDao.updateTasksOfWorker(aadharID)
        GlobalScope.launch(Dispatchers.IO) {
            userDao.deleteUsedServices(position)
        }
    }
}