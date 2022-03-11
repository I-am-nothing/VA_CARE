package com.xdd.covid_19information2.ui.setting

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.navigation.Navigation
import com.xdd.covid_19information2.ActionBar
import com.xdd.covid_19information2.MainActivity
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.databinding.SettingFragmentBinding
import com.xdd.covid_19information2.method.Covid19Information

class SettingFragment : Fragment() {

    private var _binding: SettingFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettingFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)

        (requireActivity() as MainActivity).setActionBar(ActionBar.Cancel, "Settings")

        binding.editProfileBtn.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_settingFragment_to_updateUserInformationFragment2)
        }

        binding.changePasswordBtn.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_settingFragment_to_changePasswordFragment)
        }

        binding.aboutUsBtn.setOnClickListener{
            policy()
        }

        binding.privacyPolicyBtn.setOnClickListener{
            policy()
        }

        binding.termsConditionsBtn.setOnClickListener{
            policy()
        }


        return binding.root
    }

    private fun policy(){

        val dialog = AlertDialog.Builder(binding.root.context)
            .setCancelable(false)
            .setView(R.layout.loading_view)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()


        Covid19Information(requireActivity()).getPolicy {
            dialog.dismiss()

            if(it != null){
                var str = ""
                for(i in 0 until it.length()){
                    if(it.getJSONObject(i).getString("Title") != "null"){
                        str += "\n" + it.getJSONObject(i).getString("Title")
                    }
                    str += "\n" + it.getJSONObject(i).getString("Description")
                }
                val bundle = Bundle().apply {
                    putString("Information", "NULL")
                    putString("Date", "Privacy Policy")
                    putString("Message", str)
                }

                requireActivity().runOnUiThread {
                    Navigation.findNavController(binding.root).navigate(R.id.action_settingFragment_to_covid19DetailFragment, bundle)
                }
            }
        }
    }
}