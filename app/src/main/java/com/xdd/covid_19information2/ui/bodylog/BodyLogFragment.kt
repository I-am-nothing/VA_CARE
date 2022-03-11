package com.xdd.covid_19information2.ui.bodylog

import android.R.attr
import android.R.attr.*
import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.InputFilter
import android.view.*
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.databinding.BodyLogDialogBinding
import com.xdd.covid_19information2.databinding.BodyLogFragmentBinding
import android.app.DatePickerDialog
import android.os.CancellationSignal
import android.text.Editable
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.util.Log
import android.widget.SimpleAdapter
import androidx.activity.addCallback
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.xdd.covid_19information2.ActionBar
import com.xdd.covid_19information2.MainActivity
import com.xdd.covid_19information2.adapter.tempurate.TemperatureAdapter
import com.xdd.covid_19information2.localdatabase.bodylog.Body
import com.xdd.covid_19information2.localdatabase.bodylog.BodyBody
import com.xdd.covid_19information2.localdatabase.bodylog.BodyLogHelper
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


class BodyLogFragment : Fragment() {

    private var _binding: BodyLogFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BodyLogViewModel

    private lateinit var bodyLogHelper: BodyLogHelper
    private lateinit var temperatureList: List<Body>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BodyLogFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(BodyLogViewModel::class.java)

        (requireActivity() as MainActivity).setActionBar(ActionBar.Adding, "Body Temperature Log")

        bodyLogHelper = BodyLogHelper.getInstance(binding.root.context)!!
        temperatureList = bodyLogHelper.getAllTemperature()

        val gridLayoutManager = GridLayoutManager(binding.root.context, 1, LinearLayoutManager.VERTICAL, false)
        binding.bodyRv.layoutManager = gridLayoutManager
        binding.bodyRv.adapter = TemperatureAdapter(binding.root.context, search(), this)

        (requireActivity() as MainActivity).toolbarAddingClick {
            openDialog()
        }

        binding.searchTb.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                binding.bodyRv.adapter = TemperatureAdapter(binding.root.context, search(), this@BodyLogFragment)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        return binding.root
    }

    fun onDelete(){
        temperatureList = bodyLogHelper.getAllTemperature()
        binding.bodyRv.adapter = TemperatureAdapter(binding.root.context, search(), this)

        Snackbar.make(binding.root, "Delete Success", Snackbar.LENGTH_SHORT).show()
    }

    private fun search(): List<BodyBody>{
        val arrayList = ArrayList<BodyBody>()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.TAIWAN)
        val dateOutFormat = SimpleDateFormat("yyyy年M月d日 EE a hh:mm", Locale.TAIWAN)
        val calender = Calendar.getInstance()

        for(body in temperatureList){
            calender.time = dateFormat.parse(body.datetime)!!

            if(binding.searchTb.text.toString() == "" ||dateOutFormat.format(calender.time).contains(binding.searchTb.text.toString())){
                arrayList.add(BodyBody(body.datetime, dateOutFormat.format(calender.time), body.temperature))
            }
        }

        return arrayList
    }

    private fun openDialog(){
        var date = "${Calendar.getInstance().get(Calendar.YEAR)}/${Calendar.getInstance().get(Calendar.MONTH)+1}/${Calendar.getInstance().get(Calendar.DAY_OF_MONTH)}"
        val view = View.inflate(binding.root.context, R.layout.body_log_dialog, null)
        val binding = BodyLogDialogBinding.bind(view)
        val dialog = AlertDialog.Builder(binding.root.context)
            .setView(binding.root)
            .create()

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val dateNow = SimpleDateFormat("MMM dd, 20 yy", Locale.US)
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val minute = Calendar.getInstance().get(Calendar.MINUTE)

        binding.dateTb.setText(dateNow.format(Calendar.getInstance().time))
        binding.minTb.setText(minute.toString())

        if(hour < 12){
            binding.hourTb.setText(hour.toString())
            binding.amBtn.isEnabled = false
            binding.pmBtn.isEnabled = true
        }
        else{
            if(hour != 12){
                binding.hourTb.setText((hour-12).toString())
            }
            binding.amBtn.isEnabled = true
            binding.pmBtn.isEnabled = false
        }

        binding.hourTb.filters = arrayOf<InputFilter>(LengthFilter(2))
        binding.minTb.filters = arrayOf<InputFilter>(LengthFilter(2))

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val format = SimpleDateFormat("yyyy/MM/dd", Locale.US)
            date = "$year/$month/$dayOfMonth"
            val dateOk = format.parse(date)
            binding.dateTb.setText(dateNow.format(dateOk!!))
        }
        binding.dateTb.setOnClickListener {
            val dateDialog = DatePickerDialog(view.context, dateSetListener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH)+1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            dateDialog.show()
        }

        binding.amBtn.setOnClickListener {
            binding.amBtn.isEnabled = false
            binding.pmBtn.isEnabled = true
        }

        binding.pmBtn.setOnClickListener {
            binding.amBtn.isEnabled = true
            binding.pmBtn.isEnabled = false
        }

        binding.cancelBtn.setOnClickListener{
            dialog.dismiss()
        }

        binding.continueBtn.setOnClickListener {
            val time = if((binding.hourTb.text.toString().toInt() < 12 && binding.pmBtn.isEnabled) || (binding.hourTb.text.toString().toInt() == 12 && binding.amBtn.isEnabled)){
                "${binding.hourTb.text}:${binding.minTb.text}"
            }
            else if(binding.hourTb.text.toString().toInt() < 12 && binding.amBtn.isEnabled){
                "${binding.hourTb.text.toString().toInt() + 12}:${binding.minTb.text}"
            }
            else{
                Snackbar.make(binding.root, "Invalid Time, please check of your time input.", Snackbar.LENGTH_SHORT).show()
                ""
            }

            if(time != ""){
                val datetime = "$date $time"
                val databaseHelper = BodyLogHelper.getInstance(binding.root.context)

                databaseHelper?.addTemperature(Body(datetime, binding.temperatureSb.progress/10.0f))
            }

            temperatureList = bodyLogHelper.getAllTemperature()
            this.binding.searchTb.setText("")
            this.binding.bodyRv.adapter = TemperatureAdapter(binding.root.context, search(), this)
            Snackbar.make(this.binding.root, "Add Success", Snackbar.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        binding.temperatureSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                binding.temperatureTv.text = (progress/10.0f).toString() + " °C"
                binding.temperatureTv.x = binding.temperatureSb.thumb.bounds.exactCenterX()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }
}