package dev.jord.todo.data.repository

import dev.jord.todo.data.model.Task
import dev.jord.todo.util.UiState

interface TaskRepository {
    fun addTask(task: Task, result: (UiState<String>) -> Unit)
    fun updateTask(task: Task, result: (UiState<String>) -> Unit)
    fun deleteTask(task: Task, result: (UiState<String>) -> Unit)
    fun getTask(id: String, result: (UiState<String>) -> Unit)
    fun getTasks(result: (UiState<List<Task>>) -> Unit)
    fun storeTasks(tasks: List<Task>, result: (UiState<String>) -> Unit)
}