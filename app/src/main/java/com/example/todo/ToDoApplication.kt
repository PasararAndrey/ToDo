package com.example.todo

import android.app.Application
import com.example.todo.data.TasksDatabase
import com.example.todo.data.AppRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ToDoApplication : Application() {
}

