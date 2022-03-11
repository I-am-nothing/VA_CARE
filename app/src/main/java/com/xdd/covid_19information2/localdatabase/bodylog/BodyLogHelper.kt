package com.xdd.covid_19information2.localdatabase.bodylog

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.xdd.covid_19information2.adapter.tempurate.Temperature
import java.lang.Exception

class BodyLogHelper private constructor(context: Context): SQLiteOpenHelper(context, "Covid19", null, 1){
    companion object{
        private var sInstance: BodyLogHelper? = null

        fun getInstance(context: Context): BodyLogHelper{
            if(sInstance == null){
                sInstance = BodyLogHelper(context.applicationContext)
            }
            return sInstance!!
        }
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("CREATE TABLE Temperature (Id INTEGER PRIMARY KEY AUTOINCREMENT, Datetime VARCHAR(30) NOT NULL, Temperature FLOAT NOT NULL)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS Temperature")
        onCreate(p0)
    }

    fun getTemperatures(): ArrayList<Temperature>{
        val db = readableDatabase
        val arrayList = ArrayList<Temperature>()
        val command = "SELECT * FROM Temperature ORDER BY Id DESC"

        val cursor = db.rawQuery(command, null)

        if(cursor.moveToFirst()){
            do{
                arrayList.add(Temperature(cursor.getInt(0), cursor.getString(1), cursor.getFloat(2)))
            }
            while (cursor.moveToNext())
        }

        cursor.close()

        return arrayList
    }

    fun addTemperature(temperature: Temperature): Long{
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            putNull("Id")
            put("Datetime", temperature.datetime)
            put("Temperature", temperature.temperature)
        }
        val result = db.insert("Temperature", null, contentValues)

        db.close()

        return result
    }

    fun updateTemperature(temperature: Temperature): Int{
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("Id", temperature.id)
            put("Datetime", temperature.datetime)
            put("Temperature", temperature.temperature)
        }

        val result = db.update("Temperature", contentValues, "Id = ${temperature.id}", null)

        db.close()

        return result
    }

    fun deleteTemperature(temperature: Temperature): Int{
        val db = writableDatabase

        val result = db.delete("Temperature", "Id = ${temperature.id}", null)

        db.close()

        return result
    }
}