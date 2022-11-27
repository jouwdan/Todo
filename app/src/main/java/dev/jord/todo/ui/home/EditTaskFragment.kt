package dev.jord.todo.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.jord.todo.R
import dev.jord.todo.data.model.Task
import dev.jord.todo.databinding.FragmentEditTaskBinding
import dev.jord.todo.databinding.FragmentHomeBinding
import dev.jord.todo.util.UiState
import dev.jord.todo.util.snackbar

@AndroidEntryPoint
class EditTaskFragment : Fragment() {

    private lateinit var binding: FragmentEditTaskBinding
    private val viewModel: TaskViewModel by viewModels()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_edit_task, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding = FragmentEditTaskBinding.bind(view)
        }

    private fun observer() {
        viewModel.getTask.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    snackbar(state.data.toString())
                }
                is  UiState.Failure -> {
                }
                is  UiState.Loading -> {
                }
                else -> {}
            }
        }
    }
}