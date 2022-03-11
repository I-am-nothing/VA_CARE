package com.xdd.covid_19information2.adapter.health

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.databinding.HealthViewBinding
import kotlin.collections.ArrayList

class HealthAdapter(private val context: Context, private val arrayList: ArrayList<Health>): RecyclerView.Adapter<HealthAdapter.HealthHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthAdapter.HealthHolder {
        val viewHolder = LayoutInflater.from(context).inflate(R.layout.health_view, parent, false)

        return HealthHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: HealthAdapter.HealthHolder, position: Int) {
        val health = arrayList[position]

        holder.binding.description2Tv.text = health.title

        if(health.image != null) {
            holder.binding.titleImg.setImageDrawable(health.image)
        }

        holder.binding.layout2.setOnClickListener{
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(health.uri))
            context.startActivity(webIntent)
        }
    }

    fun addHealth(value: Health){
        arrayList.add(value)
        notifyItemChanged(arrayList.size-1)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class HealthHolder(v: View): RecyclerView.ViewHolder(v){
        val binding = HealthViewBinding.bind(v)
    }
}