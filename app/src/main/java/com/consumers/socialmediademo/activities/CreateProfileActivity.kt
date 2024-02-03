package com.consumers.socialmediademo.activities

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.consumers.socialmediademo.R
import com.consumers.socialmediademo.databinding.ActivityCreateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class CreateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateProfileBinding

    @Inject
    private lateinit var database: FirebaseDatabase

    @Inject
    private lateinit var sharedPreferences: SharedPreferences

    @Inject
    private lateinit var auth: FirebaseAuth

    @Inject
    private lateinit var firestore: FirebaseFirestore

    @Inject
    private lateinit var storage: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)

        setUpClickListeners()
    }

    private fun checkEmptyFieldsWithEdgeCasesAndContinue(): Boolean {
        if (binding.inputFieldUsername.length() == 0) {
            binding.inputFieldUsername.requestFocus()
            binding.inputFieldUsername.error = "Field can't be empty"
            return false
        }
        if (binding.inputFieldAge.length() == 0) {
            binding.inputFieldAge.requestFocus()
            binding.inputFieldAge.error = "Field can't be empty"
            return false
        }
        if (binding.inputFieldFullname.length() == 0) {
            binding.inputFieldFullname.requestFocus()
            binding.inputFieldFullname.error = "Field can't be empty"
            return false
        }
        return true
    }

    private fun setUpClickListeners() {
        binding.profileImage.setOnClickListener {

        }
        binding.createProfileAccount.setOnClickListener {
            if (checkEmptyFieldsWithEdgeCasesAndContinue()) {

            }
        }
    }
}