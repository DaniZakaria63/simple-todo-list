package com.daniza.simple.todolist.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.daniza.simple.todolist.DefaultDispatcherProvider
import com.daniza.simple.todolist.DispatcherProvider
import com.daniza.simple.todolist.TodoApplication
import com.daniza.simple.todolist.data.model.StatisticsModel
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.model.TaskTypeModel
import com.daniza.simple.todolist.data.source.Result
import com.daniza.simple.todolist.data.source.TaskRepository
import com.daniza.simple.todolist.data.source.TaskUiState
import com.daniza.simple.todolist.ui.theme.CardColor
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {
    private val coroutineErrorHandler = CoroutineExceptionHandler { context, exception ->
        Log.e("ASD", "saveNewTaskType: ", exception)
        viewModelScope.launch(dispatcher.main) {
            _errorStatus.emit(value = true)
        }
    }

    private val errorHandler: (() -> Unit) -> Unit = { emit ->
        viewModelScope.launch(dispatcher.main) {
            try {
                emit()
            }catch (e: Throwable){
                _errorStatus.emit(true)
            }
        }
    }

    private val _allTasksTypeData: MutableStateFlow<TaskUiState<TaskTypeModel>> =
        MutableStateFlow(TaskUiState(isLoading = true))

    val allTasksTypeData: StateFlow<TaskUiState<TaskTypeModel>>
        get() = _allTasksTypeData.asStateFlow()

    private val _singleTasksTypeData: MutableStateFlow<TaskUiState<TaskTypeModel>> =
        MutableStateFlow(TaskUiState(isLoading = true))
    val singleTasksTypeData: StateFlow<TaskUiState<TaskTypeModel>> get() = _singleTasksTypeData.asStateFlow()

    private val _errorStatus = MutableSharedFlow<Boolean>(replay = 0)
    val errorStatus = _errorStatus.asSharedFlow()

    init {
        getAllTaskType()
    }

    private fun getAllTaskType() {
        viewModelScope.launch(dispatcher.main) {
            _allTasksTypeData.value = TaskUiState(isLoading = true)
            repository.observeTypes().flowOn(dispatcher.io)
                .map { result ->
                    parseResult(result, LIST_DATA) {
                        it.sortedByDescending { item -> item.id }
                    }
                }.collect {
                    _allTasksTypeData.value = it
                }
        }
    }


    fun getOneTaskType(taskId: Int) {
        viewModelScope.launch(dispatcher.main) {
            repository.getTaskTypeOne(taskId).flowOn(dispatcher.io)
                .map { result ->
                    parseResult(result, SINGLE_DATA)
                }.collect {
                    _singleTasksTypeData.value = it
                }
        }
    }


    private fun <T> parseResult(
        result: Result<T>,
        type: Int,
        callback: (List<TaskTypeModel>) -> List<TaskTypeModel>? = { null }
    ): TaskUiState<TaskTypeModel> {
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
        errorHandler {
            repository.saveTaskType(type)
        }
    }


    fun deleteTaskType(type: TaskTypeModel) {
        errorHandler {
            repository.deleteTaskType(type)
        }
    }


    /*its okay to make all saved data always hot*/
    /* Deprecated method from UI 1st version
    val allListData : Flow<Result<List<TaskModel>>> get() = repository.observeTasks()
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
        errorHandler{
            repository.saveTask(task)
        }
    }

    fun editTask(typeId: Int, task: TaskModel, newTask: TaskModel) {
        newTask.apply {
            id = task.id
            type_id = typeId
            dateCreated = task.dateCreated
            isFinished = task.isFinished
        }
        errorHandler{
            repository.updateTask(newTask)
        }
    }

    /*just delete*/
    fun deleteTask(task: TaskModel) {
        errorHandler{
            repository.deleteTask(task)
        }
    }

    fun updateColorValue(typeModel: TaskTypeModel, color: CardColor) {
        typeModel.color = color
        repository.updateTypeColorValue(typeModel)
    }

    /* Statistical listener*/
    val allStatisticsData: SharedFlow<TaskUiState<StatisticsModel>>
        get() =
            repository.provideStatisticsData()
                .flowOn(dispatcher.io)
                .onStart { TaskUiState<StatisticsModel>(isLoading = true) }
                .map { item -> TaskUiState(dataList = item) }
                .catch { TaskUiState<StatisticsModel>(isError = true) }
                .shareIn(viewModelScope, SharingStarted.Lazily)

    companion object {
        private const val SINGLE_DATA = 1
        private const val LIST_DATA = 2
    }
}