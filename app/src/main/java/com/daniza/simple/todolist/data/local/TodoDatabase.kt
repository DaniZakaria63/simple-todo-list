package com.daniza.simple.todolist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.daniza.simple.todolist.data.local.task.TaskDao
import com.daniza.simple.todolist.data.local.task.TaskEntity
import com.daniza.simple.todolist.data.local.type.TaskTypeEntity
import com.daniza.simple.todolist.data.local.type.TypeDao
import com.daniza.simple.todolist.data.source.DBColorConverter

@Database(
    entities = [TaskEntity::class, TaskTypeEntity::class],
    exportSchema = false,
    version = 3
)
@TypeConverters(DBColorConverter::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun taskDao() : TaskDao
    abstract fun typeDao() : TypeDao

    companion object{
        const val DB_NAME = "todo_list"
        val MIGRATION_2_3 = object: Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE task_type ADD COLUMN color INTEGER")
            }
        }
    }
}