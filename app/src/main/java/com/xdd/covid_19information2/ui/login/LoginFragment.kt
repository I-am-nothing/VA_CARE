package com.xdd.covid_19information2.ui.login

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.UiThread
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.xdd.covid_19information2.ActionBar
import com.xdd.covid_19information2.MainActivity
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.databinding.LoginFragmentBinding
import com.xdd.covid_19information2.method.Covid19Information

class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        Handler(Looper.getMainLooper()).postDelayed({
            (requireActivity() as MainActivity).setActionBar(ActionBar.Hide, null)
        }, 100)

        requireActivity().onBackPressedDispatcher.addCallback(this){
            
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pwdViewBtn.setOnClickListener{
            if(binding.pwdTb.transformationMethod == null){
                binding.pwdTb.transformationMethod = PasswordTransformationMethod()
                binding.pwdViewBtn.setImageResource(R.drawable.ic_round_visibility_off_24)
            }
            else{
                binding.pwdTb.transformationMethod = null
                binding.pwdViewBtn.setImageResource(R.drawable.ic_round_visibility_24)
            }
        }

        binding.signUpBtn.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.loginBtn.setOnClickListener{
            if(binding.emailTb.text.toString().length in 8..50 && binding.pwdTb.text.toString().length in 8..20){
                val dialog = AlertDialog.Builder(binding.root.context)
                    .setCancelable(false)
                    .setView(R.layout.loading_view)
                    .create()

                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.show()

                Covid19Information(requireActivity()).login(binding.emailTb.text.toString(), binding.pwdTb.text.toString()){
                    dialog.dismiss()
                    if(it == null){
                        Snackbar.make(binding.root, "Account or password not correct", Snackbar.LENGTH_SHORT).show()
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
}