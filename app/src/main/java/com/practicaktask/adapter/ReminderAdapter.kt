package com.practicaktask.adapter

import android.content.Context
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicaktask.R
import com.practicaktask.databinding.RowListOfReminderBinding
import com.practicaktask.helper.BaseRecyclerViewAdapter
import com.practicaktask.model.ReminderDetails
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToLong


open class ReminderAdapter(val context: Context) : BaseRecyclerViewAdapter<ReminderDetails>(context) {
    private var selectedPosition = -1
    override fun getViewHolder(view: View): RecyclerView.ViewHolder {
        return CustomViewHolder(view)
    }

    override fun getView(): Int {
        return R.layout.row_list_of_reminder
    }

    override fun setData(holder: RecyclerView.ViewHolder, data: ReminderDetails, position: Int) {
        val customViewHolder = holder as CustomViewHolder
        customViewHolder.bindData(data = data, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_list_of_reminder, parent, false)
        return getViewHolder(view)
    }

    open inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var binding: RowListOfReminderBinding? = null

        init {
            binding = DataBindingUtil.bind<RowListOfReminderBinding>(view)
        }

        fun bindData(data: ReminderDetails, position: Int) {





            binding?.txtName?.text ="Name: "+ data.name.toString()
            binding?.txtDescription?.text ="Description: "+ data.description.toString()
            binding?.txtReminderDateTime?.text = "Reminder DateTime: "+data.date.toString()+" "+data.time.toString()

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val strDate: Date = sdf.parse(data.date+" "+data.time)
            val currentDate = sdf.format(Date())

            var diffrent  =strDate.time -  Date().time

            var mCountDownTimer = object : CountDownTimer(diffrent, 1000) {
                var time = StringBuilder()
                override fun onFinish() {

                    binding?.tvTimer?.text = "Reminder Time :"+ DateUtils.formatElapsedTime(0)
                    //mTextView.setText("Times Up!");
                }

                override fun onTick(millisUntilFinished: Long) {
                    var millisUntilFinished = millisUntilFinished
                    time.setLength(0)
                    // Use days if appropriate
                    if (millisUntilFinished > DateUtils.DAY_IN_MILLIS) {
                        val count = millisUntilFinished / DateUtils.DAY_IN_MILLIS
                        if (count > 1) time.append(count).append(" days ") else time.append(count)
                            .append(" day ")
                        millisUntilFinished %= DateUtils.DAY_IN_MILLIS
                    }
                    time.append(DateUtils.formatElapsedTime((millisUntilFinished / 1000.0).roundToLong()))
                    binding?.tvTimer?.text = "Reminder Time : $time"
                }
            }.start()
        }

    }

}