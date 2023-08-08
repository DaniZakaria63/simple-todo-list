package com.daniza.simple.todolist.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.daniza.simple.todolist.TodoApplication
import com.daniza.simple.todolist.data.model.StatisticsModel
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.model.TaskTypeModel
import com.daniza.simple.todolist.data.source.Result
import com.daniza.simple.todolist.data.source.TaskRepository
import com.daniza.simple.todolist.data.source.TaskUiState
import com.daniza.simple.todolist.ui.theme.CardColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: TaskRepository
) : ViewModel() {
    private val _allTasksTypeData: MutableStateFlow<TaskUiState<TaskTypeModel>> =
        MutableStateFlow(TaskUiState(isLoading = true))

    val allTasksTypeData: Flow<TaskUiState<TaskTypeModel>>
        get() = _allTasksTypeData


    private val _singleTasksTypeData: MutableStateFlow<TaskUiState<TaskTypeModel>> =
        MutableStateFlow(TaskUiState(isLoading = true))
    val singleTasksTypeData: Flow<TaskUiState<TaskTypeModel>> get() = _singleTasksTypeData


    init {
        viewModelScope.launch {
            launch {
                repository.observeTypes()
                    .map { result -> parseResult(result, LIST_DATA){
                        it.sortedByDescending { item -> item.id }
                    }
                    }.collect {
                        _allTasksTypeData.value = it
                    }
            }
        }
    }


    fun getOneTaskType(taskId: Int) {
        viewModelScope.launch {
            repository.getTaskTypeOne(taskId).map { result ->
                parseResult(result, SINGLE_DATA)
            }.collect {
                _singleTasksTypeData.value = it
            }
        }
    }


    private fun <T> parseResult(result: Result<T>, type: Int, callback:(List<TaskTypeModel>) -> List<TaskTypeModel>? = {null}): TaskUiState<TaskTypeModel> {
        return when (result) {
            is Result.Success -> TaskUiState(
                dataSingle = if (type == SINGLE_DATA) result.data as TaskTypeModel else null,
                dataList = if (type == LIST_DATA) callback(result.data as List<TaskTypeModel>) else null,
            )

            is Result.Error -> TaskUiState(isError = true)
            Result.Loading -> TaskUiState(isLoading = true)
        }
    }


    fun saveNewTaskType(type: TaskTypeModel) {
        try{
            repository.saveTaskType(type)
        }catch (e: IllegalArgumentException){
            Log.e("ASD", "saveNewTaskType: ", e)
        }
    }


    fun deleteTaskType(type: TaskTypeModel) {
        repository.deleteTaskType(type)
    }


    /*its okay to make all saved data always hot*/
    /*val allListData : Flow<Result<List<TaskModel>>> get() = repository.observeTasks()
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
     */

    fun updateCheckedTask(task: TaskModel, check: Boolean) {
        task.isFinished = check
        task.checked = check
        repository.updateTask(task)
    }

    /*the primary information in todolist is title*/
    fun saveNewTask(typeId: Int, task: TaskModel) {
        task.type_id = typeId
        repository.saveTask(task)
    }

    fun editTask(typeId: Int, task: TaskModel, newTask: TaskModel) {
        newTask.apply {
            id = task.id
            type_id = typeId
            dateCreated = task.dateCreated
            isFinished = task.isFinished
        }
        repository.updateTask(newTask)
    }

    /*just delete*/
    fun deleteTask(task: TaskModel) {
        repository.deleteTask(task)
    }

    fun updateColorValue(typeModel: TaskTypeModel, color: CardColor) {
        typeModel.color = color
        repository.updateTypeColorValue(typeModel)
    }

    /* Statistical listener*/
    val allStatisticsData: Flow<TaskUiState<StatisticsModel>>
        get() =
            repository.provideStatisticsData()
                .map { item -> TaskUiState(dataList = item) }
                .catch { TaskUiState<StatisticsModel>(isError = true) }
                .shareIn(viewModelScope, SharingStarted.Lazily)

    companion object {
        private const val SINGLE_DATA = 1
        private const val LIST_DATA = 2
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application: TodoApplication = (this[APPLICATION_KEY]) as TodoApplication
                MainViewModel(application.repository)
            }
        }
    }
}