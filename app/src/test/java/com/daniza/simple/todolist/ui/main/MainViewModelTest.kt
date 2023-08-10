package com.daniza.simple.todolist.ui.main

import app.cash.turbine.test
import app.cash.turbine.testIn
import app.cash.turbine.turbineScope
import com.daniza.simple.todolist.MainCoroutineRule
import com.daniza.simple.todolist.data.TodoRepository
import com.daniza.simple.todolist.data.model.StatisticsModel
import com.daniza.simple.todolist.data.model.TaskAnalyticModel
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.model.TaskTypeModel
import com.daniza.simple.todolist.data.source.Result
import com.daniza.simple.todolist.data.source.TaskUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.isA
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class MainViewModelTest {
    private lateinit var dispatcher: TestCoroutineDispatcher
    private lateinit var mainViewModel: MainViewModel

    @Mock
    private lateinit var repository: TodoRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() = runTest {
        doReturn(emptyFlow<Result.Loading>()).`when`(repository).observeTypes()
        dispatcher = TestCoroutineDispatcher()
        mainViewModel = MainViewModel(repository, dispatcher)
    }


    /*
    * Expected: Given the TaskUiState of data from observing the repository
    * Testing: Mock repository to return value and check the list data
    * */
    @Test
    fun givenTypeTaskSuccess_whenObserve_shouldReturnSuccess() = runTest {
        // Given result success from repository
        doReturn(
            flowOf(
                Result.Success<List<TaskTypeModel>>(emptyList())
            )
        ).`when`(repository).observeTypes()

        // Need to re-init
        mainViewModel = MainViewModel(repository, dispatcher)

        // Then check for its response
        mainViewModel.allTasksTypeData.test {
            assertThat(awaitItem(), equalTo(TaskUiState(dataList = emptyList())))
            cancelAndIgnoreRemainingEvents()
        }
        verify(repository, times(2)).observeTypes()
    }


    /*
    * Expected: Given the TaskUiState error after observing the repository
    * Testing: Mock repository to throw an error then check the state
    * */
    @Test
    fun givenTypeTaskError_whenObserve_shouldReturnError() = runTest {
        doReturn(
            flowOf(
                Result.Error(NoSuchElementException("Because it was empty"))
            )
        ).`when`(repository).observeTypes()

        // Need to re-init
        mainViewModel = MainViewModel(repository, dispatcher)

        mainViewModel.allTasksTypeData.test {
            assertThat(awaitItem(), equalTo(TaskUiState(isError = true)))
            cancelAndIgnoreRemainingEvents()
        }
        verify(repository, times(2)).observeTypes()
    }


    @Test
    fun givenTypeTaskSuccess_whenGetOne_shouldReturnSuccess() = runTest {
        val oneData = TaskTypeModel(id = 0)
        doReturn(
            flowOf(
                Result.Success(oneData)
            )
        ).`when`(repository).getTaskTypeOne(any())

        mainViewModel.getOneTaskType(0)

        mainViewModel.singleTasksTypeData.test {
            assertThat(awaitItem(), equalTo(TaskUiState(dataSingle = oneData)))
            cancelAndIgnoreRemainingEvents()
        }
        verify(repository).getTaskTypeOne(any())
    }


    @Test
    fun givenTypeTaskError_whenGetOne_shouldReturnError() = runTest {
        val oneData = TaskTypeModel(id = 99)
        doReturn(
            flowOf(
                Result.Error(NoSuchElementException("No data provided"))
            )
        ).`when`(repository).getTaskTypeOne(any())

        mainViewModel.getOneTaskType(oneData.id)

        mainViewModel.singleTasksTypeData.test {
            assertThat(awaitItem(), equalTo(TaskUiState(isError = true)))
            cancelAndIgnoreRemainingEvents()
        }
        verify(repository).getTaskTypeOne(any())
    }


    @Test
    fun givenNewType_saveSuccess() = runTest {
        val oneData = TaskTypeModel(id = 11, name = "hello")
        doAnswer { args ->
            val data: TaskTypeModel = args.arguments[0] as TaskTypeModel

            assertThat(data, notNullValue())
            assertThat(data.name, not(""))
            assertThat(data.name, not("-"))

            null
        }.`when`(repository).saveTaskType(any())

        mainViewModel.saveNewTaskType(oneData)

        verify(repository, times(1)).saveTaskType(any())
    }


    @Test
    fun givenNewType_saveError() = runTest {
        val oneData = TaskTypeModel(id = 11, name = "-")
        doThrow(IllegalArgumentException("Name cannot be empty"))
            .`when`(repository).saveTaskType(any())

        mainViewModel.errorStatus.test {
            mainViewModel.saveNewTaskType(oneData)
            assertThat(awaitItem(), equalTo(true))
            cancelAndIgnoreRemainingEvents()
        }

        verify(repository, times(1)).saveTaskType(any())
    }


    @Test
    fun givenNewType_deleteType_shouldSuccess() = runTest {
        val oneData = TaskTypeModel(id = 0)
        doAnswer { args ->
            val data: TaskTypeModel = args.arguments[0] as TaskTypeModel

            assertThat(data, notNullValue())
            assertThat(data.id, instanceOf(Number::class.java))
            assertThat(data.id, equalTo(oneData.id))

            null
        }.`when`(repository).deleteTaskType(any())

        mainViewModel.deleteTaskType(oneData)

        verify(repository, times(1)).deleteTaskType(any())
    }


    @Test
    fun givenNewType_deleteType_shouldError() = runTest {
        val oneData = TaskTypeModel(id = 11)
        doThrow(IllegalArgumentException::class)
            .`when`(repository).deleteTaskType(any())

        mainViewModel.errorStatus.test {
            mainViewModel.deleteTaskType(oneData)
            assertThat(awaitItem(), equalTo(true))
            cancelAndIgnoreRemainingEvents()
        }

        verify(repository, times(1)).deleteTaskType(any())
    }


    @Test
    fun givenTask_updateChecked_shouldSuccess() = runTest {
        val oneData = TaskModel(id = 99, isFinished = false) // active task
        val check = true

        doAnswer {  args ->
            val data : TaskModel = args.arguments[0] as TaskModel

            assertThat(data, equalTo(oneData))
            assertThat(data.isFinished, `is`(check))

            null
        }.`when`(repository).updateTask(any())

        mainViewModel.updateCheckedTask(oneData, check)

        verify(repository).updateTask(any())
    }


    @Test
    fun givenTask_saveNewTask_shouldSuccess() = runTest {
        val typeId = 99
        val oneData = TaskModel(id = 11, type_id = typeId) // active task

        doAnswer {  args ->
            val data : TaskModel = args.arguments[0] as TaskModel
            assertThat(data.type_id, `is`(typeId))

            null
        }.`when`(repository).saveTask(any())

        mainViewModel.saveNewTask(typeId, oneData)

        verify(repository).saveTask(any())
    }


    @Test
    fun givenTask_saveNewTask_shouldErrorRuntime() = runTest {
        val taskId = 99
        val oneData = TaskModel(id = 11)
        doThrow(RuntimeException::class)
            .`when`(repository).saveTask(any())

        mainViewModel.errorStatus.test {
            mainViewModel.saveNewTask(taskId, oneData)
            assertThat(awaitItem(), equalTo(true))
            cancelAndIgnoreRemainingEvents()
        }

        verify(repository, times(1)).saveTask(any())
    }


    @Test
    fun givenTask_editTask_shouldSuccess() = runTest {
        val typeId = 99
        val oneData = TaskModel(id = 11, isFinished = false)
        val updateData = TaskModel(id = 11, type_id = typeId, isFinished = true)

        doAnswer { args ->
            val data : TaskModel = args.arguments[0] as TaskModel
            assertThat(data.id, `is`(updateData.id))
            assertThat(data.type_id, `is`(updateData.type_id))
            assertThat(data.isFinished, `is`(updateData.isFinished))

            null
        }.`when`(repository).updateTask(any())

        mainViewModel.editTask(typeId, oneData, updateData)

        verify(repository).updateTask(any())
    }


    @Test
    fun givenTask_deleteTask_shouldSuccess() = runTest {
        val oneData = TaskModel(id = 11)

        doAnswer { args ->
            val data : TaskModel = args.arguments[0] as TaskModel
            assertThat(data.id, `is`(oneData.id))

            null
        }.`when`(repository).deleteTask(any())

        mainViewModel.deleteTask(oneData)

        verify(repository).deleteTask(oneData)
    }


    @Ignore("Pending testing of the variable triggering")
    @Test
    fun givenTask_analytical_shouldSuccess() = runTest {
        doReturn(
            flowOf(
                StatisticsModel("total task", "", "10"),
                StatisticsModel("type one", "", "5"),
                StatisticsModel("total type", "", "2"),
            )
        ).`when`(repository).provideStatisticsData()

        mainViewModel = MainViewModel(repository, dispatcher)
        mainViewModel.allStatisticsData.test {
            assertThat(awaitItem(), equalTo(TaskUiState(isLoading = true)))
            cancelAndIgnoreRemainingEvents()
        }

        verify(repository).provideStatisticsData()
    }


    @Test
    fun dummyTest() = runTest {
        flowOf(true).test {
            assertThat(awaitItem(), `is`(true))
            awaitComplete()
            cancelAndIgnoreRemainingEvents()
        }


        val sharedFlow = MutableSharedFlow<Boolean>(replay = 0)

        sharedFlow.test {
            sharedFlow.emit(true)
            assertThat(awaitItem(), `is`(true))
            cancelAndIgnoreRemainingEvents()
        }
    }
}