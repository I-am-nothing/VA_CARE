package com.xdd.covid_19information2.ui.covid19detail

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xdd.covid_19information2.ActionBar
import com.xdd.covid_19information2.MainActivity
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.databinding.Covid19DetailFragmentBinding
import com.xdd.covid_19information2.method.Covid19Information

class Covid19DetailFragment : Fragment() {

    private var _binding: Covid19DetailFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: Covid19DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = Covid19DetailFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(Covid19DetailViewModel::class.java)

        if(arguments?.getBoolean("Widget", false)!!){
            (requireActivity() as MainActivity).setActionBar(ActionBar.NoCancel, "Covid Information")
        }
        else{
            (requireActivity() as MainActivity).setActionBar(ActionBar.Cancel, "Covid Information")
        }

        binding.datetime4Tv.text = arguments?.getString("Date", "")
        binding.description3Tv.text = arguments?.getString("Message", "")

        if(viewModel.getJson(arguments?.getString("Information", "")!!) != null){
            val imageBytes = Base64.decode(viewModel.getJson(arguments?.getString("Information", "")!!), Base64.DEFAULT)
            val options = BitmapFactory.Options().apply {
                inSampleSize = 3
                inJustDecodeBounds = false
            }
            val decodeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size, options)
            requireActivity().runOnUiThread{
                binding.imageView2.setImageBitmap(decodeImage)
            }
        }
        else{
            Covid19Information(requireActivity()).getCovidWithId(arguments?.getString("Information", "")!!){
                if(it != null){
                    val imageBytes = Base64.decode(it["Image"].toString(), Base64.DEFAULT)
                    val options = BitmapFactory.Options().apply {
                        inSampleSize = 3
                        inJustDecodeBounds = false
                    }
                    val decodeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size, options)
                    requireActivity().runOnUiThread{
                        binding.imageView2.setImageBitmap(decodeImage)
                    }

                    viewModel.addJson(arguments?.getString("Information", "")!!, it["Image"].toString())
                }
            }
        }

        return binding.root
    }

}