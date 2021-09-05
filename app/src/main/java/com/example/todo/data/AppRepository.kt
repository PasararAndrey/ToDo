package com.example.todo.data

import com.example.todo.data.task.Task
import com.example.todo.ui.fragment.tasks.SortOrder
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun insertTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun updateTask(task: Task)
    fun getTask(id: Int): Flow<Task>
    fun allTasks(searchQuery: String, sortOrder: SortOrder, anchorImportant: Boolean): Flow<List<Task>>
}