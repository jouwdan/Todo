package dev.jord.todo.data.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    @DocumentId var id: String = "", // This is the id of the document in the database
    var title: String = "", // This is the title of the task
    var description: String = "", // This is the description of the task
    var priority: String = "", // Low, Medium, High
    var dueDate: String = "", // 2021-01-01
    var location: String = "", // 123.123, 123.123
    var completed: Boolean = false // true or false
) : Parcelable
