package ru.zatsoft.mynet.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.zatsoft.mynet.model.FeedState
import ru.zatsoft.mynet.R
import ru.zatsoft.mynet.util.AndroidUtils
import ru.zatsoft.mynet.databinding.FragmentSignInBinding

import ru.zatsoft.mynet.viewmodel.AuthViewModel

class SignInFragment: Fragment() {

    private val viewModel: AuthViewModel by viewModels(
        ownerProducer = ::requireParentFragment)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignInBinding.inflate(
            inflater,
            container,
            false)
        binding.button.setOnClickListener {
            viewModel.signUser(binding.login.text.toString(), binding.pass.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
        }

        viewModel.authCreated.observe(viewLifecycleOwner) {
            if(viewModel.dataState.value == FeedState.Error){
                Snackbar.make(binding.root , "${R.string.authError}", Snackbar.LENGTH_LONG).show()
            }
            viewModel.loadPosts()
            findNavController().navigateUp()
        }

        return binding.root
    }
}
