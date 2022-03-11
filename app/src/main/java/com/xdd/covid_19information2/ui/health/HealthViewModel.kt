package com.xdd.covid_19information2.ui.health

import androidx.lifecycle.ViewModel
import com.xdd.covid_19information2.adapter.covidinformation.CovidInformation
import com.xdd.covid_19information2.adapter.health.Health

class HealthViewModel : ViewModel() {
    @JvmName("getHealthInformation1")
    fun getHealthInformation(): ArrayList<Health>?{
        return healthInformation
    }

    @JvmName("setHealthInformation1")
    fun setHealthInformation(value: ArrayList<Health>?){
        healthInformation = value
    }


    companion object{
        @JvmStatic
        var healthInformation: ArrayList<Health>? = null
    }
}