package com.xdd.covid_19information2.adapter.tempurate

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.databinding.ConfirmDialogBinding
import com.xdd.covid_19information2.databinding.TemperatureLogViewBinding
import com.xdd.covid_19information2.localdatabase.bodylog.Body
import com.xdd.covid_19information2.localdatabase.bodylog.BodyBody
import com.xdd.covid_19information2.localdatabase.bodylog.BodyLogHelper
import com.xdd.covid_19information2.ui.bodylog.BodyLogFragment
import com.xdd.taiwancovid_19informationfinal.dialog.ConfirmDialog
import com.xdd.taiwancovid_19informationfinal.dialog.TemperatureDialog
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class TemperatureAdapter(private val context: Context, private val arrayList: ArrayList<Temperature>): RecyclerView.Adapter<TemperatureAdapter.ViewHolder>() {

    private val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US)
    private val dateFormat2 = SimpleDateFormat("yyyy年M月d日 EE a h:mm", Locale.TAIWAN)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TemperatureAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.temperature_log_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TemperatureAdapter.ViewHolder, position: Int) {
        val temperature = arrayList[position]

        holder.binding.apply {
            temperature2Tv.text = temperature.temperature.toString() + " °C"
            datetimeTv.text = dateFormat2.format(dateFormat.parse(temperature.datetime)!!)

            temperatureLayout.setOnClickListener{
                TemperatureDialog(root.context, temperature).onConfirmClick {
                    temperature2Tv.text = it.temperature.toString() + " °C"
                    datetimeTv.text = dateFormat2.format(dateFormat.parse(it.datetime)!!)
                    Snackbar.make(root, "修改成功", Snackbar.LENGTH_SHORT).show()
                }
            }

            temperatureLayout.setOnLongClickListener {
                ConfirmDialog(root.context, "是否刪除「${datetimeTv.text} ${temperature2Tv.text}」該筆紀錄").onConfirmClick(temperature){
                    notifyItemRemoved(arrayList.indexOf(temperature))
                    arrayList.removeAt(arrayList.indexOf(temperature))

                    Snackbar.make(root, "刪除成功", Snackbar.LENGTH_SHORT).show()
                }

                true
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val binding = TemperatureLogViewBinding.bind(v)
    }
}