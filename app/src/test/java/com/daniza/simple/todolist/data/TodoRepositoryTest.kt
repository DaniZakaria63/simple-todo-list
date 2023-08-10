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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify


/*
* All the testing methods are that needed for view model
* */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class TodoRepositoryTest {
    private val dummyTaskList = List(3) { index ->
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
    * Expected: Handling error to be wrapped in Result.Error
    * Testing: Do mock to fakeTypeDao and check for the result
    * */
    @Test
    fun todoRepository_retrieveAll_returnError() = runTest {
        // Given spy to type object
        val spyType = spy(fakeTypeDao)
        doReturn(flow<Map<TaskTypeEntity, List<TaskEntity>>>{
            throw NoSuchElementException("No data provided")
        }).`when`(spyType).findAllWithTask()

        // When that custom error actual happened
        repository = TodoRepository(fakeTaskDao, spyType, UnconfinedTestDispatcher())
        repository.observeTypes().collect {

            // Then check for the handler
            assertThat(it, equalTo(
                Result.Error(NoSuchElementException("No data provided"))
            ))
        }
        verify(spyType).findAllWithTask()
    }


    /*
    * Expected: All type data with its tasks list
    * Testing: Retrieve all type data, then check the task list value
    * */
    @Test
    fun todoRepository_getTypeAndTask_checkBothValue() = runTest {
        // When type and tasks observed
        repository.observeTypes().collect { it ->

            // Then check for all type, and for each task list
            val typeTasks = if (it is Result.Success) it.data else null

            println("Size ${typeTasks?.size}")

            assertThat(typeTasks, notNullValue()) // typeTask not null
            assertThat(typeTasks?.count(), equalTo(2)) // typeTask size should be 2
            assertThat(
                typeTasks?.find { it.id == 1 }?._task_list?.count(),
                equalTo(2)
            ) // first task size of typeTask should be 2

            println("Size2 ${typeTasks?.size}")
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
        repository.getTaskTypeOne(newData.id).collect {
            val data = it

            assertThat(data, notNullValue())
            assertThat(data, equalTo(Result.Success(newData)))
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
        oneData.apply { this.isFinished = true }

        // When update task entity
        repository.updateTask(oneData)

        // Then check for updated value
        repository.getTask(oneData.id).test {
            val getData = awaitItem()
            val data = if(getData is Result.Success) getData.data else null

            delay(500)
            assertNotNull(data)
            assertThat(data?.id, `is`(oneData.id))
            assertThat(data?.checked, `is`(true))

            awaitComplete()
        }

        // When try to update again
        repository.updateTask(oneData.apply { this.isFinished = false })

        // Then check once again
        repository.getTask(oneData.id).test {
            val getData = awaitItem()
            val data = if(getData is Result.Success) getData.data else null

            assertNotNull(data)
            assertThat(data?.id, `is`(oneData.id))
            assertThat(data?.checked, `is`(false))

            awaitComplete()
        }
    }


    /*
    * Expected: Success delete item
    * Testing: Delete one item from the list, and do check once again
    * */
    @Test
    fun todoRepository_taskList_deleteTask_returnNoElement() = runTest {
        // Given one sample data
        val oneData = dummyTaskList.random().asDomainModel()

        // When deleted
        repository.deleteTask(oneData)

        // Then response is error because return no element
        repository.getTask(oneData.id).test {
            val exception = awaitError()
            assertThat(exception, instanceOf(NoSuchElementException::class.java))
        }
    }


    /*
    * Expected: Task type/category got deleted
    * Testing: Delete the type and check for the data once again
    * */
    @Test
    fun todoRepository_typeList_deleteType_returnNoElement() = runTest {
        // Given one sample data
        val oneData = dummyTypeList.random().toDomainModel()

        // When deleted
        repository.deleteTaskType(oneData)

        // Then response error while searching for it
        repository.getTaskTypeOne(oneData.id).collect { errorData ->

            val data = if(errorData is Result.Error) errorData.exception else null

            assertNotNull(data)
            assertThat(data, instanceOf(NoSuchElementException::class.java))
            assertThat(errorData, equalTo(Result.Error(NoSuchElementException("Unknown Category"))))


        }
    }


    /*
    * Expected: Changing color from Green to Black
    * Testing: Give new data with green color value, save it, and update with check its value
    * */
    @Test
    fun todoRepository_typeList_changeColor() = runTest {
        // Given one sample data
        val oneData = TaskTypeModel(
            id = 99,
            name = "lorem ipsum i guess",
            color = CardColor.DARK_GREEN
        )

        // When adding the sample data
        repository.saveTaskType(oneData)
        repository.getTaskTypeOne(oneData.id).collect{
            assertThat(it, equalTo(Result.Success(oneData)))
        }

        // Then update the color value and check for updated
        oneData.color = CardColor.BLACK
        repository.updateTypeColorValue(oneData)
        repository.getTaskTypeOne(oneData.id).collect{
            val data = if(it is Result.Success) it.data else null
            assertNotNull(data)
            assertThat(data?.color, equalTo(CardColor.BLACK))
        }
    }


    // Just extension
    private fun TaskTypeEntity.toDomainModel() : TaskTypeModel {
        return TaskTypeModel(id, name, color, date_created)
    }
}