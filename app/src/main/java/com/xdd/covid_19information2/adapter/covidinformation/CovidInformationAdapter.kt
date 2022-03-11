package com.xdd.covid_19information2.adapter.covidinformation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.databinding.CovidInformationViewBinding

class CovidInformationAdapter(private val context: Context, private val arrayList: ArrayList<CovidInformation>): RecyclerView.Adapter<CovidInformationAdapter.CovidInformationHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CovidInformationAdapter.CovidInformationHolder {
        val viewHolder = LayoutInflater.from(context).inflate(R.layout.covid_information_view, parent, false)

        return CovidInformationHolder(viewHolder)
    }

    override fun onBindViewHolder(
        holder: CovidInformationAdapter.CovidInformationHolder,
        position: Int
    ) {
        val covidInformation = arrayList[position]
        val date = covidInformation.datetime.split("-")

        holder.binding.month2Tv.text = "${date[0]}-${date[1]}"
        holder.binding.dayTv.text = date[2]
        holder.binding.descripiton2Tv.text = covidInformation.title

        holder.binding.layout4.setOnClickListener{
            val bundle = Bundle().apply {
                putString("Information", covidInformation.informationId)
                putString("Date", covidInformation.datetime)
                putString("Message", covidInformation.title)
            }
            Navigation.findNavController(it).navigate(R.id.action_covid19Fragment_to_covid19DetailFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class CovidInformationHolder(v: View): RecyclerView.ViewHolder(v){
        val binding = CovidInformationViewBinding.bind(v)
    }
}