package com.example.todo.ui.fragment.addtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todo.data.task.Task
import com.example.todo.data.task.TaskDao
import kotlinx.coroutines.launch

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {
    fun insertTask(
        title: String,
        description: String
    ) {
        val insertedTask = getInsertedTaskFromInput(title, description)
        insertTask(insertedTask)
    }

    private fun insertTask(task: Task) {
        viewModelScope.launch {
            taskDao.insertTask(task)
        }
    }

    private fun getInsertedTaskFromInput(title: String, description: String): Task {
        return Task(title = title, description = description)
    }

    fun isEntryValid(title: String, description: String): Boolean {
        if (title.isBlank() || description.isBlank()) {
            return false
        }
        return true
    }
}

class TaskViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskDao) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }

}