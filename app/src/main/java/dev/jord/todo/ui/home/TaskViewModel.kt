package dev.jord.todo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jord.todo.data.model.Task
import dev.jord.todo.data.model.User
import dev.jord.todo.data.repository.TaskRepository
import dev.jord.todo.util.UiState
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    val repository: TaskRepository
): ViewModel() {

    private val TAG = "TaskViewModel"
    private val _addTask = MutableLiveData<UiState<Pair<Task,String>>>()
    val addTask: LiveData<UiState<Pair<Task,String>>>
        get() = _addTask
    private val _updateTask = MutableLiveData<UiState<Pair<Task,String>>>()
    val updateTask: LiveData<UiState<Pair<Task,String>>>
        get() = _updateTask
    private val _doneTask = MutableLiveData<UiState<Pair<Task,String>>>()
    val doneTask: LiveData<UiState<Pair<Task,String>>>
        get() = _doneTask
    private val _deleteTask = MutableLiveData<UiState<Pair<Task,String>>>()
    val deleteTask: LiveData<UiState<Pair<Task,String>>>
        get() = _deleteTask
    private val _storeTasks = MutableLiveData<UiState<String>>()
    val storeTasks: LiveData<UiState<String>>
        get() = _storeTasks
    private val _getTask = MutableLiveData<UiState<Pair<Task,String>>>()
    val getTask: LiveData<UiState<Pair<Task,String>>>
        get() = _getTask
    private val _getTasks = MutableLiveData<UiState<List<Task>>>()
    val getTasks: MutableLiveData<UiState<List<Task>>>
        get() = _getTasks

    private val _tasks = MutableLiveData<UiState<List<Task>>>()
    val tasks: LiveData<UiState<List<Task>>>
        get() = _tasks

    fun addTask(task: Task) {
        _addTask.value = UiState.Loading
        repository.addTask(task) {
            _addTask.value = it
        }
    }

    fun updateTask(task: Task) {
        _updateTask.value = UiState.Loading
        repository.updateTask(task) {
            _updateTask.value = it
        }
    }

    fun doneTask(task: Task) {
        _doneTask.value = UiState.Loading
        task.completed = !task.completed
        repository.updateTask(task) {
            _doneTask.value = it
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

    fun getTasks(user: User?) {
        _tasks.value = UiState.Loading
        repository.getTasks(user) {
            _tasks.value = it
        }
    }

    fun storeTasks(tasks: List<Task>) {
        _storeTasks.value = UiState.Loading
        repository.storeTasks(tasks) {
            _storeTasks.value = it
        }
    }
}