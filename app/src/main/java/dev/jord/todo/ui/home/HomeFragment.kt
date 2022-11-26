package dev.jord.todo.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.jord.todo.R
import dev.jord.todo.data.model.Task
import dev.jord.todo.databinding.FragmentHomeBinding
import dev.jord.todo.ui.auth.AuthViewModel
import dev.jord.todo.util.UiState
import dev.jord.todo.util.hide
import dev.jord.todo.util.show
import dev.jord.todo.util.snackbar

@AndroidEntryPoint
class HomeFragment : Fragment() {
    val TAG = "HomeFragment"
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: TaskViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    val adapter by lazy {
        TaskAdapter(
            donePressed = { task -> donePressed(task) },
            deletePressed = { task -> deletePressed(task) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (this::binding.isInitialized){
            return binding.root
        }else {
            binding = FragmentHomeBinding.inflate(inflater, container, false)
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addTaskButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, AddTaskFragment())
                ?.commit();
        }
        binding.taskList.layoutManager = LinearLayoutManager(requireContext())
        binding.taskList.adapter = adapter
        authViewModel.getSession {
            viewModel.getTasks(it)
        }
        observer()
    }


    private fun observer() {
        viewModel.tasks.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    adapter.updateList(state.data.toMutableList())
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    snackbar("Error loading tasks")
                }
                else -> {
                    binding.progressBar.hide()
                }
            }
        }
    }

    private fun deletePressed(task: Task) {
        viewModel.deleteTask(task)
        snackbar("Task deleted successfully!")
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, HomeFragment())
            ?.commit();
    }

    private fun donePressed(task: Task) {
        viewModel.doneTask(task)
        snackbar("Task marked as done!")
    }
}