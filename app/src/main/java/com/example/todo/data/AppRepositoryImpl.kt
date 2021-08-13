package com.example.todo.data

import androidx.annotation.WorkerThread
import com.example.todo.data.task.Task
import com.example.todo.data.task.TaskDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppRepositoryImpl @Inject constructor(private val taskDao: TaskDao) :
    AppRepository {
    @WorkerThread
    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }
    @WorkerThread
    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
    @WorkerThread
    override suspend fun updateTask(task: Task) {
        return taskDao.updateTask(task)
    }
    override fun allTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    override fun getTask(id: Int): Flow<Task> = taskDao.getTask(id)
}