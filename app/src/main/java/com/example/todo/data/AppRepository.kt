package com.example.todo.data

import androidx.annotation.WorkerThread
import com.example.todo.data.task.Task
import com.example.todo.data.task.TaskDao
import kotlinx.coroutines.flow.Flow


class AppRepository(private val taskDao: TaskDao) {
    @WorkerThread
    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    @WorkerThread
    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    @WorkerThread
    suspend fun updateTask(task: Task) {
        return taskDao.updateTask(task)
    }

    fun allTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    fun getTask(id: Int): Flow<Task> = taskDao.getTask(id)
}