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
import com.xdd.covid_19information2.adapter.tempurate.Temperature
import com.xdd.covid_19information2.adapter.tempurate.TemperatureAdapter
import com.xdd.covid_19information2.localdatabase.bodylog.Body
import com.xdd.covid_19information2.localdatabase.bodylog.BodyBody
import com.xdd.covid_19information2.localdatabase.bodylog.BodyLogHelper
import com.xdd.taiwancovid_19informationfinal.dialog.TemperatureDialog
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.streams.toList


class BodyLogFragment : Fragment() {

    private var _binding: BodyLogFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BodyLogViewModel
    private lateinit var temperatureHelper: BodyLogHelper

    private val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US)
    private val dateFormat2 = SimpleDateFormat("yyyy年M月d日 EE a h:mm", Locale.TAIWAN)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BodyLogFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(BodyLogViewModel::class.java)

        (requireActivity() as MainActivity).apply {
            setActionBar(ActionBar.Adding, "體溫紀錄")
            toolbarAddingClick {
                TemperatureDialog(this@BodyLogFragment.binding.root.context, null).onConfirmClick {
                    search("")
                    Snackbar.make(this@BodyLogFragment.binding.root, "新增成功", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.apply {
            temperatureHelper = BodyLogHelper.getInstance(root.context)

            temperatureRv.layoutManager = GridLayoutManager(root.context, 1, LinearLayoutManager.VERTICAL, false)

            search("")

            searchTbe.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    search(searchTbe.text.toString())
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }

        return binding.root
    }

    private fun search(like: String){
        val list = temperatureHelper.getTemperatures().stream().filter {
            dateFormat2.format(dateFormat.parse(it.datetime)!!).contains(like) || it.temperature.toString().contains(like)
        }.toList() as ArrayList<Temperature>

        binding.apply {
            temperatureRv.adapter = TemperatureAdapter(root.context, list)
        }
    }
}