package com.consumers.socialmediademo.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.consumers.socialmediademo.activities.CreateProfileActivity
import com.consumers.socialmediademo.databinding.FragmentLoginsingupBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginSignupFragment : Fragment() {
    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: FragmentLoginsingupBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginsingupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.continueLoginSignupFlow.setOnClickListener {
            if (binding.inputFieldEmailLogin.length() == 0) {
                binding.inputFieldEmailLogin.requestFocus()
                binding.inputFieldEmailLogin.error = "Email required"
                return@setOnClickListener
            }

            if (binding.inputFieldPasswordLogin.length() == 0) {
                binding.inputFieldPasswordLogin.requestFocus()
                binding.inputFieldPasswordLogin.error = "Password required"
                return@setOnClickListener
            }

            continueWithLoginFlow(
                binding.inputFieldEmailLogin.text.toString(),
                binding.inputFieldPasswordLogin.text.toString()
            )
        }
    }

    private fun continueWithLoginFlow(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                loginFlowContinue(email, password, true)
            } else {
                if (it.exception is FirebaseAuthInvalidCredentialsException) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                loginFlowContinue(email, password, false)
                            } else {
                                val snackbar = Snackbar.make(
                                    binding.parentCLlogin,
                                    "Invalid Credentials",
                                    Snackbar.LENGTH_SHORT
                                )
                                snackbar.show()
                            }
                        }
                    return@addOnCompleteListener
                }

                if (it.exception is FirebaseAuthInvalidUserException) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                loginFlowContinue(email, password, true)
                            } else {
                                val snackbar = Snackbar.make(
                                    binding.parentCLlogin,
                                    "Something went wrong :(",
                                    Snackbar.LENGTH_SHORT
                                )
                                snackbar.show()
                            }
                        }
                }
            }
        }
    }

    private fun loginFlowContinue(email: String, password: String, isToastNeeded: Boolean) {
        if (isToastNeeded)
            Toast.makeText(requireContext(), "Login Success", Toast.LENGTH_SHORT).show()
        sharedPreferences.edit().putString("userLoggedIn", "true").apply()
        val userDetails = getUserDetailsMap(email, password)
        sharedPreferences.edit().putString("authDetails", userDetails).apply()

        requireContext().startActivity(
            Intent(
                requireContext(),
                CreateProfileActivity::class.java
            )
        )
        requireActivity().finish()
    }

    private fun getUserDetailsMap(email: String, password: String): String {
        val userDetailsMap = mapOf("email" to email, "password" to password)
        return Gson().toJson(userDetailsMap).toString()
    }

}