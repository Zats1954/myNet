package ru.zatsoft.mynet.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.zatsoft.mynet.databinding.FragmentInitBinding
import ru.zatsoft.mynet.databinding.FragmentSignInBinding

import ru.zatsoft.mynet.viewmodel.AuthViewModel

class InitFragment: Fragment() {
    private val viewModel: AuthViewModel by viewModels(::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentInitBinding.inflate(
            inflater,
            container,
            false)
       return binding.root}

}