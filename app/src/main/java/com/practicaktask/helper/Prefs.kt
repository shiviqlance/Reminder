package com.practicaktask.helper

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicaktask.model.ReminderModel


class Prefs(context: Context) {
    val PREFS_FILENAME = context.packageName + ".prefs"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    fun clear() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    fun setStringValue(key: String, value: String?) {
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringValue(key: String): String? {
        return prefs.getString(key, "")
    }


    fun setReminderModel(obj: ReminderModel) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(obj)
        editor.putString("Reminder", json)
        editor.apply()
    }

    fun getReminderModel(): ReminderModel {
        val gson = Gson()
        val json = prefs.getString("Reminder", "")
        return if (json.isNullOrEmpty())
            ReminderModel(0, null)
        else
            gson.fromJson<ReminderModel>(json, ReminderModel::class.java!!)
    }

}