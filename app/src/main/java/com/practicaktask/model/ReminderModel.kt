package com.practicaktask.model

data class ReminderModel(

    var data:Int,
    var list: ArrayList<ReminderDetails>?,

    )
data class ReminderDetails(
    var name:String,
    var description:String,
    var date:String,
    var time:String
)
