package com.practicaktask.helper

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.legacy.content.WakefulBroadcastReceiver
import com.practicaktask.ui.MainActivity
import com.practicaktask.R
import com.practicaktask.model.ReminderModel
import java.util.Calendar

class AlarmReceiver: WakefulBroadcastReceiver() {
    var mAlarmManager: AlarmManager? = null
    var mPendingIntent: PendingIntent? = null
    var prefs: Prefs? = null
    override fun onReceive(context: Context?, intent: Intent?) {

        val mReceivedID = intent!!.getStringExtra("EXTRA_REMINDER_ID")
        prefs = context?.let { Prefs(it) }
        // Get notification title from Reminder Database

        // Get notification title from Reminder Database
        val reminder: ReminderModel? = prefs?.getReminderModel()
        var mTitle: String = ""
        reminder?.list?.forEach {
            if (it.name.equals(mReceivedID)){
                mTitle = it.name
            }
        }


        // Create intent to open ReminderEditActivity on notification click

        // Create intent to open ReminderEditActivity on notification click
        val editIntent = Intent(context, MainActivity::class.java)
        editIntent.putExtra("EXTRA_REMINDER_ID", mReceivedID.toString())
        val mClick = PendingIntent.getActivity(
            context,
            1,
            editIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Create Notification

        // Create Notification
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(context!!)
            .setLargeIcon(BitmapFactory.decodeResource(context!!.resources, R.mipmap.ic_launcher))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context!!.resources.getString(R.string.app_name))
            .setTicker(mTitle)
            .setContentText(mTitle)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(mClick)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)

        val nManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nManager.notify(1, mBuilder.build())
    }

    fun setAlarm(context: Context, calendar: Calendar, name: String) {
        mAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Put Reminder ID in Intent Extra
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("EXTRA_REMINDER_ID", name.toString())
        mPendingIntent =
            PendingIntent.getBroadcast(context, 1, intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // Calculate notification time
        val c = Calendar.getInstance()
        val currentTime = c.timeInMillis
        val diffTime = calendar.timeInMillis - currentTime

        // Start alarm using notification time
        mAlarmManager!![AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + diffTime] =
            mPendingIntent

        // Restart alarm if device is rebooted
        val receiver = ComponentName(context, BootReceiver::class.java)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }
    fun cancelAlarm(context: Context, ID: Int) {
        mAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Cancel Alarm using Reminder ID
        mPendingIntent = PendingIntent.getBroadcast(context, ID, Intent(context, AlarmReceiver::class.java),PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE )
        mAlarmManager!!.cancel(mPendingIntent)

        // Disable alarm
        val receiver = ComponentName(context, BootReceiver::class.java)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}