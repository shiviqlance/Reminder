package com.practicaktask.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.practicaktask.model.ReminderDetails
import java.util.Calendar

class BootReceiver: BroadcastReceiver(){
    private var mCalendar: Calendar? = null
    private var mAlarmReceiver: AlarmReceiver? = null

    // Constant values in milliseconds
    private val milMinute = 60000L
    private val milHour = 3600000L
    private val milDay = 86400000L
    private val milWeek = 604800000L
    private val milMonth = 2592000000L


    private val mTitle: String? = null
    private var mTime: String? = null
    private var mDate: String? = null
    private var mRepeatNo: String? = null
    private var mRepeatType: String? = null
    private var mActive: String? = null
    private var mRepeat: String? = null
    private var mYear = 0
    private  var mMonth:Int = 0
    private  var mHour:Int = 0
    private  var mMinute:Int = 0
    private  var mDay:Int = 0

    var prefs: Prefs? = null


    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent!!.action == "android.intent.action.BOOT_COMPLETED") {
            prefs = context?.let { Prefs(it) }

            mCalendar = Calendar.getInstance()
            mAlarmReceiver = AlarmReceiver()
            val reminders: ArrayList<ReminderDetails>? = prefs?.getReminderModel()?.list
            for (rm in reminders!!) {

                mDate = rm.date
                mTime = rm.time
               var mDateSplit = mDate!!.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
               var mTimeSplit = mTime!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                mDay = mDateSplit[0].toInt()
                mMonth = mDateSplit[1].toInt()
                mYear = mDateSplit[2].toInt()
                mHour = mTimeSplit[0].toInt()
                mMinute = mTimeSplit[1].toInt()

                mCalendar?.set(Calendar.MONTH, --mMonth)
                mCalendar?.set(Calendar.YEAR, mYear)
                mCalendar?.set(Calendar.DAY_OF_MONTH, mDay)
                mCalendar?.set(Calendar.HOUR_OF_DAY, mHour)
                mCalendar?.set(Calendar.MINUTE, mMinute)
                mCalendar?.set(Calendar.SECOND, 0)

                // Cancel existing notification of the reminder by using its ID
                // mAlarmReceiver.cancelAlarm(context, mReceivedID);

                mAlarmReceiver!!.setAlarm(context!!, mCalendar!!,rm.name)
                // Create a new notification

            }
        }
    }
}