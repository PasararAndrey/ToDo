package com.example.todo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todo.data.task.Task
import com.example.todo.data.task.TaskDao

@Database(entities = [Task::class], version = 1)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TasksDatabase? = null
        private const val DB_NAME = "tasks.db"

        fun getDatabase(context: Context): TasksDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    TasksDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                instance
            }

        }

    }
}
