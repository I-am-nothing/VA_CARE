package com.xdd.covid_19information2.ui.vaccine

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatCallback
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.xdd.covid_19information2.ActionBar
import com.xdd.covid_19information2.MainActivity
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.databinding.VaccineFragmentBinding
import com.xdd.covid_19information2.method.Covid19Information
import org.json.JSONObject

class VaccineFragment : Fragment(){

    private var _binding: VaccineFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: VaccineViewModel
    private var vaccine: Vaccine? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = VaccineFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(VaccineViewModel::class.java)

        (requireActivity() as MainActivity).setActionBar(ActionBar.Cancel, "Vaccine Booking")

        val dialog = AlertDialog.Builder(binding.root.context)
            .setCancelable(false)
            .setView(R.layout.loading_view)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        vaccine = viewModel.getVaccine()
        if(vaccine != null){
            binding.iden2Tb.setText(vaccine!!.identityId)
            binding.name2Tb.setText(vaccine!!.name)
            binding.phone2Tb.setText(vaccine!!.phone)
            binding.address2Tb.setText(vaccine!!.address)
            binding.azCb.isChecked = vaccine!!.az
            binding.bntCb.isChecked = vaccine!!.bnt
            binding.mdaCb.isChecked = vaccine!!.mda
            binding.mvcCb.isChecked = vaccine!!.mvc

            if(vaccine!!.vaccineLocation != null){
                binding.vacAddressTb.setText(vaccine!!.vaccineLocation?.getString("Name"))
            }

            dialog.dismiss()
        }
        else{
            Covid19Information(requireActivity()).getAccountInformation {
                dialog.dismiss()
                if(it != null){
                    requireActivity().runOnUiThread {
                        binding.iden2Tb.setText(it["IdentityId"].toString())
                        binding.name2Tb.setText(it["Name"].toString())
                        binding.phone2Tb.setText(it["Phone"].toString())
                        binding.address2Tb.setText(it["Address"].toString())
                    }
                    vaccine = Vaccine(it["IdentityId"].toString()
                        , it["Name"].toString()
                        , it["Phone"].toString()
                        , it["Address"].toString()
                        , az = false
                        , bnt = false
                        , mda = false
                        , mvc = false,
                        null
                    )
                    viewModel.setVaccine(vaccine)
                }
            }
        }

        val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                when(it.key){
                    android.Manifest.permission.ACCESS_FINE_LOCATION -> {
                        if(it.value){
                            Navigation.findNavController(binding.root).navigate(R.id.action_vaccineFragment_to_mapsFragment)
                        }
                        else{
                            Snackbar.make(binding.root, "You must need the permission to use the Google map", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        
        binding.azCb.setOnCheckedChangeListener { _, isChecked ->
            vaccine!!.az = isChecked
            viewModel.setVaccine(vaccine)
        }

        binding.bntCb.setOnCheckedChangeListener { _, isChecked ->
            vaccine!!.bnt = isChecked
            viewModel.setVaccine(vaccine)
        }

        binding.mdaCb.setOnCheckedChangeListener { _, isChecked ->
            vaccine!!.mda = isChecked
            viewModel.setVaccine(vaccine)
        }

        binding.mvcCb.setOnCheckedChangeListener { _, isChecked ->
            vaccine!!.mvc = isChecked
            viewModel.setVaccine(vaccine)
        }

        binding.vacAddressTb.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(binding.root.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                requestMultiplePermissions.launch(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }
            else{
                Navigation.findNavController(binding.root).navigate(R.id.action_vaccineFragment_to_mapsFragment)
            }
        }

        binding.iden2Tb.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                vaccine!!.identityId = binding.iden2Tb.text.toString()
                viewModel.setVaccine(vaccine)
            }
        })

        binding.name2Tb.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                vaccine!!.name = binding.name2Tb.text.toString()
                viewModel.setVaccine(vaccine)
            }
        })

        binding.phone2Tb.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                vaccine!!.phone = binding.phone2Tb.text.toString()
                viewModel.setVaccine(vaccine)
            }
        })

        binding.address2Tb.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                vaccine!!.address = binding.address2Tb.text.toString()
                viewModel.setVaccine(vaccine)
            }
        })

        binding.bookBtn.setOnClickListener {
            when{
                !checkIdentity() -> Snackbar.make(binding.root, "Your Identity Id is invalid", Snackbar.LENGTH_SHORT).show()
                !checkName() -> Snackbar.make(binding.root, "Your name must have 2 to 30 character", Snackbar.LENGTH_SHORT).show()
                !checkPhone() -> Snackbar.make(binding.root, "Your phone number must have 10 character", Snackbar.LENGTH_SHORT).show()
                !checkAddress() -> Snackbar.make(binding.root, "Your name must have at least 9 character", Snackbar.LENGTH_SHORT).show()
                !checkCheckedTrue() -> Snackbar.make(binding.root, "You must select the vaccine", Snackbar.LENGTH_SHORT).show()
                !checkVaccineAddress() -> Snackbar.make(binding.root, "Your must choose the vaccine address", Snackbar.LENGTH_SHORT).show()
                else -> {
                    val dialog2 = AlertDialog.Builder(binding.root.context)
                        .setCancelable(false)
                        .setView(R.layout.loading_view)
                        .create()

                    dialog2.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog2.show()
                    Covid19Information(requireActivity()).vaccineBook(
                        vaccine!!.identityId,
                        vaccine!!.name,
                        vaccine!!.phone,
                        vaccine!!.address,
                        binding.azCb.isChecked,
                        binding.bntCb.isChecked,
                        binding.mdaCb.isChecked,
                        binding.mvcCb.isChecked,
                        vaccine!!.vaccineLocation!!.getString("VaccineLocationId")
                    ){
                        dialog2.dismiss()
                        if(it){
                            Snackbar.make(binding.root, "Booking Success", Snackbar.LENGTH_SHORT).show()
                        }
                        else{
                            Snackbar.make(binding.root, "Booking Error, Maybe you are already booking vaccine", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun checkIdentity(): Boolean{
        val identity = binding.iden2Tb.text.toString()

        if(identity.length != 10){
            return false
        }
        if(identity[0].code.toByte().toInt() !in 'A'.code.toByte().toInt()..'Z'.code.toByte().toInt()){
            return false
        }
        for(i in 1 until identity.length){
            if(identity[i].code.toByte().toInt() !in '0'.code.toByte().toInt()..'9'.code.toByte().toInt()){
                return false
            }
        }
        return true
    }

    private fun checkName(): Boolean{
        return binding.name2Tb.text.toString().length in 2..30
    }

    private fun checkPhone(): Boolean{
        return binding.phone2Tb.text.toString().length == 10
    }

    private fun checkAddress(): Boolean{
        return binding.address2Tb.text.toString().length >= 9
    }

    private fun checkVaccineAddress(): Boolean{
        return binding.vacAddressTb.text.toString() != ""
    }

    private fun checkCheckedTrue(): Boolean{
        return binding.azCb.isChecked || binding.bntCb.isChecked || binding.mdaCb.isChecked || binding.mvcCb.isChecked
    }

    override fun onResume() {
        super.onResume()

        val vL = Navigation.findNavController(binding.root).currentBackStackEntry?.savedStateHandle?.get<String>("VaccineLocation")
        if(vL != null){
            val vaccineLocation = JSONObject(vL)
            vaccine!!.vaccineLocation = vaccineLocation
            viewModel.setVaccine(vaccine)

            binding.vacAddressTb.setText(vaccine!!.vaccineLocation?.getString("Name"))
        }
    }
}