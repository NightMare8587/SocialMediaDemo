package com.consumers.socialmediademo

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.consumers.socialmediademo.activities.CreateProfileActivity
import com.consumers.socialmediademo.activities.HomeScreen
import com.consumers.socialmediademo.fragments.LoginSignupFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var fm: FragmentManager

    @Inject
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialiseViews()

        setUpFragments()
    }

    private fun setUpFragments() {
        if (sharedPreferences.contains("userLoggedIn") && sharedPreferences.getString(
                "userLoggedIn",
                ""
            ).equals("true")
        ) {
            if (sharedPreferences.contains("profileDetails")) {
                startActivity(Intent(this, HomeScreen::class.java))
                finish()
                return
            }
            startActivity(Intent(this, CreateProfileActivity::class.java))
            finish()
            return
        }
        fm.beginTransaction().replace(R.id.mainActivityContainer, LoginSignupFragment()).commit()
    }

    private fun initialiseViews() {
        fm = supportFragmentManager
    }
}