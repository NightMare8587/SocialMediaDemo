package com.consumers.socialmediademo.fragments.HomeFrags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.consumers.socialmediademo.R
import com.consumers.socialmediademo.databinding.FragmentChatBinding

class Chat : Fragment() {
    private lateinit var binding : FragmentChatBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialiseViews()
    }

    private fun initialiseViews() {
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext())
    }
}