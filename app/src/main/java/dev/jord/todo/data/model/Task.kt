package dev.jord.todo.data.model

import com.google.firebase.firestore.DocumentId

data class Task(
    @DocumentId var id: String = "", // This is the id of the document in the database
    val title: String = "", // This is the title of the task
    val description: String = "", // This is the description of the task
    val priority: String = "", // Low, Medium, High
    val dueDate: String = "", // 2021-01-01
    val location: String = "", // 123.123, 123.123
    val completed: Boolean = false // true or false
)
