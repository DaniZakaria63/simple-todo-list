package com.daniza.simple.todolist.ui.main

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.model.TaskTypeModel
import com.daniza.simple.todolist.data.source.Status
import com.daniza.simple.todolist.data.source.TaskUiState
import com.daniza.simple.todolist.ui.splash.SplashScreen
import com.daniza.simple.todolist.ui.widget.common.ErrorScreen
import com.daniza.simple.todolist.ui.widget.common.LoadingScreen
import com.daniza.simple.todolist.ui.widget.task_type.TaskTypeCardList

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    toSingleTask: (Int) -> Unit,
) {
    Surface(color = MaterialTheme.colorScheme.primary) {
        var showSplashScreen by remember { mutableStateOf(false) } // default is false
        if (showSplashScreen) {
            SplashScreen {
                showSplashScreen = false
            }
        } else {
            val dummy_task = listOf(
                TaskModel(title = "test 1"),
                TaskModel(title = "test 2")
            )
            val dummy_type = listOf(
                TaskTypeModel(name = "one", _task_list = dummy_task),
                TaskTypeModel(name = "two", _task_list = dummy_task)
            )

            TodoListScene(
                mainViewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
                onCardClicked = toSingleTask
            )
        }
    }
}


@Composable
private fun TodoListScene(
    modifier: Modifier = Modifier,
    onCardClicked: (Int) -> Unit,
    mainViewModel: MainViewModel = viewModel()
) {

    var showDialog by remember { mutableStateOf("") }
    var stateTask by remember { mutableStateOf(TaskModel()) }
    val listTaskType: TaskUiState<TaskTypeModel> by mainViewModel.allTasksTypeData.collectAsStateWithLifecycle(
        initialValue = TaskUiState<TaskTypeModel>(isLoading = true)
    )

    when (listTaskType.status) {
        Status.DATA -> TodoTaskTypeContent(
            listTaskType = listTaskType.dataList!!,
            onCardClicked = onCardClicked,
            onCheckChanged = { task, b ->
                mainViewModel.updateCheckedTask(task, b)
            },
        )

        Status.ERROR -> ErrorScreen(
            message = "Refresh please",
            onTimeout = { }
        )

        Status.LOADING -> LoadingScreen()
    }
}


@Composable
private fun TodoTaskTypeContent(
    listTaskType: List<TaskTypeModel> = listOf(),
    onCardClicked: (Int) -> Unit,
    onCheckChanged: (TaskModel, Boolean) -> Unit,
) {
    Log.i("ASD", "TodoTaskTypeContent: $listTaskType")
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.padding(24.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Tasks")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color.Gray,
                                fontWeight = FontWeight.Light
                            )
                        ) {
                            append(" Lists")
                        }
                    },
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                )
//                Divider(color = Color.Black) // TODO: Update this into similar like the mockup
            }

            Spacer(modifier = Modifier.padding(24.dp))
            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, Color.Gray),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
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
                color = Color.Gray,
                modifier = Modifier.padding(top = 12.dp, bottom = 32.dp)
            )

            Spacer(modifier = Modifier.padding(32.dp))
            if (listTaskType.isEmpty()) {
                Text(
                    text = "You haven't add any list yet",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium
                )
            } else {
                LazyRow {
                    items(items = listTaskType) { taskType ->
                        TaskTypeCardList(
                            taskType = taskType,
                            items = taskType.task_list,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 8.dp)
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
