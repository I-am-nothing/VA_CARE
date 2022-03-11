package com.xdd.covid_19information2.ui.changepassword

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.databinding.ChangePasswordFragmentBinding
import com.xdd.covid_19information2.method.Covid19Information

class ChangePasswordFragment : Fragment() {

    private var _binding: ChangePasswordFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChangePasswordFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)

        binding.update3Btn.setOnClickListener {
            when{
                !checkPassword(binding.pwd3Tb.text.toString()) -> Snackbar.make(binding.root, "Your password is invalid", Snackbar.LENGTH_SHORT).show()
                !checkPassword(binding.newPwdTb.text.toString()) -> Snackbar.make(binding.root, "Your new password is invalid", Snackbar.LENGTH_SHORT).show()
                binding.conPwd2Tb.text.toString() != binding.newPwdTb.text.toString() -> Snackbar.make(binding.root, "Your confirm password is not same as new password", Snackbar.LENGTH_SHORT).show()
                else -> {
                    val dialog = AlertDialog.Builder(binding.root.context)
                        .setCancelable(false)
                        .setView(R.layout.loading_view)
                        .create()

                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.show()

                    Covid19Information(requireActivity()).updateAccountPassword(binding.pwd3Tb.text.toString(), binding.newPwdTb.text.toString()){
                        dialog.dismiss()
                        if(it){
                            requireActivity().runOnUiThread {
                                Snackbar.make(binding.root, "Update Success", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            requireActivity().runOnUiThread {
                                Snackbar.make(binding.root, "Update Failed, Because your password is incorrect", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun checkPassword(text: String): Boolean {
        var upperChar = false
        var lowerChar = false
        var number = false
        for (i in text) {
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

        return (text.length in 8..20) && upperChar && lowerChar && number
    }
}