@file:OptIn(ExperimentalMaterial3Api::class)

package com.daniza.simple.todolist.ui.widget.task

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
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
import com.daniza.simple.todolist.data.model.TaskTypeModel
import com.daniza.simple.todolist.data.model.toDateString
import com.daniza.simple.todolist.data.source.Status

enum class TaskDialogType {
    NEW, EDIT, DELETE, TASK_TYPE
}

@Composable
fun TaskTypeDialog(
    callback: (TaskTypeModel?, Status) -> Unit
) {
    var title by remember {
        mutableStateOf(TextFieldValue(""))
    }
    Dialog(onDismissRequest = { callback(null, Status.ERROR) }) {
        Surface(shape = RoundedCornerShape(4.dp), color = Color.White) {
            Column(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "New Category of Task",
                    modifier = Modifier.padding(top = 24.dp, bottom = 20.dp),
                    color = Color.Black
                )
                TextField(value = title, onValueChange = { title = it }, label = {
                    Text(text = "Title")
                })
                Button(onClick = {
                    callback(TaskTypeModel(name = title.text), Status.DATA)
                }, modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                    Text(text = "Add Category")
                }
            }
        }
    }
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

    when (type) {
        TaskDialogType.DELETE -> DialogDeleteTask(taskModel = taskModel, onButtonClicked = callback)
        else ->
            DialogFormTask(
                title = title,
                description = description,
                duedate = taskModel.dueDate,
                onButtonClicked = callback,
                onTitleChange = { title = it },
                onDescChange = { description = it },
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

@ExperimentalMaterial3Api
@Composable
private fun DialogFormTask(
    title: TextFieldValue,
    description: TextFieldValue,
    duedate: String,
    onButtonClicked: (TaskModel, Status) -> Unit,
    onTitleChange: (TextFieldValue) -> Unit,
    onDescChange: (TextFieldValue) -> Unit,
) {
    Dialog(onDismissRequest = { onButtonClicked(TaskModel(id = 0), Status.ERROR) }) {
        Surface(shape = RoundedCornerShape(12.dp), color = Color.White) {
            Box(contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    TextField(value = title, onValueChange = onTitleChange, label = {
                        Text(text = "Title")
                    })

                    Spacer(modifier = Modifier.padding(vertical = 12.dp))

                    TextField(value = description, onValueChange = onDescChange, label = {
                        Text(text = "Description")
                    })

                    Spacer(modifier = Modifier.padding(vertical = 12.dp))

                    var datePickerValue by remember {
                        mutableStateOf(duedate)
                    }
                    var showDatePicker by remember { mutableStateOf(false) }
                    val datePickerState =
                        rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
                    if (showDatePicker) {
                        DatePickerDialog(
                            onDismissRequest = { showDatePicker = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    datePickerState.selectedDateMillis?.let {
                                        datePickerValue = it.toDateString()
                                    }
                                    showDatePicker = false
                                }) {
                                    Text(text = "Select")
                                }
                            }) {
                            DatePicker(state = datePickerState)
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Date: $datePickerValue",
                            color = Color.Black,
                            modifier = Modifier
                                .weight(1f)
                                .align(alignment = Alignment.CenterVertically)
                        )
                        TextButton(onClick = { showDatePicker = true }) {
                            Text(text = "Select Due Date")
                        }

                    }

                    Spacer(modifier = Modifier.padding(vertical = 12.dp))

                    Button(onClick = {
                        onButtonClicked(
                            TaskModel(
                                title = title.text,
                                description = description.text,
                                dueDate = datePickerValue
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