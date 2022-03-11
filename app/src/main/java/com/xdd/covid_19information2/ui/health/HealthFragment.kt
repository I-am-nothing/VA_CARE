package com.xdd.covid_19information2.ui.health

import android.app.AlertDialog
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.xdd.covid_19information2.ActionBar
import com.xdd.covid_19information2.MainActivity
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.adapter.health.Health
import com.xdd.covid_19information2.adapter.health.HealthAdapter
import com.xdd.covid_19information2.databinding.HealthFragmentBinding
import com.xdd.covid_19information2.databinding.HelloFragmentBinding
import com.xdd.covid_19information2.method.Covid19Information
import java.io.InputStream
import java.net.URL

class HealthFragment : Fragment() {

    private var _binding: HealthFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HealthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HealthFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(HealthViewModel::class.java)

        (requireActivity() as MainActivity).setActionBar(ActionBar.Cancel, "Health Information")

        val gridLayoutManager = GridLayoutManager(binding.root.context, 1, LinearLayoutManager.VERTICAL, false)

        val dialog = AlertDialog.Builder(binding.root.context)
            .setCancelable(false)
            .setView(R.layout.loading_view)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        val adapter: HealthAdapter = if(viewModel.getHealthInformation() == null){
            HealthAdapter(binding.root.context, ArrayList())
        }else{
            dialog.dismiss()
            HealthAdapter(binding.root.context, viewModel.getHealthInformation()!!)
        }
        binding.healthRv.layoutManager = gridLayoutManager
        binding.healthRv.adapter = adapter

        Covid19Information(requireActivity()).getHealth {
            val arrayList = ArrayList<Health>()

            if(it != null){
                dialog.dismiss()
                for(i in 0 until it.getJSONArray("items").length()){
                    val title = it.getJSONArray("items").getJSONObject(i).getJSONObject("snippet")["title"].toString()
                    val url = "https://www.youtube.com/watch?v=" + it.getJSONArray("items").getJSONObject(i).getJSONObject("snippet").getJSONObject("resourceId")["videoId"].toString()
                    val imageUrl = it.getJSONArray("items").getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium")["url"].toString()
                    val imageStream = URL(imageUrl).content as InputStream
                    val drawable = Drawable.createFromStream(imageStream, "XDD")
                    val health = Health(title, url, drawable)
                    arrayList.add(health)

                    if(viewModel.getHealthInformation() == null){
                        requireActivity().runOnUiThread {
                            adapter.addHealth(health)
                        }
                    }
                }

                requireActivity().runOnUiThread {
                    binding.healthRv.adapter = HealthAdapter(binding.root.context, arrayList)
                }
                viewModel.setHealthInformation(arrayList)
            }
        }

        binding.search2Tb.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val arrayList = ArrayList<Health>()

                for(i in viewModel.getHealthInformation()!!){
                    if(i.title.contains(binding.search2Tb.text)){
                        arrayList.add(i)
                    }
                }

                binding.healthRv.adapter = HealthAdapter(binding.root.context, arrayList)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        return binding.root
    }
}