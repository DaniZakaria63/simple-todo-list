package com.daniza.simple.todolist.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.model.TaskTypeModel
import com.daniza.simple.todolist.data.source.Status
import com.daniza.simple.todolist.data.source.TaskUiState
import com.daniza.simple.todolist.ui.splash.SplashScreen
import com.daniza.simple.todolist.ui.widget.common.ErrorScreen
import com.daniza.simple.todolist.ui.widget.common.LoadingScreen
import com.daniza.simple.todolist.ui.widget.task.TaskTypeDialog
import com.daniza.simple.todolist.ui.widget.task_type.TaskTypeCardList

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    toSingleTask: (Int) -> Unit,
) {
    var showSplashScreen by remember { mutableStateOf(false) } // default is false
    if (showSplashScreen) {
        SplashScreen {
            showSplashScreen = false
        }
    } else {
        var showNewDialog by remember { mutableStateOf(false) }
        val listTaskType: TaskUiState<TaskTypeModel> by mainViewModel.allTasksTypeData.collectAsStateWithLifecycle()

        if (showNewDialog) {
            TaskTypeDialog(callback = { task, status ->
                showNewDialog = false
                if (status == Status.DATA) mainViewModel.saveNewTaskType(task!!)
            })
        }

        when (listTaskType.status) {
            Status.DATA -> TodoTaskTypeContent(
                modifier = Modifier.fillMaxSize(),
                listTaskType = listTaskType.dataList!!,
                onCardClicked = toSingleTask,
                onCheckChanged = { task, b ->
                    mainViewModel.updateCheckedTask(task, b)
                },
                onButtonAddClicked = { showNewDialog = true }
            )

            Status.ERROR -> ErrorScreen(
                message = "Refresh please",
                onTimeout = { }
            )

            Status.LOADING -> LoadingScreen()
        }
    }
}

@Composable
private fun TodoTaskTypeContent(
    modifier: Modifier = Modifier,
    listTaskType: List<TaskTypeModel> = listOf(),
    onCardClicked: (Int) -> Unit,
    onCheckChanged: (TaskModel, Boolean) -> Unit,
    onButtonAddClicked: () -> Unit,
) {
    Surface(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.padding(24.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Tasks")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Light
                            )
                        ) {
                            append(" Lists")
                        }
                    },
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Spacer(modifier = Modifier.padding(24.dp))
            Button(
                onClick = onButtonAddClicked,
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, Color.Gray),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "add",
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
            Text(
                text = "Add List",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 12.dp, bottom = 32.dp)
            )

            Spacer(modifier = Modifier.padding(28.dp))
            if (listTaskType.isEmpty()) {
                Text(
                    text = "You haven't add any list yet",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else {
                LazyRow {
                    items(items = listTaskType) { taskType ->
                        TaskTypeCardList(
                            taskType = taskType,
                            items = taskType.task_list,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 8.dp, bottom = 24.dp)
                                .width(220.dp),
                            onCardClicked = onCardClicked,
                            onCheckChange = onCheckChanged
                        )
                    }
                }
            }
        }
    }
}
