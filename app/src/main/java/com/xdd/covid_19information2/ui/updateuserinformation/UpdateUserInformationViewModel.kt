package com.xdd.covid_19information2.ui.updateuserinformation

import android.util.JsonReader
import androidx.lifecycle.ViewModel
import org.json.JSONObject
import java.lang.Exception

class UpdateUserInformationViewModel : ViewModel() {
    @JvmName("setJson1")
    fun setJson(body: JSONObject){
        json = body
    }

    @JvmName("getJson1")
    fun getJson(): JSONObject?{
        return json
    }

    companion object{
        @JvmStatic
        var json: JSONObject? = null
    }
}