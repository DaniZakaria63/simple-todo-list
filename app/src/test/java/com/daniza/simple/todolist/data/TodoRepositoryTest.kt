package com.daniza.simple.todolist.data

import app.cash.turbine.test
import com.daniza.simple.todolist.MainCoroutineRule
import com.daniza.simple.todolist.data.local.task.TaskEntity
import com.daniza.simple.todolist.data.local.task.asDomainsModel
import com.daniza.simple.todolist.data.local.type.TaskTypeEntity
import com.daniza.simple.todolist.data.model.TaskModel
import com.daniza.simple.todolist.data.model.TaskTypeModel
import com.daniza.simple.todolist.data.source.Result
import com.daniza.simple.todolist.ui.theme.CardColor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.isActive
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.createTestCoroutineScope
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.sameInstance
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.isNotNull


/*
* All the testing methods are that needed for view model
* */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class TodoRepositoryTest {
    private val dummyTaskList = List<TaskEntity>(3) { index ->
        TaskEntity(
            index,
            if (index % 2 == 0) 1 else 2,
            DummyData.randomName(),
            DummyData.randomNameLong(),
            "2020-2-20",
            false,
            ""
        )
    }
    private val dummyTypeList = listOf(
        TaskTypeEntity(1, "one", CardColor.GREY, ""),
        TaskTypeEntity(2, "two", CardColor.BLACK, ""),
    )
    private lateinit var fakeTaskDao: FakeTaskDataSource
    private lateinit var fakeTypeDao: FakeTypeDataSource
    private lateinit var repository: TodoRepository

    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()


    /*
    * Setting up the test module
    * Using fake data for testing TodoRepository
    * Fake data source represented of how room's work
    * */
    @Before
    fun setUp() {
        // Global Given
        fakeTaskDao = FakeTaskDataSource(dummyTaskList.toMutableList())
        fakeTypeDao = FakeTypeDataSource(dummyTypeList.toMutableList(), fakeTaskDao)
        repository =
            TodoRepository(fakeTaskDao, fakeTypeDao, UnconfinedTestDispatcher())
    }


    /*
    * Expected: Retrieve all data that equal to dummyTaskList
    * Test: checking all task data as domain model
    * */
    @Test
    fun todoRepository_retrieveAll_checkAll() = runTest {
        // When the repository retrieve value
        repository.observeTasks().test {

            // Then check expected item list
            assertThat(awaitItem(), equalTo(Result.Success(dummyTaskList.asDomainsModel())))
            awaitComplete()
        }
    }


    /*
    * Expected: Empty data, return zero value of the list
    * Testing: Retrieve all with empty data without any error
    * */
    @Test
    fun todoRepository_retrieveAll_emptyListNoError() = runTest {
        // Given empty source to repository
        fakeTaskDao.deleteAll()
        repository = TodoRepository(fakeTaskDao, fakeTypeDao)

        // When the repository retrieve all value
        repository.observeTasks().test {

            //Then check expected empty list
            assertThat(awaitItem(), equalTo(Result.Success(listOf())))
            awaitComplete()
        }
    }


    /*
    * Expected: All type data with its tasks list
    * Testing: Retrieve all type data, then check the task list value
    * */
    @Test
    fun todoRepository_getTypeAndTask_checkBothValue() = runTest {
        // When type and tasks observed
        repository.observeTypes().collect {

            // Then check for all type, and for each task list
            val response = it
            val typeTasks = if (response is Result.Success) response.data else null

            delay(1_000)
            System.out.println("Size ${typeTasks?.size}")

            assertThat(typeTasks, notNullValue()) // typeTask not null
            assertThat(typeTasks?.count(), equalTo(2)) // typeTask size should be 2
            assertThat(
                typeTasks?.find { it.id == 1 }?._task_list?.count(),
                equalTo(2)
            ) // first task size of typeTask should be 2

            System.out.println("Size2 ${typeTasks?.size}")
        }
    }


    /*
    * Expected: Add new record into tasks list
    * Testing: Give new dummy object into fake dao through repository
    * */
    @Test
    fun todoRepository_newTypeTask_addTypeSuccess() = runTest {
        // Given dummy data of type task
        val newData = TaskTypeModel(3, "dummy")

        // When add data
        repository.saveTaskType(newData)

        // Then check the value
        repository.getTaskTypeOne(newData.id).test {
            val data = awaitItem()

            assertThat(data, notNullValue())
            assertThat(data, equalTo(Result.Success(newData)))
            awaitComplete()
        }
    }


    /*
    * Expected: Throw an error because of empty title
    * Testing: Give an object with empty value for the title
    * */
    @Test
    fun todoRepository_newTypeTask_addTypeError_emptyTitle() = runTest {
        // Given dummy data with no title
        val newData = TaskTypeModel(id = 3, "-")

        // When add data
        val exception = assertThrows(IllegalArgumentException::class.java){
            repository.saveTaskType(newData)
        }

        // Then
        assertThat("Category name cannot be empty", `is`(exception.message))
    }


    /*
    * Expected: Task can be checked and unchecked
    * Testing: Set state for active into true and false
    * */
    @Test
    fun todoRepository_taskList_activeAndInactive() = runTest{
        // Given one single data from dummy
        val oneData: TaskModel = dummyTaskList.random().asDomainModel()
        oneData.apply { this.checked = true }

        // When update task entity
        repository.updateTask(oneData)

        // Then check for updated value
        repository.getTask(oneData.id).test {
            val getData = awaitItem()
            val data = if(getData is Result.Success) getData.data else null

            assertThat(data, isNotNull())
            assertThat(data?.id, `is`(oneData.id))
            assertThat(data?.checked, `is`(true))

            awaitComplete()
        }

        delay(5_000)

        // When try to update again
        repository.updateTask(oneData.apply { this.checked = false })

        // Then check once again
        repository.getTask(oneData.id).test {
            val getData = awaitItem()
            val data = if(getData is Result.Success) getData.data else null

            assertThat(data, isNotNull())
            assertThat(data?.id, `is`(oneData.id))
            assertThat(data?.checked, `is`(false))

            awaitComplete()
        }
    }
}