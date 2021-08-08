package com.example.todo.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.todo.data.task.Task
import com.example.todo.data.task.TaskDao
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


    fun getTask(id: Int): Flow<Task> {
        return taskDao.getTask(id)
    }
}