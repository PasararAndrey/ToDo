package com.example.todo

import android.app.Application
import com.example.todo.data.TasksDatabase
import com.example.todo.data.AppRepository

class ToDoApplication : Application() {
    private val tasksDatabase: TasksDatabase
            by lazy { TasksDatabase.getDatabase(this) }
    val repository by lazy { AppRepository(tasksDatabase.taskDao()) }
}