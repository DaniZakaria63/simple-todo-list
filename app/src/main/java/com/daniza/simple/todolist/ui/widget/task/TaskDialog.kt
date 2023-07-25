@file:OptIn(ExperimentalMaterial3Api::class)

package com.daniza.simple.todolist.ui.widget.task

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.daniza.simple.todolist.data.model.TaskModel


@Composable
fun DialogDeleteTask(
    taskModel: TaskModel,
    callback: (TaskModel, Status) -> Unit
) {
    // TODO: Create alert dialog with two button callback
}

@Composable
fun DialogEditTask(
    taskModel: TaskModel,
    callback: (TaskModel, Status) -> Unit
) {
    // TODO: Create dialog with input text
}

@Composable
fun DialogNewTask(
    callback: (TaskModel, Status) -> Unit
) {
    var title by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var description by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var duedate by remember {
        mutableStateOf(TextFieldValue("2023-7-26"))
    }

    Dialog(onDismissRequest = { callback(TaskModel(id = 0), Status.ERROR) }) {
        Surface(shape = RoundedCornerShape(12.dp), color = Color.White) {
            Box(contentAlignment = Alignment.Center) {
                Column(modifier = Modifier.padding(24.dp)) {
                    TextField(value = title, onValueChange = { title = it }, label = {
                        Text(text = "Title")
                    })

                    Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 12.dp))

                    TextField(value = description, onValueChange = { description = it }, label = {
                        Text(text = "Description")
                    })

                    Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 12.dp))

                    TextField(
                        value = duedate,
                        onValueChange = { duedate = it },
                        enabled = false,
                        label = {
                            Text(text = "Due Date")
                        },
                        placeholder = {
                            Text(text = "Today")
                        })

                    Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 12.dp))

                    Button(onClick = {
                        callback(
                            TaskModel(
                                title = title.text,
                                description = description.text,
                                dueDate = duedate.text
                            ), Status.DATA
                        )
                    }) {
                        Text(text = "Done")
                    }
                }
            }
        }
    }
}