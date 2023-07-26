@file:OptIn(ExperimentalMaterial3Api::class)

package com.daniza.simple.todolist.ui.widget.task

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

enum class TaskDialogType {
    NEW, EDIT, DELETE
}

@Composable
fun TaskDialog(
    type: TaskDialogType,
    taskModel: TaskModel,
    callback: (TaskModel, Status) -> Unit
) {
    var title by remember {
        mutableStateOf(TextFieldValue(taskModel.title))
    }

    var description by remember {
        mutableStateOf(TextFieldValue(taskModel.description))
    }

    var duedate by remember {
        mutableStateOf(TextFieldValue("2023-7-26")) // need to implement date picker ASAP
    }

    when (type) {
        TaskDialogType.DELETE -> DialogDeleteTask(taskModel = taskModel, onButtonClicked = callback)
        else ->
            DialogFormTask(
                title = title,
                description = description,
                duedate = duedate,
                onButtonClicked = callback,
                onTitleChange = { title = it },
                onDescChange = { description = it },
                onDateChange = { duedate = it },
            )
    }
}


@Composable
private fun DialogDeleteTask(
    taskModel: TaskModel,
    onButtonClicked: (TaskModel, Status) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onButtonClicked(taskModel, Status.ERROR) },
        title = {
            Text(text = "The action cannot be undone")
        },
        text = {
            Column {
                Text(text = "Are you sure want to delete")
                Text(text = taskModel.title)
            }
        },
        confirmButton = {
            TextButton(onClick = { onButtonClicked(taskModel, Status.DATA) }) {
                Text(text = "Sure")
            }
        },
        dismissButton = {
            TextButton(onClick = { onButtonClicked(taskModel, Status.ERROR) }) {
                Text(text = "No")
            }
        }
    )
}

@Composable
private fun DialogFormTask(
    title: TextFieldValue,
    description: TextFieldValue,
    duedate: TextFieldValue,
    onButtonClicked: (TaskModel, Status) -> Unit,
    onTitleChange: (TextFieldValue) -> Unit,
    onDescChange: (TextFieldValue) -> Unit,
    onDateChange: (TextFieldValue) -> Unit,
) {
    Dialog(onDismissRequest = { onButtonClicked(TaskModel(id = 0), Status.ERROR) }) {
        Surface(shape = RoundedCornerShape(12.dp), color = Color.White) {
            Box(contentAlignment = Alignment.Center) {
                Column(modifier = Modifier.padding(24.dp)) {
                    TextField(value = title, onValueChange = onTitleChange, label = {
                        Text(text = "Title")
                    })

                    Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 12.dp))

                    TextField(value = description, onValueChange = onDescChange, label = {
                        Text(text = "Description")
                    })

                    Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 12.dp))

                    TextField(
                        value = duedate,
                        onValueChange = onDateChange,
                        enabled = false,
                        label = {
                            Text(text = "Due Date")
                        },
                        placeholder = {
                            Text(text = "Today")
                        })

                    Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 12.dp))

                    Button(onClick = {
                        onButtonClicked(
                            TaskModel(
                                title = title.text,
                                description = description.text,
                                dueDate = duedate.text
                            ),
                            if (title.text.isEmpty() || title.text.equals("-")) {
                                Status.ERROR
                            } else {
                                Status.DATA
                            }
                        )
                    }) {
                        Text(text = "Done")
                    }
                }
            }
        }
    }
}