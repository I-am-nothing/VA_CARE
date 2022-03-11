package com.xdd.covid_19information2.ui.hello

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.xdd.covid_19information2.ActionBar
import com.xdd.covid_19information2.MainActivity
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.databinding.HelloFragmentBinding
import com.xdd.covid_19information2.method.Covid19Information

class HelloFragment : Fragment() {

    private var _binding: HelloFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HelloViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HelloFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(HelloViewModel::class.java)

        (requireActivity() as MainActivity).setActionBar(ActionBar.Hide, null)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            Covid19Information(requireActivity()).login {
                requireActivity().runOnUiThread{
                    Log.e("XDD", (requireActivity() as MainActivity).intent.getStringExtra("Information").toString())
                    when {
                        it == null -> {
                            Navigation.findNavController(binding.root).navigate(R.id.action_helloFragment_to_loginFragment)
                        }
                        (requireActivity() as MainActivity).intent.getStringExtra("Information") != null -> {
                            Navigation.findNavController(binding.root).navigate(R.id.action_helloFragment_to_covid19DetailFragment, (requireActivity() as MainActivity).intent.extras)
                        }
                        else -> {
                            Snackbar.make(requireView(), "Welcome", Snackbar.LENGTH_SHORT).show()
                            Navigation.findNavController(binding.root).navigate(R.id.action_helloFragment_to_mainFragment)
                        }
                    }
                }
            }
        }, 500)
    }
}