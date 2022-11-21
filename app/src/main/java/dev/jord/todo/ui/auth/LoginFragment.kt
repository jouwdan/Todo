package dev.jord.todo.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.jord.todo.R
import dev.jord.todo.databinding.FragmentLoginBinding
import dev.jord.todo.util.*

@AndroidEntryPoint
class LoginFragment : Fragment() {

    val TAG: String = "LoginFragment"
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.login.setOnClickListener {
            if (validation()) {
                viewModel.login(
                    email = binding.username.text.toString(),
                    password = binding.password.text.toString()
                )
            }
        }
    }

    fun observer(){
        viewModel.login.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.loading.show()
                }
                is UiState.Failure -> {
                    binding.loading.hide()
                    snackbar("error")
                }
                is UiState.Success -> {
                    binding.loading.hide()
                    snackbar(state.data)
                    findNavController().navigate(R.id.action_Login_to_Welcome)
                }
            }
        }
    }

    fun validation(): Boolean {
        var isValid = true

        if (binding.username.text.isNullOrEmpty()){
            isValid = false
            snackbar(getString(R.string.enter_email))
        }else{
            if (!binding.username.text.toString().isValidEmail()){
                isValid = false
                snackbar(getString(R.string.invalid_login))
            }
        }
        if (binding.password.text.isNullOrEmpty()){
            isValid = false
            snackbar(getString(R.string.enter_password))
        }else{
            if (binding.password.text.toString().length < 8){
                isValid = false
                snackbar(getString(R.string.invalid_login))
            }
        }
        return isValid
    }

    override fun onStart() {
        super.onStart()
        viewModel.getSession { user ->
            if (user != null){
                findNavController().navigate(R.id.action_Login_to_Home)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}