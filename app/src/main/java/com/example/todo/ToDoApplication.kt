package com.example.todo

import android.app.Application
import com.example.todo.data.TasksDatabase

class ToDoApplication : Application() {
    val tasksDatabase: TasksDatabase
            by lazy { TasksDatabase.getDatabase(this) }
}