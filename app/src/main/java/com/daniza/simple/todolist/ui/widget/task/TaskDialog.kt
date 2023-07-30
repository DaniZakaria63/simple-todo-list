@file:OptIn(ExperimentalMaterial3Api::class)

package com.daniza.simple.todolist.ui.widget.task

import android.util.Log
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.model.TaskTypeModel
import com.daniza.simple.todolist.data.model.toDateString
import com.daniza.simple.todolist.data.source.Status
import com.daniza.simple.todolist.ui.theme.CardColor
import com.daniza.simple.todolist.ui.widget.common.CustomColorSelector

enum class TaskDialogType {
    NOTHING, NEW, EDIT, DELETE, TASK_DELETE, COLOR_PICKER
}

@Composable
fun TaskTypeDialog(
    callback: (TaskTypeModel?, Status) -> Unit
) {
    var title by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var cardColor by remember {
        mutableStateOf(CardColor.NONE)
    }
    Dialog(onDismissRequest = { callback(null, Status.ERROR) }) {
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "New Category of Task",
                    modifier = Modifier.padding(top = 24.dp, bottom = 20.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = {
                        Text(text = "Title")
                    },
                    shape = OutlinedTextFieldDefaults.shape,
                )
                Text(text = "Card Color", modifier = Modifier.padding(top = 24.dp, bottom = 8.dp))
                CustomColorSelector(
                    modifier = Modifier.padding(top = 8.dp),
                    value = CardColor.NONE,
                    colorChanged = { color -> cardColor = color })
                Button(onClick = {
                    callback(TaskTypeModel(name = title.text, color = cardColor), Status.DATA)
                }, modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                    Text(text = "Add Category")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
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
                type = type,
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
    type: TaskDialogType,
    title: TextFieldValue,
    description: TextFieldValue,
    duedate: String,
    onButtonClicked: (TaskModel, Status) -> Unit,
    onTitleChange: (TextFieldValue) -> Unit,
    onDescChange: (TextFieldValue) -> Unit,
) {
    Dialog(onDismissRequest = { onButtonClicked(TaskModel(id = 0), Status.ERROR) }) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 3.dp
        ) {
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
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .weight(1f)
                                .align(alignment = Alignment.CenterVertically)
                        )
                        TextButton(onClick = { showDatePicker = true }) {
                            Text(text = "Select Due Date")
                        }

                    }

                    Spacer(modifier = Modifier.padding(vertical = 12.dp))

                    Button(
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        onClick = {
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
                        },
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "add_new_list")
                        Text(
                            text = if (type == TaskDialogType.NEW) "Add To List" else "Update The Task",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}