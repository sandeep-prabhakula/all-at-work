package com.sandeepprabhakula.smartindiahackathon.daos

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.sandeepprabhakula.smartindiahackathon.models.User
import com.sandeepprabhakula.smartindiahackathon.models.Worker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserDao {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")
    private val auth = FirebaseAuth.getInstance()
    private val firebaseUser = auth.currentUser
    fun addUsers(user: User?) {
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                usersCollection.document(user.uid).set(it)
            }
        }
    }

    fun getUserById(uid: String): Task<DocumentSnapshot> {
        return usersCollection.document(uid).get()
    }

    fun addUsedServices(worker: Worker) {
        GlobalScope.launch(Dispatchers.IO) {
            val user = getUserById(firebaseUser?.uid.toString()).await().toObject(User::class.java)
            user?.usedServices?.add(worker)
            usersCollection.document(firebaseUser?.uid.toString()).set(user!!)
        }
    }

    fun deleteUsedServices(position: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val user = getUserById(firebaseUser?.uid.toString()).await().toObject(User::class.java)
            user?.usedServices?.removeAt(position)
            usersCollection.document(firebaseUser?.uid.toString()).set(user!!)
        }
    }

    fun deleteUser() {
        GlobalScope.launch(Dispatchers.IO) {
            auth.signOut()
            usersCollection.document(firebaseUser?.uid.toString()).delete().await()
        }
    }

    fun updateDetails(mobile:String,address:String){
        GlobalScope.launch(Dispatchers.IO){
            val user = getUserById(firebaseUser?.uid.toString()).await().toObject(User::class.java)
            if(address.isNotBlank()){
                user?.userAddress = address
            }
            if(mobile.isNotBlank()){
                user?.userMobile = mobile
            }
            usersCollection.document(firebaseUser?.uid.toString()).set(user!!)
        }
    }
}