package com.example.todo.data.task

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow


class AppRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    @WorkerThread
    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    @WorkerThread
    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
}