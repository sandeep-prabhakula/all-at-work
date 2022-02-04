package com.sandeepprabhakula.smartindiahackathon

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if(user!=null){
            val intent = Intent(this,WorkingFragmentsHolder::class.java)
            startActivity(intent)
            finish()
        }
    }
}