package com.xdd.covid_19information2.ui.vaccine

import androidx.lifecycle.ViewModel
import com.xdd.covid_19information2.adapter.health.Health

class VaccineViewModel : ViewModel() {

    @JvmName("getVaccine1")
    fun getVaccine(): Vaccine?{
        return vaccine
    }

    @JvmName("setVaccine1")
    fun setVaccine(value: Vaccine?){
        vaccine = value
    }


    companion object{
        @JvmStatic
        var vaccine: Vaccine? = null
    }
}