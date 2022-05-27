package com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var _binding: FragmentProfileBinding
    private val binding get() = _binding
    private lateinit var user: UserItems

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupAction(){

    }

    private fun setupViewModel(){

    }
}