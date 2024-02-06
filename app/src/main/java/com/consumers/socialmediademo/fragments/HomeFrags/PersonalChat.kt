package com.consumers.socialmediademo.fragments.HomeFrags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.consumers.socialmediademo.R
import com.consumers.socialmediademo.databinding.FragmentPersonalChatBinding

class PersonalChat : Fragment() {
    private lateinit var binding : FragmentPersonalChatBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_chat,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialiseViews()
    }

    private fun initialiseViews() {
        binding.personalChatRV.layoutManager = LinearLayoutManager(requireContext())
    }
}