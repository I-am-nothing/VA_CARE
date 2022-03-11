package com.xdd.covid_19information2.localdatabase.bodylog

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.lang.Exception

class BodyLogHelper private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTemperatureTable = "CREATE TABLE $TABLE_TEMPERATURE($KEY_DATETIME VARCHAR(30) PRIMARY KEY, $KEY_TEMPERATURE FLOAT NOT NULL)"

        db?.execSQL(createTemperatureTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TEMPERATURE")
        onCreate(db)
    }

    fun addTemperature(body: Body): Long{
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(KEY_DATETIME, body.datetime)
            put(KEY_TEMPERATURE, body.temperature)
        }
        val success = db.insert(TABLE_TEMPERATURE, null, contentValues)
        db.close()

        return success
    }

    fun getAllTemperature(): List<Body>{
        val db = this.readableDatabase
        val arrayList = ArrayList<Body>()
        val selectQuery = "SELECT * FROM $TABLE_TEMPERATURE ORDER BY $KEY_DATETIME DESC"

        val cursor = db.rawQuery(selectQuery, null)

        if(cursor.moveToFirst()){
            do{
                val dateTime = cursor.getString(0)
                val temperature = cursor.getFloat(1)
                arrayList.add(Body(dateTime, temperature))
            } while (cursor.moveToNext())
        }
        cursor.close()

        return arrayList
    }

    fun updateTemperature(body: Body, datetimeLast: String): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(KEY_DATETIME, body.datetime)
            put(KEY_TEMPERATURE, body.temperature)
        }

        val success = db?.update(TABLE_TEMPERATURE, contentValues, "$KEY_DATETIME=$datetimeLast", null)
        db.close()

        return success!!
    }

    fun deleteEmployee(body: Body): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(KEY_DATETIME, body.datetime)
            put(KEY_TEMPERATURE, body.temperature)
        }

        val success = db.delete(TABLE_TEMPERATURE, "$KEY_DATETIME='${body.datetime}'", null)
        db.close()

        return success
    }

    companion object{
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "BodyTemperatureDb"
        private const val TABLE_TEMPERATURE = "TemperatureLog"
        private const val KEY_DATETIME = "Datetime"
        private const val KEY_TEMPERATURE = "Temperature"

        private var sInstance: BodyLogHelper? = null

        @Synchronized
        fun getInstance(context: Context): BodyLogHelper? {
            if (sInstance == null) {
                sInstance = BodyLogHelper(context.applicationContext)
            }
            return sInstance
        }
    }
}