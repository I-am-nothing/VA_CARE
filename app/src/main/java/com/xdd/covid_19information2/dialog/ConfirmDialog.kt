package com.xdd.taiwancovid_19informationfinal.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.View
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.adapter.tempurate.Temperature
import com.xdd.covid_19information2.databinding.ConfirmDialogBinding
import com.xdd.covid_19information2.localdatabase.bodylog.BodyLogHelper

class ConfirmDialog(context: Context, message: String) {
    private val view = View.inflate(context, R.layout.confirm_dialog, null)
    private val binding = ConfirmDialogBinding.bind(view)
    private val dialog = AlertDialog.Builder(context)
        .setView(binding.root)
        .create()

    init {
        dialog.apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            show()
        }

        binding.apply {
            messageTv.text = message

            cancel2Btn.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    fun onConfirmClick(temperature: Temperature, ok: () -> Unit){
        binding.apply {
            confirm2Btn.setOnClickListener {
                BodyLogHelper.getInstance(root.context).deleteTemperature(temperature)
                dialog.dismiss()
                ok()
            }
        }
    }
}