package com.xdd.covid_19information2.widget

import android.app.Application
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.xdd.covid_19information2.MainActivity
import com.xdd.covid_19information2.R

class CovidInformationWidget : AppWidgetProvider() {

    companion object{
        const val CLICK_ACTION = "com.xdd.covid-19_information_2.CLICK_ACTION"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val intent = Intent(context, ListWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = (Uri.parse(this.toUri(Intent.URI_INTENT_SCHEME)))
            }
            val remoteViews = RemoteViews(context.packageName, R.layout.covid_information_widget).apply {
                setRemoteAdapter(R.id.widget_lv, intent)
                setEmptyView(R.id.widget_lv, R.id.empty_tv)
            }
            val clickIntent = Intent(context, CovidInformationWidget::class.java).apply {
                action = CLICK_ACTION
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            val clickPendingIntent = PendingIntent.getBroadcast(context, 8787, clickIntent, PendingIntent.FLAG_MUTABLE)
            remoteViews.setPendingIntentTemplate(R.id.widget_lv, clickPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)

            Thread.sleep(200)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action == CLICK_ACTION) {
            val mainIntent = Intent(context, MainActivity::class.java).apply {
                putExtras(intent.extras!!)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context!!.startActivity(mainIntent)
        }
        super.onReceive(context, intent)
    }
}