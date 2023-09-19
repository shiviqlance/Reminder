package com.practicaktask.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.practicaktask.R
import com.practicaktask.adapter.ReminderAdapter
import com.practicaktask.databinding.ActivityMainBinding
import com.practicaktask.helper.Prefs
import com.practicaktask.model.ReminderDetails
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var pref: Prefs? = null
    var list =  ArrayList<ReminderDetails>()

    var reminderAdapter: ReminderAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initView()
        initObsver()
        initListner()

    }

    private fun initView() {

        binding?.llHeader?.txtTitle?.text = getString(R.string.reminderlit)
        binding?.llHeader?.imBack?.isVisible = false

        reminderAdapter = ReminderAdapter(this)
        binding.rlListOfEvent.adapter = reminderAdapter
    }

    private fun initObsver() {

        System.out.println("data print $list")

        reminderAdapter?.updateAll(list)
    }

    private fun initListner() {
        binding?.llHeader?.imBack?.setOnClickListener {
            finish()
        }

        binding?.llHeader?.imgAddReminder?.setOnClickListener {
            startActivity(Intent(this, AddReminderActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        pref = Prefs(this)

        getData()
    }

    private fun getData() {
        if (pref?.getReminderModel()?.list != null) {





            val mainHandler = Handler(Looper.getMainLooper())

            mainHandler.post(object : Runnable {
                override fun run() {
                    list.clear()
                    pref?.getReminderModel()?.list?.forEach {
                        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                        val strDate: Date = sdf.parse(it.date+" "+it.time)
                        if (System.currentTimeMillis() < strDate.time) {
                            list.add(it)
                        }
                    }
                    reminderAdapter?.updateAll(list)


                    mainHandler.postDelayed(this, 1000)
                }
            })


           //

        }

    }
}