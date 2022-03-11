package com.xdd.taiwancovid_19informationfinal.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.text.InputFilter
import android.view.View
import android.widget.DatePicker
import android.widget.SeekBar
import com.google.android.material.snackbar.Snackbar
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.adapter.tempurate.Temperature
import com.xdd.covid_19information2.databinding.BodyLogDialogBinding
import com.xdd.covid_19information2.localdatabase.bodylog.BodyLogHelper
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class TemperatureDialog(context: Context, private var temperature: Temperature?) {
    private val view = View.inflate(context, R.layout.body_log_dialog, null)
    private val binding = BodyLogDialogBinding.bind(view)
    private val dialog = AlertDialog.Builder(binding.root.context).setView(binding.root).create()
    private val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US)
    private val dateFormat2 = SimpleDateFormat("MMM d, 20, yy", Locale.US)
    private val calendar = Calendar.getInstance()

    init {
        calendar.isLenient = false
        dialog.apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            show()
        }

        binding.apply {

            val datePick = DatePickerDialog.OnDateSetListener { p0, p1, p2, p3 ->
                calendar.apply {
                    set(Calendar.YEAR, p1)
                    set(Calendar.MONTH, p2)
                    set(Calendar.DAY_OF_MONTH, p3)
                    dateTb.setText(dateFormat2.format(time))
                }
            }

            if(temperature == null){
                titleTv.text = "新增體溫紀錄"
                confirmBtn.text = "新增"
                temperature = Temperature()
            }
            else{
                calendar.time = dateFormat.parse(temperature!!.datetime)!!
                titleTv.text = "編輯體溫紀錄"
                confirmBtn.text = "編輯"
            }
            hourTb.filters = arrayOf(InputFilter.LengthFilter(2))
            minuteTb.filters = arrayOf(InputFilter.LengthFilter(2))

            dateTb.setText(dateFormat2.format(calendar.time))
            hourTb.setText(calendar.get(Calendar.HOUR).toString())
            minuteTb.setText(calendar.get(Calendar.MINUTE).toString())

            if(calendar.get(Calendar.AM_PM) == 0){
                amBtn.isEnabled = false
                pmBtn.isEnabled = true
            }
            else{
                amBtn.isEnabled = true
                pmBtn.isEnabled = false
            }

            temperatureSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    thumbTv.text = (p1 / 10f).toString() + " °C"
                    thumbTv.x = p0!!.thumb.bounds.exactCenterX()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }

            })

            dateTb.setOnClickListener {
                DatePickerDialog(root.context, datePick, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            amBtn.setOnClickListener {
                amBtn.isEnabled = false
                pmBtn.isEnabled = true
            }

            pmBtn.setOnClickListener {
                pmBtn.isEnabled = false
                amBtn.isEnabled = true
            }

            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    fun onConfirmClick(ok: (Temperature) -> Unit){
        binding.apply {
            confirmBtn.setOnClickListener {
                try{
                    calendar.apply {
                        time = dateFormat2.parse(dateTb.text.toString())!!
                        set(Calendar.AM_PM, if(amBtn.isEnabled) 1 else 0)
                        set(Calendar.HOUR, hourTb.text.toString().toInt())
                        set(Calendar.MINUTE, minuteTb.text.toString().toInt())
                        temperature!!.datetime = dateFormat.format(time)
                    }

                    temperature!!.temperature = temperatureSb.progress / 10f

                    val temperatureHelper = BodyLogHelper.getInstance(root.context)
                    if(temperature!!.id == null){
                        temperatureHelper.addTemperature(temperature!!)
                        dialog.dismiss()
                        ok(temperature!!)
                    }
                    else{
                        temperatureHelper.updateTemperature(temperature!!)
                        dialog.dismiss()
                        ok(temperature!!)
                    }
                }
                catch (e: Exception){
                    Snackbar.make(root, "請輸入有效的時間", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}