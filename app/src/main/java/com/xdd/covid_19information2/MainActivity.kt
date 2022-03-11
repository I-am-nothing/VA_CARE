package com.xdd.covid_19information2

import android.app.Notification
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.xdd.covid_19information2.adapter.covidinformation.CovidInformation
import com.xdd.covid_19information2.databinding.LoginFragmentBinding
import com.xdd.covid_19information2.databinding.MainActivityBinding
import com.xdd.covid_19information2.method.Covid19Information
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private var actionBar = ActionBar.Hide

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.toolbarCancelBtn.setOnClickListener{
            onBackPressed()
        }

        Covid19Information(this).checkStatus{
            if(!it){
                Snackbar.make(binding.root, "Please Check out your internet connection.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    fun toolbarSettingClick(click: () -> Unit){
        binding.toolbarSettingBtn.setOnClickListener{
            click()
        }
    }

    fun toolbarAddingClick(click: () -> Unit){
        binding.toolbarAddBtn.setOnClickListener{
            click()
        }
    }

    override fun onBackPressed() {
        when(actionBar){
            ActionBar.Hide -> {
                finish()
            }
            ActionBar.NoCancel -> {
                finish()
            }
            ActionBar.Settings -> {

            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    fun setActionBar(actionBar: ActionBar, title: String?){
        this.actionBar = actionBar
        binding.toolbarTv.text = title

        when(actionBar){
            ActionBar.Adding -> {
                supportActionBar?.show()
                binding.toolbarCancelBtn.visibility = View.VISIBLE
                binding.toolbarSettingBtn.visibility = View.GONE
                binding.toolbarAddBtn.visibility = View.VISIBLE
            }
            ActionBar.Cancel -> {
                supportActionBar?.show()
                binding.toolbarCancelBtn.visibility = View.VISIBLE
                binding.toolbarSettingBtn.visibility = View.GONE
                binding.toolbarAddBtn.visibility = View.GONE
            }
            ActionBar.Hide -> {
                supportActionBar?.hide()
                binding.toolbarCancelBtn.visibility = View.GONE
                binding.toolbarSettingBtn.visibility = View.GONE
                binding.toolbarAddBtn.visibility = View.GONE
            }
            ActionBar.NoCancel -> {
                supportActionBar?.show()
                binding.toolbarCancelBtn.visibility = View.GONE
                binding.toolbarSettingBtn.visibility = View.GONE
                binding.toolbarAddBtn.visibility = View.GONE
            }
            ActionBar.Settings -> {
                supportActionBar?.show()
                binding.toolbarCancelBtn.visibility = View.GONE
                binding.toolbarSettingBtn.visibility = View.VISIBLE
                binding.toolbarAddBtn.visibility = View.GONE
            }
        }
    }
}

enum class ActionBar{
    Hide,
    NoCancel,
    Settings,
    Adding,
    Cancel
}