package com.consumers.socialmediademo.activities

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.consumers.socialmediademo.commons.CONSTANTS
import com.consumers.socialmediademo.databinding.ActivityCreateProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import javax.inject.Inject


class CreateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateProfileBinding

    @Inject
    private lateinit var sharedPreferences: SharedPreferences

    @Inject
    private lateinit var auth: FirebaseAuth

    @Inject
    private lateinit var firestore: FirebaseFirestore

    @Inject
    private lateinit var storage: FirebaseStorage

    private var isUserNameTaken = true
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

    private fun checkForPermissionsProvidedAndContinue(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireMediaPermissionAndroidThirteen()
        } else
            requireMediaPermission()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requireMediaPermissionAndroidThirteen(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(READ_MEDIA_IMAGES),
                CONSTANTS.ANDROID_13_MEDIA_PERMISSION
            )
            false
        }
    }

    private fun requireMediaPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(READ_EXTERNAL_STORAGE),
                CONSTANTS.ANDROID_MEDIA_PERMISSION
            )
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults[0] == RESULT_OK) {
                    openGalleryToChooseMediaForProfile()
                }
            }

            2 -> {
                if (grantResults[0] == RESULT_OK) {
                    openGalleryToChooseMediaForProfile()
                }
            }
        }
    }

    private fun openGalleryToChooseMediaForProfile() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            CONSTANTS.MEDIA_PICKER_INTENT
        )
    }

    private fun setUpClickListeners() {
        binding.inputFieldUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                firestore.collection("Users").whereEqualTo("userName",binding.inputFieldUsername.text.toString()).get().addOnCompleteListener {
                    if(it.isSuccessful){
                        val result = it.result
                        if(!result.isEmpty){
                            binding.inputFieldUsername.error = "Username Already taken"
                            isUserNameTaken = true
                        }else
                            isUserNameTaken = false
                    }
                }
            }

        })
        binding.profileImage.setOnClickListener {
            if (checkForPermissionsProvidedAndContinue()) {
                openGalleryToChooseMediaForProfile()
            }
        }
        binding.createProfileAccount.setOnClickListener {
            if (checkEmptyFieldsWithEdgeCasesAndContinue() && !isUserNameTaken) {
                val createAccountMap = mapOf("userName" to binding.inputFieldUsername.text.toString(), "fullName" to binding.inputFieldFullname.text.toString()
                , "age" to binding.inputFieldAge.text.toString())

                firestore.collection("Users").document(auth.uid.toString()).set(createAccountMap).addOnCompleteListener {
                    if(it.isSuccessful){
                        val snackbar = Snackbar.make(binding.parentCLcreateProfile,"Account Created",Snackbar.LENGTH_SHORT)
                        snackbar.show()
                        sharedPreferences.edit().putString("profileDetails",Gson().toJson(createAccountMap)).apply()

                        continueFlowAndLaunchHome()
                    }
                }
            }
        }
    }

    private fun continueFlowAndLaunchHome() {
        startActivity(Intent(this,HomeScreen::class.java))
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONSTANTS.MEDIA_PICKER_INTENT && resultCode == RESULT_OK && data != null) {
            val uriToBeUploaded = data.data

            if (uriToBeUploaded != null) {
                storage.reference.child(auth.uid.toString()).child("profile_picture")
                    .putFile(uriToBeUploaded)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val snackbar = Snackbar.make(
                                binding.parentCLcreateProfile,
                                "Profle picture uploaded!",
                                Snackbar.LENGTH_SHORT
                            )
                            snackbar.show()
                            val downloadUrl = storage.reference.child(auth.uid.toString())
                                .child("profile_picture").downloadUrl.result

                            sharedPreferences.edit().putString("profile_url",downloadUrl.toString()).apply()

                            Picasso.get().load(downloadUrl).into(binding.profileImage)
                        }
                    }
            }
        }
    }
}