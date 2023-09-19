package com.practicaktask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.core.view.isVisible
import com.practicaktask.R
import com.practicaktask.model.ReminderDetails
import com.practicaktask.model.ReminderModel
import com.practicaktask.Utils
import com.practicaktask.databinding.ActivityAddReminderBinding
import com.practicaktask.helper.Prefs

class AddReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddReminderBinding
    var list =  ArrayList<ReminderDetails>()

    var prefs : Prefs? = null
    var reminderModel: ReminderModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = Prefs(this)

        initView()
        initObserver()
        initListner()
    }

    private fun initView() {

        binding?.llHeader?.imgAddReminder?.isVisible = false
        binding?.llHeader?.txtTitle?.text = getString(R.string.addReminderDetails)

        reminderModel =  prefs?.getReminderModel()

        if (prefs?.getReminderModel()?.data != 0) {
            list = reminderModel!!.list!!
        }

    }

    private fun initObserver() {


    }

    private fun initListner() {

        binding?.llHeader?.imBack?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding?.edtDateTime?.setOnClickListener {
            Utils.selectDate(binding?.edtDateTime as EditText, this, "dd/MM/yyyy", true, false)
        }

        binding?.edtTime?.setOnClickListener {
            Utils.selectTime(binding?.edtTime as EditText, this)
        }

        binding.btnSubmit.setOnClickListener {

            when(!Utils.isEmptyEditTextNew(
                binding.edtName,
                resources.getString(R.string.pleaseenterName)
            )
                    &&!Utils.isEmptyEditTextNew(
                binding.edtDesciption,
                resources.getString(R.string.pleaseenterDesciption)
            )
                    &&!Utils.isEmptyEditTextNew(
                binding.edtDateTime,
                resources.getString(R.string.pleaseenterDate)
            )
                    &&!Utils.isEmptyEditTextNew(
                binding.edtTime,
                resources.getString(R.string.pleaseenterTime)
            )
            ){
                true->{

                   var name = binding?.edtName?.text.toString()
                   var descriptor = binding?.edtDesciption?.text.toString()
                   var strdata = binding?.edtDateTime?.text.toString()
                   var time = binding?.edtTime?.text.toString()

                    var reminderDetails = ReminderDetails(name,descriptor,strdata,time)


                     list.add(reminderDetails)

                    var reminderModel = ReminderModel(1,list)


                    reminderModel?.let { it1 -> prefs?.setReminderModel(it1) }

                    onBackPressedDispatcher.onBackPressed()
                }

                else -> {

                }
            }



        }



    }
}