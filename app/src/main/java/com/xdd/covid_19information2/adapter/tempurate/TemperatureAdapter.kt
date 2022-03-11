package com.xdd.covid_19information2.adapter.tempurate

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.databinding.ConfirmDialogBinding
import com.xdd.covid_19information2.databinding.TemperatureLogViewBinding
import com.xdd.covid_19information2.localdatabase.bodylog.Body
import com.xdd.covid_19information2.localdatabase.bodylog.BodyBody
import com.xdd.covid_19information2.localdatabase.bodylog.BodyLogHelper
import com.xdd.covid_19information2.ui.bodylog.BodyLogFragment
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class TemperatureAdapter(private val context: Context, private val list: List<BodyBody>, private val fragment: BodyLogFragment): RecyclerView.Adapter<TemperatureAdapter.TemperatureHolder>() {

    private val bodyLogHelper = BodyLogHelper.getInstance(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TemperatureAdapter.TemperatureHolder {
        val viewHolder = LayoutInflater.from(context).inflate(R.layout.temperature_log_view, parent, false)

        return TemperatureHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: TemperatureAdapter.TemperatureHolder, position: Int) {
        val body = list[position]

        holder.binding.temperature2Tv.text = body.temperature.toString() + " °C"
        holder.binding.datetimeTv.text = body.datetime

        holder.binding.temperatureLayout.setOnClickListener {
            val view = View.inflate(context, R.layout.confirm_dialog, null)
            val binding = ConfirmDialogBinding.bind(view)

            val dialog = AlertDialog.Builder(context)
                .setView(view)
                .create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            binding.descriptionTv.text = "Are you sure to delete \"${body.datetime}\" Log?"

            binding.cancel2Btn.setOnClickListener {
                dialog.dismiss()
            }

            binding.deleteBtn.setOnClickListener {
                bodyLogHelper?.deleteEmployee(Body(body.lastDatetime, body.temperature))
                dialog.dismiss()
                fragment.onDelete()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class TemperatureHolder(v: View): RecyclerView.ViewHolder(v){
        val binding = TemperatureLogViewBinding.bind(v)
    }
}