package com.daniza.simple.todolist.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.daniza.simple.todolist.TodoApplication
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.model.TaskTypeModel
import com.daniza.simple.todolist.data.source.Result
import com.daniza.simple.todolist.data.source.TaskRepository
import com.daniza.simple.todolist.data.source.TaskUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class MainViewModel(
    private val repository: TaskRepository
) : ViewModel() {
    val allTasksTypeData: Flow<TaskUiState<TaskTypeModel>>
        get() = repository.observeTypes()
            .map { result ->
                when (result) {
                    is Result.Error -> TaskUiState(isError = true)
                    is Result.Success -> TaskUiState(result.data)

                    Result.Loading -> TaskUiState(isLoading = true)
                }
            }

    /*its okay to make all saved data always hot*/
    //val allListData : Flow<Result<List<TaskModel>>> get() = repository.observeTasks()

    val allListData: SharedFlow<TaskUiState<TaskModel>>
        get() = repository.observeTasks()
            .map { result ->
                when (result) {
                    is Result.Loading -> TaskUiState<TaskModel>(isLoading = true)
                    is Result.Success -> TaskUiState(
                        dataList = result.data.sortedWith(
                            compareBy({ it.checked }, { it.id })
                        )
                    )

                    is Result.Error -> TaskUiState(isError = true)
                }
            }.shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed()
            )

    val singleTask: TaskModel
        get() = updateStateTask(null) ?: TaskModel(id = 0) // dummy

    fun updateStateTask(task: TaskModel?): TaskModel? = task


    fun updateCheckedTask(task: TaskModel, check: Boolean) {
        task.isFinished = check
        task.checked = check
        repository.updateTask(task)
    }

    /*the primary information in todolist is title*/
    fun saveNewTask(task: TaskModel) {
        repository.saveTask(task)
    }

    fun editTask(task: TaskModel, newTask: TaskModel) {
        newTask.apply {
            id = task.id
            dateCreated = task.dateCreated
            isFinished = task.isFinished
        }
        repository.updateTask(newTask)
    }

    /*just delete*/
    fun deleteTask(task: TaskModel) {
        repository.deleteTask(task)
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application: TodoApplication = (this[APPLICATION_KEY]) as TodoApplication
                MainViewModel(application.repository)
            }
        }
    }
}