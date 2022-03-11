package com.xdd.covid_19information2.ui.covid19

import android.app.AlertDialog
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.xdd.covid_19information2.ActionBar
import com.xdd.covid_19information2.MainActivity
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.adapter.covidinformation.CovidInformation
import com.xdd.covid_19information2.adapter.covidinformation.CovidInformationAdapter
import com.xdd.covid_19information2.databinding.Covid19FragmentBinding
import com.xdd.covid_19information2.method.Covid19Information

class Covid19Fragment : Fragment() {

    private var _binding: Covid19FragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: Covid19ViewModel
    private var onUi = true
    private var nowLayout: CovidInformation? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = Covid19FragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(Covid19ViewModel::class.java)

        (requireActivity() as MainActivity).setActionBar(ActionBar.Cancel, "Covid-19 Information")

        val gridLayoutManager = GridLayoutManager(binding.root.context, 1, LinearLayoutManager.VERTICAL, false)
        binding.covRv.layoutManager = gridLayoutManager

        val dialog = AlertDialog.Builder(binding.root.context)
            .setCancelable(false)
            .setView(R.layout.loading_view)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        if(viewModel.getCovidInformation() != null){
            dialog.dismiss()
            requireActivity().runOnUiThread {
                binding.covRv.adapter = CovidInformationAdapter(
                    binding.root.context,
                    viewModel.getCovidInformation()!!
                )
            }
        }
        Thread {
            while (onUi){
                if(viewModel.getCovidInformation() != null){
                    for(i in viewModel.getCovidInformation()!!){
                        nowLayout = i
                        if(!onUi){
                            break
                        }
                        requireActivity().runOnUiThread{
                            binding.headTv.text = i.title
                            binding.headDate.text = i.datetime
                            //binding.headImg.setImageBitmap(i.image)
                        }
                        Thread.sleep(5000)
                    }
                }
            }
        }.start()

        Covid19Information(requireActivity()).getCovidInformation {
            if(it != null){
                val arrayList = ArrayList<CovidInformation>()
                for(i in 0 until it.length()){
                    val date = it.getJSONObject(i)["Date"].toString()
                    arrayList.add(CovidInformation(date.substring(0, date.indexOf('T') - 1), it.getJSONObject(i)["Message"].toString(), it.getJSONObject(i)["InformationId"].toString()))
                }

                viewModel.setCovidInformation(arrayList)
                requireActivity().runOnUiThread {
                    binding.covRv.adapter = CovidInformationAdapter(binding.root.context,
                        arrayList
                    )
                }
                dialog.dismiss()
            }
        }

        binding.layout5.setOnClickListener {
            if(nowLayout != null){
                val bundle = Bundle().apply {
                    putString("Information", nowLayout!!.informationId)
                    putString("Date", nowLayout!!.datetime)
                    putString("Message", nowLayout!!.title)
                }
                Navigation.findNavController(it).navigate(R.id.action_covid19Fragment_to_covid19DetailFragment, bundle)
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        onUi = false
    }

    override fun onStop() {
        super.onStop()

        onUi = false
    }

    override fun onResume() {
        super.onResume()

        onUi = true
    }
}