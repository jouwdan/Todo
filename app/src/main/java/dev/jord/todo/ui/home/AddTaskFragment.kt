package dev.jord.todo.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.jord.todo.R
import dev.jord.todo.data.model.Task
import dev.jord.todo.databinding.FragmentAddTaskBinding
import dev.jord.todo.databinding.FragmentForgotPasswordBinding
import dev.jord.todo.ui.auth.AuthViewModel
import dev.jord.todo.util.snackbar

@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    val TAG: String = "AddTaskFragment"
    lateinit var binding: FragmentAddTaskBinding
    val viewModel: TaskViewModel by viewModels()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_add_task, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding = FragmentAddTaskBinding.bind(view)

            val priorityArray = resources.getStringArray(R.array.priority)
            val arrayAdapter = activity?.let { ArrayAdapter(it, R.layout.dropdown_item, priorityArray) }
            binding.priorityDropdown.setAdapter(arrayAdapter)

            binding.addTaskButton.setOnClickListener {
                val title = binding.taskName.text.toString()
                val description = binding.taskDescription.text.toString()
                val priority = binding.priorityDropdown.text.toString()
                val task = Task(title = title, description = description, priority = priority)
                viewModel.addTask(task)
                snackbar("Task added successfully!")
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, HomeFragment())
                    ?.commit();
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}