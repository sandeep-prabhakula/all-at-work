package com.sandeepprabhakula.smartindiahackathon.models

data class User(
    var uid:String = "",
    var userName:String = "",
    var userEmail:String = "",
    var userImage:String = "",
    var userMobile:String = "",
    var userAddress:String = "",
    var usedServices:ArrayList<Worker> = ArrayList()
)
