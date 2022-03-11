package com.xdd.covid_19information2.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.addCallback
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.work.*
import com.xdd.covid_19information2.ActionBar
import com.xdd.covid_19information2.MainActivity
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.databinding.MainFragmentBinding
import com.xdd.covid_19information2.service.NotificationService
import java.util.concurrent.TimeUnit

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        (requireActivity() as MainActivity).setActionBar(ActionBar.Settings, "")

        workManager()

        (requireActivity() as MainActivity).toolbarSettingClick {
            Navigation.findNavController(binding.root).navigate(R.id.action_mainFragment_to_settingFragment)
        }

        binding.vacBtn.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_mainFragment_to_vaccineFragment)
        }

        binding.covBtn.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_mainFragment_to_covid19Fragment)
        }

        binding.heaBtn.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_mainFragment_to_healthFragment)
        }

        binding.bodyBtn.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_mainFragment_to_bodyLogFragment)
        }

        return binding.root
    }

    private fun workManager(){
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(true)
            .build()

        val request = PeriodicWorkRequest.Builder(NotificationService::class.java, 5, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(binding.root.context).enqueueUniquePeriodicWork("vaccine_location", ExistingPeriodicWorkPolicy.KEEP, request)
    }

    private fun notificationWork(){
        val request: WorkRequest = OneTimeWorkRequestBuilder<NotificationService>().build()

        WorkManager.getInstance(binding.root.context).enqueue(request)
    }
}