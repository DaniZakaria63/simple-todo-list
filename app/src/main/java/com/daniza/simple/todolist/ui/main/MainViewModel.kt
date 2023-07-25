package com.daniza.simple.todolist.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.daniza.simple.todolist.TodoApplication
import com.daniza.simple.todolist.data.TodoRepository
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.source.Result
import com.daniza.simple.todolist.ui.widget.task.TaskUiState
import com.daniza.simple.todolist.ui.widget.task.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

/*Several Todos:
* - for testing purpose, use FakeTodoRepository and FakeDataSource
* - making sure all the test phase got passed
* */
class MainViewModel(
    private val repository: TodoRepository
) : ViewModel() {
    /*its okay to make all saved data always hot*/
//    val allListData : Flow<Result<List<TaskModel>>> get() = repository.observeTasks()
    val allListData: SharedFlow<TaskUiState> get() = repository.observeTasks()
        .map { result->
            when (result){
                is Result.Loading -> TaskUiState(isLoading = true)
                is Result.Success -> TaskUiState(taskDatas = result.data)
                is Result.Error -> TaskUiState(isError = true)
            }
        }.shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed()
        )


    fun updateCheckedTask(task: TaskModel, check: Boolean) {

    }

    fun saveNewTask(task: TaskModel){

    }

    fun editTask(task: TaskModel){

    }

    fun deleteTask(task: TaskModel){

    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application: TodoApplication =(this[APPLICATION_KEY]) as TodoApplication
                MainViewModel(application.repository)
            }
        }
    }
}