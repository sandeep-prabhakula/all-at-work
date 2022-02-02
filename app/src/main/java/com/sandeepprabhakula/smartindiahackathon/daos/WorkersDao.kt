package com.sandeepprabhakula.smartindiahackathon.daos

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.sandeepprabhakula.smartindiahackathon.models.Worker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WorkersDao {
    val db = FirebaseFirestore.getInstance()
    val workersCollection = db.collection("workers")

    fun addWorker(worker: Worker?) {
        worker?.let {
            GlobalScope.launch(Dispatchers.IO) {
                workersCollection.document(worker.workerMobile).set(it)
            }
        }
    }

    fun getWorkerById(mobile: String): Task<DocumentSnapshot> {
        return workersCollection.document(mobile).get()
    }

    fun updateTasksOfWorker(mobile: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val worker = getWorkerById(mobile).await().toObject(Worker::class.java)
            worker?.worksDoneThisWeek = worker?.worksDoneThisWeek?.plus(1)!!
            workersCollection.document(mobile).set(worker)
        }
    }
}