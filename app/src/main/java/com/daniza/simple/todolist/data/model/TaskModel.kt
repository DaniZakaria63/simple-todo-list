package com.daniza.simple.todolist.data.model

import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.daniza.simple.todolist.data.local.task.TaskEntity
import java.text.SimpleDateFormat

data class TaskModel(
    var id: Int = 0,
    var title: String = "-",
    var description: String = "-",
    var dueDate: String = "-",
    var isFinished: Boolean = false,
    var dateCreated: String = "-"
) {
    // local mutable variable for keeping state
    var checked: Boolean by mutableStateOf(isFinished)

    fun asDatabaseModel(): TaskEntity {
        return TaskEntity(id, title, description, dueDate, isFinished, dateCreated)
    }
}

fun Long.toDateString(calendar: Calendar = Calendar.getInstance()) : String{
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    calendar.timeInMillis = this
    return dateFormat.format(calendar.time)
}
