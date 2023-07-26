package com.daniza.simple.todolist.data

import com.daniza.simple.todolist.MainCoroutineRule
import com.daniza.simple.todolist.data.local.task.TaskEntity
import com.daniza.simple.todolist.data.model.TaskModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.createTestCoroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/* Need more information about testing Flow
* */
@ExperimentalCoroutinesApi
internal class TodoRepositoryTest {
    private val dummyList = List<TaskEntity>(3) { index ->
        TaskEntity(
            index,
            DummyData.randomName(),
            DummyData.randomNameLong(),
            "2020-2-20",
            false,
            ""
        )
    }
    private lateinit var fakeDao: FakeTaskDataSource
    private lateinit var repository: TodoRepository


    // Initialize Coroutine Scope of Testing
    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    @Before
    fun setUp() {
        // Global Given
        fakeDao = FakeTaskDataSource(dummyList.toMutableList())
        repository =
            TodoRepository(fakeDao, createTestCoroutineScope(), mainDispatcherRule.testDispatcher)

    }

    @Test
    fun todoRepository_retrieveAll_countAll() = runTest {
        // When the repository retrieve value
        val allData = repository.observeTasks()

        // Then check expected item count
//        assertThat()
    }

    @Test
    fun todoRepository_getTask_showLast() = runTest {
        // Given the new task object
        val task = TaskModel(id = 1945)

        // When data added
        repository.saveTask(task)

        // Then check last repository data
//        assertThat(repository.getTask(task.id))
    }
}