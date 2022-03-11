package com.xdd.covid_19information2.ui.covid19detail

import androidx.lifecycle.ViewModel
import org.json.JSONObject
import java.lang.Exception

class Covid19DetailViewModel : ViewModel() {

    fun addJson(id: String, base64Image: String){
        json.put(id, base64Image)
    }

    fun getJson(id: String): String?{
        return try{
            json[id].toString()
        } catch (e: Exception){
            null
        }
    }

    companion object{
        @JvmStatic
        val json = JSONObject()
    }
}