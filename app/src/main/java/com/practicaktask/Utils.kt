package com.practicaktask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {
    fun isEmptyEditTextNew(edt: EditText, msg: String): Boolean {
        return if (edt.text.toString().isEmpty()) {
            edt.error = msg
            edt.requestFocus()
            true
        } else {
            false
        }
        //return false
    }

    fun selectDate(
        edittext: EditText,
        context: Context,
        format: String,
        minAllowed: Boolean,
        maxAllowed: Boolean,
    ) {
        val c = Calendar.getInstance()
        val mYear = c[Calendar.YEAR]
        val mMonth = c[Calendar.MONTH]
        val mDay = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            context,
            R.style.DialogTheme,
            { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val calendar = Calendar.getInstance()
                calendar[year, monthOfYear] = dayOfMonth

                val s = getDate(year, monthOfYear, dayOfMonth, format)
                edittext.setText(s)
            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(context, R.color.black))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEUTRAL)
            .setTextColor(ContextCompat.getColor(context, R.color.black))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(context, R.color.black))
        if (minAllowed) datePickerDialog.datePicker.minDate = Date().time
        if (maxAllowed) datePickerDialog.datePicker.maxDate = Date().time

    }

    private fun getDate(year: Int, monthOfYear: Int, dayOfMonth: Int, format: String): String {
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar[year, monthOfYear] = dayOfMonth
        return dateFormatter.format(calendar.time)
    }
    fun selectTime(txtTime: EditText, context: Context) {
        var hrsFormatter = 0
        var minFormatter = 0
        if (!txtTime.text.toString().isNullOrEmpty()) {
            hrsFormatter = txtTime.text.toString().substringBefore(":").trim().toInt()
            minFormatter = txtTime.text.toString().substringAfter(":").substring(0, 2).toInt()
        }
        val timePicker = TimePickerDialog(
            context, R.style.DialogTheme,
            // listener to perform task
            // when time is picked
            { view, hourOfDay, minute ->
                val formattedTime: String = when {
                    hourOfDay == 0 -> {
                        if (minute < 10) {
                            "${hourOfDay + 12}:0${minute} AM"
                        } else {
                            "${hourOfDay + 12}:${minute} AM"
                        }
                    }
                    hourOfDay > 12 -> {
                        if (minute < 10) {
                            "${hourOfDay - 12}:0${minute} PM"
                        } else {
                            "${hourOfDay - 12}:${minute} PM"
                        }
                    }
                    hourOfDay == 12 -> {
                        if (minute < 10) {
                            "${hourOfDay}:0${minute} PM"
                        } else {
                            "${hourOfDay}:${minute} PM"
                        }
                    }
                    else -> {
                        if (minute < 10) {
                            "${hourOfDay}:0${minute} AM"
                        } else {
                            "${hourOfDay}:${minute} AM"
                        }
                    }
                }
                var time = ""
                time = if (minute < 10) {
                    "${hourOfDay}:0${minute}"
                } else {
                    "${hourOfDay}:${minute} "
                }

                txtTime.setText(time)
            },
            // default hour when the time picker
            // dialog is opened
            hrsFormatter,
            // default minute when the time picker
            // dialog is opened
            minFormatter, false
        )
        // then after building the timepicker
        // dialog show the dialog to user
        timePicker.show()

        timePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(
            ContextCompat.getColor(
                context, R.color.black
            )
        )
        timePicker.getButton(DatePickerDialog.BUTTON_NEUTRAL).setTextColor(
            ContextCompat.getColor(
                context, R.color.black
            )
        )
        timePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(
            ContextCompat.getColor(
                context, R.color.black
            )
        )
    }

}