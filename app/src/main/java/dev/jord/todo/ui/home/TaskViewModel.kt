package dev.jord.todo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jord.todo.data.model.Task
import dev.jord.todo.data.repository.TaskRepository
import dev.jord.todo.util.UiState
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    val repository: TaskRepository
): ViewModel() {

    private val TAG = "TaskViewModel"
    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _register
    private val _task = MutableLiveData<UiState<String>>()
    val task: LiveData<UiState<String>>
        get() = _task
    private val _tasks = MutableLiveData<UiState<String>>()
    val tasks: LiveData<UiState<String>>
        get() = _tasks
    private val _updateTask = MutableLiveData<UiState<String>>()
    val updateTask: LiveData<UiState<String>>
        get() = _updateTask
    private val _deleteTask = MutableLiveData<UiState<String>>()
    val deleteTask: LiveData<UiState<String>>
        get() = _deleteTask
    private val _storeTasks = MutableLiveData<UiState<String>>()
    val storeTasks: LiveData<UiState<String>>
        get() = _storeTasks
    private val _getTask = MutableLiveData<UiState<String>>()
    val getTask: LiveData<UiState<String>>
        get() = _getTask
    private val _getTasks = MutableLiveData<UiState<List<Task>>>()
    val getTasks: MutableLiveData<UiState<List<Task>>>
        get() = _getTasks

    fun addTask(task: Task) {
        _task.value = UiState.Loading
        repository.addTask(task) {
            _task.value = it
        }
    }

    fun updateTask(task: Task) {
        _updateTask.value = UiState.Loading
        repository.updateTask(task) {
            _updateTask.value = it
        }
    }

    fun deleteTask(task: Task) {
        _deleteTask.value = UiState.Loading
        repository.deleteTask(task) {
            _deleteTask.value = it
        }
    }

   fun getTask(id: String) {
        _getTask.value = UiState.Loading
        repository.getTask(id) {
            _getTask.value = it
        }
    }

    fun getTasks() {
        _getTasks.value = UiState.Loading
        repository.getTasks {
            _getTasks.value = it
        }
    }

    fun storeTasks(tasks: List<Task>) {
        _storeTasks.value = UiState.Loading
        repository.storeTasks(tasks) {
            _storeTasks.value = it
        }
    }
}