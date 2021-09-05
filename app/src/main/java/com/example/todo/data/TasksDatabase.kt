package com.example.todo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todo.data.task.Task
import com.example.todo.data.task.TaskDao

@TypeConverters(Converters::class)
@Database(entities = [Task::class], version = 6, exportSchema = false)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
