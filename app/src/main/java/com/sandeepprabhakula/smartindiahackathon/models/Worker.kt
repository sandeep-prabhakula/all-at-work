package com.sandeepprabhakula.smartindiahackathon.models

data class Worker(
    var workerName:String = "",
    var workerAge:String ="",
    var workerMobile:String = "",
    var workerSkills:String = "" ,
    var worksDoneThisWeek:Int = 0,
    var workerLocation:String=""
)
