package com.consumers.socialmediademo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.consumers.socialmediademo.R
import com.consumers.socialmediademo.databinding.ActivityHomeScreenBinding
import com.consumers.socialmediademo.fragments.HomeFrags.Account
import com.consumers.socialmediademo.fragments.HomeFrags.Chat
import com.consumers.socialmediademo.fragments.HomeFrags.Home
import com.google.android.material.navigation.NavigationBarView

class HomeScreen : AppCompatActivity() {
    private lateinit var fm : FragmentManager
    private lateinit var binding : ActivityHomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home_screen)
        fm = supportFragmentManager

        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.homeBottomNavBar.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    fm.beginTransaction().replace(R.id.homeFragmentCV,Home()).commit()
                    return@OnItemSelectedListener true
                }
                R.id.chat -> {
                    fm.beginTransaction().replace(R.id.homeFragmentCV,Chat()).commit()
                    return@OnItemSelectedListener true
                }
                R.id.account -> {
                    fm.beginTransaction().replace(R.id.homeFragmentCV,Account()).commit()
                    return@OnItemSelectedListener true
                }

                else -> {
                    return@OnItemSelectedListener false
                }
            }
        })
    }
}