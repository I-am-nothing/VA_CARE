package com.xdd.covid_19information2.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Base64
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.adapter.covidinformation.CovidInformation
import com.xdd.covid_19information2.adapter.covidinformation.CovidInformationAdapter
import com.xdd.covid_19information2.method.Covid19Information
import java.util.concurrent.ThreadLocalRandom

class ListWidgetService: RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return CovidInformationViewFactory(applicationContext, intent)
    }

    internal inner class CovidInformationViewFactory(private val context: Context, private val intent: Intent): RemoteViewsService.RemoteViewsFactory {

        private val arrayList = ArrayList<CovidInformation>()

        override fun onCreate() {
            var statusOk = false

            Covid19Information(null).getCovidInformation {
                if(it != null){
                    for(i in 0 until it.length()){
                        val imageBytes = Base64.decode(it.getJSONObject(i)["Image"].toString(), Base64.DEFAULT)
                        val options = BitmapFactory.Options().apply {
                            inSampleSize = 3
                            inJustDecodeBounds = false
                        }
                        val decodeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size, options)
                        val date = it.getJSONObject(i)["Date"].toString()
                        //arrayList.add(CovidInformation(date.substring(0, date.indexOf('T') - 1), it.getJSONObject(i)["Message"].toString(), decodeImage, it.getJSONObject(i)["InformationId"].toString()))
                    }
                    statusOk = true
                }
            }

            while (!statusOk){
                Thread.sleep(500)
            }
        }

        override fun onDataSetChanged() {

        }

        override fun onDestroy() {
            arrayList.clear()
        }

        override fun getCount(): Int {
            return arrayList.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val information = arrayList[position]
            val rv = RemoteViews(context.packageName, R.layout.widget_view)

            rv.setImageViewBitmap(R.id.widget_img, information.image)
            rv.setTextViewText(R.id.widget_title_tv, information.title)
            rv.setTextViewText(R.id.widget_datetime_tv, information.datetime)

            val extras = Bundle().apply {
                putString("Date", information.datetime)
                putString("Message", information.title)
                putString("Information", information.informationId)
                putBoolean("Widget", true)
            }

            val fillInIntent = Intent().apply {
                putExtras(extras)
            }
            rv.setOnClickFillInIntent(R.id.layout3, fillInIntent)

            Thread.sleep(100)

            return rv
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }
    }
}