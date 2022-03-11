package com.xdd.covid_19information2.ui.signup

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
import com.google.android.material.snackbar.Snackbar
import com.xdd.covid_19information2.ActionBar
import com.xdd.covid_19information2.MainActivity
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.databinding.SignUpFragmentBinding
import com.xdd.covid_19information2.method.Covid19Information

class SignUpFragment : Fragment() {

    private var _binding: SignUpFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignUpFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        (requireActivity() as MainActivity).setActionBar(ActionBar.Cancel, "Register")

        binding.regisBtn.setOnClickListener {
            when {
                !checkEmail() -> {
                    Snackbar.make(binding.root, "Your email's length is not in 8 to 50", Snackbar.LENGTH_SHORT).show()
                }
                !checkIdentity() -> {
                    Snackbar.make(binding.root, "Your identity id is incorrect", Snackbar.LENGTH_SHORT).show()
                }
                !checkPassword() -> {
                    Snackbar.make(binding.root, "Password must have upper char, lower char and number", Snackbar.LENGTH_SHORT).show()
                }
                !confirmPassword() -> {
                    Snackbar.make(binding.root, "Password and confirm password is not same", Snackbar.LENGTH_SHORT).show()
                }
                !checkName() -> {
                    Snackbar.make(binding.root, "Your name's length is not in 2 to 30", Snackbar.LENGTH_SHORT).show()
                }
                !checkPhone() -> {
                    Snackbar.make(binding.root, "Your phone number's length is not 10", Snackbar.LENGTH_SHORT).show()
                }
                !checkAddress() -> {
                    Snackbar.make(binding.root, "Your address' length must have at least 9 characters", Snackbar.LENGTH_SHORT).show()
                }
                else -> {
                    val dialog = AlertDialog.Builder(binding.root.context)
                        .setCancelable(false)
                        .setView(R.layout.loading_view)
                        .create()

                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.show()

                    Covid19Information(requireActivity()).register(binding.email2Tb.text.toString(), binding.pwd2Tb.text.toString(), binding.idenTb.text.toString(), binding.nameTb.text.toString(), binding.phoneTb.text.toString(), binding.addressTb.text.toString()){
                        dialog.dismiss()
                        if(it == null){
                            Snackbar.make(binding.root, "This account is already exit", Snackbar.LENGTH_SHORT).show()
                        }
                        else{
                            Snackbar.make(requireView(), "Welcome", Snackbar.LENGTH_SHORT).show()
                            requireActivity().runOnUiThread {
                                Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_mainFragment)
                            }
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun checkPassword(): Boolean{
        val password = binding.pwd2Tb.text.toString()
        var upperChar = false
        var lowerChar = false
        var number = false
        for(i in password){
            when {
                i.code.toByte().toInt() in 'a'.code.toByte().toInt()..'z'.code.toByte().toInt() -> {
                    lowerChar = true
                }
                i.code.toByte().toInt() in 'A'.code.toByte().toInt()..'Z'.code.toByte().toInt() -> {
                    upperChar = true
                }
                i.code.toByte().toInt() in '0'.code.toByte().toInt()..'9'.code.toByte().toInt() -> {
                    number = true
                }
            }
        }

        return (password.length in 8..20) && upperChar && lowerChar && number
    }

    private fun confirmPassword(): Boolean{
        return binding.pwd2Tb.text.toString() == binding.conPwdTb.text.toString()
    }

    private fun checkEmail(): Boolean{
        return (binding.email2Tb.text.toString().length in 8..50)
    }

    private fun checkIdentity(): Boolean{
        val identity = binding.idenTb.text.toString()

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
        return binding.nameTb.text.toString().length in 2..30
    }

    private fun checkPhone(): Boolean{
        return binding.phoneTb.text.toString().length == 10
    }

    private fun checkAddress(): Boolean{
        return binding.addressTb.text.toString().length >= 9
    }
}