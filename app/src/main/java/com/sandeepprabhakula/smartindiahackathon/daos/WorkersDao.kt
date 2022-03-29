package com.sandeepprabhakula.smartindiahackathon.daos

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.sandeepprabhakula.smartindiahackathon.models.Worker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WorkersDao {
    val db = FirebaseFirestore.getInstance()
    val workersCollection = db.collection("workers")

    fun addWorker(worker: Worker?) {
        worker?.let {
            CoroutineScope(Dispatchers.IO).launch {
                workersCollection.document(worker.workerAadharID).set(it)
            }
        }
    }

    fun getWorkerById(aadharID: String): Task<DocumentSnapshot> {
        return workersCollection.document(aadharID).get()
    }

    fun updateTasksOfWorker(aadharId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val worker = getWorkerById(aadharId).await().toObject(Worker::class.java)
            worker?.worksDoneThisWeek = worker?.worksDoneThisWeek?.plus(1)!!
            workersCollection.document(aadharId).set(worker)
        }
    }
}