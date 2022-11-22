package dev.jord.todo.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.jord.todo.MainActivity
import dev.jord.todo.R
import dev.jord.todo.data.model.User
import dev.jord.todo.databinding.FragmentRegisterBinding
import dev.jord.todo.ui.home.HomeFragment
import dev.jord.todo.util.*

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    val TAG: String = "RegisterFragment"
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.register.setOnClickListener {
            if (validation()){
                viewModel.register(
                    email = binding.email.text.toString(),
                    password = binding.password.text.toString(),
                    user = getUserObj()
                )
            }
        }
    }

    private fun observer() {
        viewModel.register.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.loading.show()
                }
                is UiState.Failure -> {
                    binding.loading.hide()
                    snackbar("Registration error")
                }
                is UiState.Success -> {
                    binding.loading.hide()
                    snackbar(state.data)
                    (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
                    startActivity(Intent(activity, MainActivity::class.java))
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.name.text.isNullOrEmpty()){
            isValid = false
            snackbar(getString(R.string.enter_name))
        }

        if (binding.email.text.isNullOrEmpty()){
            isValid = false
            snackbar(getString(R.string.enter_email))
        }else{
            if (!binding.email.text.toString().isValidEmail()){
                isValid = false
                snackbar(getString(R.string.invalid_email))
            }
        }
        if (binding.password.text.isNullOrEmpty()){
            isValid = false
            snackbar(getString(R.string.enter_password))
        }else{
            if (binding.password.text.toString().length < 8){
                isValid = false
                snackbar(getString(R.string.invalid_password))
            }
        }
        return isValid
    }

    private fun getUserObj(): User {
        return User(
            id = "",
            name = binding.name.text.toString(),
            email = binding.email.text.toString(),
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}