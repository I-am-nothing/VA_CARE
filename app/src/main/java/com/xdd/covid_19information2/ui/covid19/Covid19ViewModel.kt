package com.xdd.covid_19information2.ui.covid19

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.xdd.covid_19information2.adapter.covidinformation.CovidInformation
import com.xdd.covid_19information2.method.Covid19Information
import org.json.JSONObject

class Covid19ViewModel : ViewModel() {

    @JvmName("getCovidInformation1")
    fun getCovidInformation(): ArrayList<CovidInformation>?{
        return covidInformation
    }

    @JvmName("setCovidInformation1")
    fun setCovidInformation(value: ArrayList<CovidInformation>?){
        covidInformation = value
    }


    companion object{
        @JvmStatic
        var covidInformation: ArrayList<CovidInformation>? = null
    }
}