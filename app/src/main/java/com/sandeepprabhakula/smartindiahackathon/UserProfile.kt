package com.sandeepprabhakula.smartindiahackathon

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.sandeepprabhakula.smartindiahackathon.daos.UserDao

class UserProfile : Fragment() {
    private lateinit var userDao: UserDao
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        val displayPicture: ImageView = view.findViewById(R.id.displayPicture)
        val usedServices: TextView = view.findViewById(R.id.usedServicesList)
        val logout: TextView = view.findViewById(R.id.logout)
        val delete: TextView = view.findViewById(R.id.deleteAccount)
        val userProfileName: TextView = view.findViewById(R.id.userProfileName)
        val userProfileEmail: TextView = view.findViewById(R.id.userProfileEmail)
        val addMoreDetails:TextView = view.findViewById(R.id.addMoreDetails)
        addMoreDetails.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.workingFragmentsHost, AddMoreDetails())
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                addToBackStack(null)
                commit()
            }
        }
        userProfileName.text = user?.displayName
        userProfileEmail.text = user?.email
        Glide.with(displayPicture.context).load(user?.photoUrl).circleCrop().into(displayPicture)
        usedServices.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.workingFragmentsHost, UserUsedServicesList())
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                addToBackStack(null)
                commit()
            }
        }
        logout.setOnClickListener {
            if (user != null) {
                auth.signOut()
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }
        }
        delete.setOnClickListener {
            user?.delete()
            userDao = UserDao()
            userDao.deleteUser()
            startActivity(Intent(context, MainActivity::class.java))
            activity?.finish()
            Toast.makeText(activity, "Account deletion successful", Toast.LENGTH_SHORT).show()
        }
        return view
    }
}