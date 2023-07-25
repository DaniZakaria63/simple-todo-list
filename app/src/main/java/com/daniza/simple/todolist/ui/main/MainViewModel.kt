package com.daniza.simple.todolist.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.daniza.simple.todolist.TodoApplication
import com.daniza.simple.todolist.data.TodoRepository
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.source.Result
import kotlinx.coroutines.flow.Flow

/*Several Todos:
* - for testing purpose, use FakeTodoRepository and FakeDataSource
* - making sure all the test phase got passed
* */
class MainViewModel(
    private val repository: TodoRepository
) : ViewModel() {

    /*its okay to make all saved data always hot*/
    val allListData : Flow<Result<List<TaskModel>>> get() = repository.observeTasks()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application: TodoApplication =(this[APPLICATION_KEY]) as TodoApplication
                MainViewModel(application.repository)
            }
        }
    }
}