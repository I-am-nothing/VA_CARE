package com.xdd.covid_19information2.ui.updateuserinformation

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.xdd.covid_19information2.ActionBar
import com.xdd.covid_19information2.MainActivity
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.databinding.UpdateUserInformationFragmentBinding
import com.xdd.covid_19information2.method.Covid19Information
import org.json.JSONObject

class UpdateUserInformationFragment : Fragment() {

    private var _binding: UpdateUserInformationFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UpdateUserInformationViewModel
    private var userInformation: JSONObject? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UpdateUserInformationFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(UpdateUserInformationViewModel::class.java)

        (requireActivity() as MainActivity).setActionBar(ActionBar.Cancel, "Edit Profile")

        userInformation = viewModel.getJson()

        if(userInformation != null){
            binding.name3Tb.setText(userInformation!!.getString("Name"))
            binding.phone3Tb.setText(userInformation!!.getString("Phone"))
            binding.address3Tb.setText(userInformation!!.getString("Address"))
        }
        else{
            Covid19Information(requireActivity()).getAccountInformation {
                if(it != null){
                    userInformation = it
                    viewModel.setJson(it)

                    requireActivity().runOnUiThread {
                        binding.name3Tb.setText(userInformation!!.getString("Name"))
                        binding.phone3Tb.setText(userInformation!!.getString("Phone"))
                        binding.address3Tb.setText(userInformation!!.getString("Address"))
                    }
                }
            }
        }

        binding.updateBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(binding.root.context)
                .setCancelable(false)
                .setView(R.layout.loading_view)
                .create()

            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()

            Covid19Information(requireActivity()).updateAccountInformation(binding.name3Tb.text.toString(), binding.phone3Tb.text.toString(), binding.address3Tb.text.toString()){
                if(it){
                    userInformation!!.put("Name", binding.name3Tb.text.toString())
                    userInformation!!.put("Phone", binding.phone3Tb.text.toString())
                    userInformation!!.put("Address", binding.address3Tb.text.toString())

                    requireActivity().runOnUiThread{
                        Snackbar.make(binding.root, "Update Success", Snackbar.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }
            }
        }

        return binding.root
    }
}