package com.daniza.simple.todolist

import android.content.Context
import androidx.room.Room
import com.daniza.simple.todolist.data.TodoRepository
import com.daniza.simple.todolist.data.local.TodoDatabase
import com.daniza.simple.todolist.data.local.TodoDatabase.Companion.MIGRATION_2_3
import com.daniza.simple.todolist.data.local.task.TaskDao
import com.daniza.simple.todolist.data.local.type.TypeDao

object ServiceLocator {
    @Volatile
    private var database: TodoDatabase? = null

    private var repository: TodoRepository? = null

    fun provideTodoRepository(context: Context): TodoRepository {
        synchronized(this){
            return repository ?: createTodoRepository(context)
        }
    }

    private fun createTodoRepository(context: Context) : TodoRepository{
        val newRepo =TodoRepository(
            createTaskDao(context),
            createTypeTaskDao(context)
        )

        repository = newRepo
        return newRepo
    }

    private fun createTypeTaskDao(context: Context): TypeDao {
        val database = database ?: createTodoDatabase(context)
        return database.typeDao()
    }

    private fun createTaskDao(context: Context): TaskDao{
        val database = database ?: createTodoDatabase(context)
        return database.taskDao()
    }

    private fun createTodoDatabase(context: Context) : TodoDatabase{
        return database?: synchronized(this){
            val result = Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java,
                TodoDatabase.DB_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
            database = result
            return result
        }
    }
}