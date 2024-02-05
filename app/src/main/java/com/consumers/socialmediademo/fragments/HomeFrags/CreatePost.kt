package com.consumers.socialmediademo.fragments.HomeFrags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.consumers.socialmediademo.R
import com.consumers.socialmediademo.databinding.FragmentCreatePostBinding

class CreatePost : Fragment() {
    private lateinit var binding : FragmentCreatePostBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_post,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialiseViews()
    }

    private fun initialiseViews() {

    }
}