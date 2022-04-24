package ru.zatsoft.mynet.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.zatsoft.mynet.R
import ru.zatsoft.mynet.databinding.FragmentSignUpBinding
import ru.zatsoft.mynet.util.AndroidUtils
import ru.zatsoft.mynet.viewmodel.AuthViewModel

class SignUpFragment: Fragment() {

    private val viewModel: AuthViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignUpBinding.inflate(
            inflater,
            container,
            false)
        binding.button.setOnClickListener {
            if(binding.pass.text.toString() == binding.passCopy.text.toString()){
            viewModel.signUp(binding.login.text.toString(),
                             binding.pass.text.toString(),
                             binding.name.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            }
            else {
                Toast.makeText(this.context, R.string.wrong_pass, Toast.LENGTH_LONG ).show()
                return@setOnClickListener
            }
        }

        viewModel.authCreated.observe(viewLifecycleOwner) {
            viewModel.loadPosts()
            findNavController().navigateUp()
        }

        return binding.root
    }
}