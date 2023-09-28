package com.daniza.simple.todolist

import android.content.Context
import androidx.room.Room
import com.daniza.simple.todolist.data.TodoRepository
import com.daniza.simple.todolist.data.local.TodoDatabase
import com.daniza.simple.todolist.data.source.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {
    @Provides
    fun provideTaskRepository(todoDatabase: TodoDatabase, dispatcherProvider: DispatcherProvider): TaskRepository{
        return TodoRepository(
            todoDatabase.taskDao(),
            todoDatabase.typeDao(),
            dispatcherProvider
        )
    }

    @Provides
    fun provideTodoDatabase(@ApplicationContext context: Context): TodoDatabase{
        return Room.databaseBuilder(
            context.applicationContext,
            TodoDatabase::class.java,
            TodoDatabase.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideDispatcherProvider(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }
}