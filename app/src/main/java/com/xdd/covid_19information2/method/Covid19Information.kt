package com.xdd.covid_19information2.method

import android.app.Activity
import android.content.Context
import android.util.JsonReader
import android.util.Log
import android.widget.Toast
import androidx.core.content.contentValuesOf
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.Inet4Address
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class Covid19Information(private val activity: Activity?) {
    companion object{
        private const val hostUrl = "http://54.234.67.26/taiwancovid19information"

        @JvmStatic
        private var accountId: String? = null
    }

    fun getPolicy(result: (JSONArray?) -> Unit){
        httpGet("/information/policy"){
            if(it != null){
                result(it.getJSONArray("message"))
            }
        }
    }

    fun updateAccountPassword(oldPassword: String, newPassword: String, result: (Boolean) -> Unit){
        val body = JSONObject()
            .put("accountId", accountId)
            .put("oldPassword", oldPassword)
            .put("newPassword", newPassword)
        httpPost("/account/update/password", body){
            if(it != null){
                result(it.getBoolean("status"))
            }
            else{
                result(false)
            }
        }
    }

    fun updateAccountInformation(name: String, phone: String, address: String, result: (Boolean) -> Unit){
        val body = JSONObject()
            .put("accountId", accountId)
            .put("name", name)
            .put("phone", phone)
            .put("address", address)
        httpPost("/account/update/information", body){
            if(it != null){
                result(it.getBoolean("status"))
            }
            else{
                result(false)
            }
        }
    }

    fun vaccineBook(identityId: String, name: String, phone: String, address: String, az: Boolean, bnt: Boolean, mda: Boolean, mvc: Boolean, vaccineLocationId: String, result: (Boolean) -> Unit){
        val body = JSONObject()
            .put("accountId", accountId)
            .put("identityId", identityId)
            .put("name", name)
            .put("phone", phone)
            .put("address", address)
            .put("az", az)
            .put("bnt", bnt)
            .put("mda", mda)
            .put("mvc", mvc)
            .put("vaccineLocationId", vaccineLocationId)

        httpPost("/vaccine/book", body){
            if(it != null){
                result(it["status"] as Boolean)
            }
            else{
                result(false)
            }
        }
    }

    fun getVaccineLocation(result: (JSONArray?) -> Unit){
        httpGet("/vaccine/location"){
            if(it == null){
                result(null)
            }
            else{
                result(it.getJSONArray("message"))
            }
        }
    }

    fun getAccountInformation(result: (JSONObject?) -> Unit){
        val body = JSONObject().put("accountId", accountId)
        httpPost("/account/information", body){
            Log.e("XDD",it.toString())
            if(it == null || !(it["status"] as Boolean)){
                result(null)
            }
            else{
                result(it.getJSONArray("message").getJSONObject(0))
            }
        }
    }

    fun getCovidWithId(id: String, result: (JSONObject?) -> Unit){
        httpGet("/information/covid19/$id"){
            if(it == null || it["status"] == false){
                result(null)
            }
            else{
                result(it.getJSONArray("message").getJSONObject(0))
            }
        }
    }

    fun getHealth(result: (JSONObject?) -> Unit){
        httpGet("/information/health"){
            if(it == null){
                result(null)
            }
            else{
                result(it.getJSONObject("message"))
            }
        }
    }

    fun checkStatus(result:(Boolean) -> Unit){
        httpGet("/checkstatus"){
            if(it != null){
                result(it.get("status") as Boolean)
            }
            else{
                result(false)
            }
        }
    }

    fun getCovidInformation(result: (JSONArray?) -> Unit){
        httpGet("/information/covid19"){
            if(it == null){
                result(null)
            }
            else{
                result(it.getJSONArray("message"))
            }
        }
    }

    fun getCovidImage(id: String, result: (String?) -> Unit){
        Log.e("FUCK", "In")
        httpGet("/information/covid19/$id"){
            if(it == null){
                result(null)
            }
            else{
                Log.e("FUCK", it.toString())
                result(it.getJSONArray("message").getJSONObject(0).getString("Image"))
            }
        }
    }

    fun register(email: String, password: String, identityId: String, name: String, phone: String, address: String, result:(String?) -> Unit){
        Log.e("XDD", address)
        val body = JSONObject()
            .put("email", email)
            .put("password", password)
            .put("identityId", identityId)
            .put("name", name)
            .put("phone", phone)
            .put("address", address)
        httpPost("/account/register", body){
            if(it?.get("status") as Boolean){
                accountId = it["message"] as String
                saveLocalData(JSONObject().put("email", email).put("password", password))
                result(accountId)
            }
            else{
                result(null)
            }
        }
    }

    fun login(email: String, password: String, result:(String?) -> Unit){
        val body = JSONObject()
            .put("email", email)
            .put("password", password)
        httpPost("/account/login", body){
            if(it?.get("status") as Boolean){
                accountId = it["message"] as String
                saveLocalData(JSONObject().put("email", email).put("password", password))
                result(accountId)
            }
            else{
                result(null)
            }
        }
    }

    fun login(result: (String?) -> Unit){
        val localData = getLocalData()
        if(localData != null){
            if(localData["email"].toString() != "" && localData["password"].toString() != ""){
                login(localData["email"].toString(), localData["password"].toString()){
                    result(it)
                }
            }
        }
        else{
            result(null)
        }
    }

    private fun getLocalData(): JSONObject?{
        val sharedPref = activity!!.getPreferences(Context.MODE_PRIVATE)
        val json = sharedPref.getString("LocalData", null)

        return if(json == null){
            null
        } else{
            JSONObject(json)
        }
    }

    private fun saveLocalData(body: JSONObject){
        activity!!.getPreferences(Context.MODE_PRIVATE)
            .edit()
            .putString("LocalData", body.toString())
            .apply()
    }

    private fun httpPost(path: String, body: JSONObject, result: (JSONObject?) -> Unit){
        Thread {
            try {
                with(URL(hostUrl + path).openConnection() as HttpURLConnection) {
                    requestMethod = "POST"
                    doOutput = true
                    readTimeout = 3000
                    addRequestProperty("Content-Type", "application/json")
                    addRequestProperty("Accept", "application/json")
                    outputStream.write(body.toString().toByteArray(), 0, body.toString().toByteArray().size)
                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()

                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        it.close()
                        disconnect()

                        result(JSONObject(response.toString()))
                    }
                }
            }
            catch (e: Exception){
                Log.e("XDD", e.message.toString())
                activity!!.runOnUiThread {
                    Toast.makeText(activity.applicationContext, e.message, Toast.LENGTH_SHORT).show()
                }
                result(null)
            }

        }.start()
    }

    private fun httpGet(path: String, result: (JSONObject?) -> Unit){
        Thread{
            try {
                with(URL(hostUrl + path).openConnection() as HttpURLConnection){
                    requestMethod = "GET"
                    readTimeout = 5000
                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()

                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        it.close()
                        disconnect()

                        result(JSONObject(response.toString()))
                    }
                }
            }
            catch (e: Exception){
                Log.e("XDD", e.message.toString())
                //activity!!.runOnUiThread {
                    //Toast.makeText(activity.applicationContext, e.message, Toast.LENGTH_SHORT).show()
               // }
                result(null)
            }
        }.start()
    }
}