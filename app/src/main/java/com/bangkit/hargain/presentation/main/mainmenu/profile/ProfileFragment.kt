package com.bangkit.hargain.presentation.main.mainmenu.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.hargain.databinding.FragmentProfileBinding
import com.bangkit.hargain.presentation.main.MainActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = (activity as MainActivity).getCurrentUser()
        binding?.nameTextView?.text = currentUser?.name ?: "-"
        binding?.emailTextView?.text = currentUser?.email ?: "-"

        binding?.signOutButton?.setOnClickListener {
            (activity as MainActivity).signOut()
        }
    }
}