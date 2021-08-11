package com.example.todo.ui.fragment.tasks

import androidx.lifecycle.*
import com.example.todo.data.AppRepository
import com.example.todo.data.task.Task
import kotlinx.coroutines.launch

class TasksViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private var _tasks = repository.allTasks().asLiveData()
    val tasks get() = _tasks

    fun insertTask(
        title: String,
        description: String
    ) {
        val insertedTask = getInsertedTaskFromInput(title, description)
        insertTask(insertedTask)
    }

    private fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
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

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task = task)
        }
    }

    fun getTask(id: Int): LiveData<Task> {
        return repository.getTask(id).asLiveData()
    }

    fun updateTask(
        taskId: Int,
        taskTitle: String,
        taskDesc: String
    ) {
        val updatedTask = getUpdatedTaskFromInput(taskId, taskTitle, taskDesc)
        updateTask(updatedTask)
    }

    private fun updateTask(updatedTask: Task) {
        viewModelScope.launch {
            repository.updateTask(updatedTask)
        }
    }
    private fun getUpdatedTaskFromInput(
        taskId: Int,
        taskTitle: String,
        taskDesc: String
    ): Task {
        return Task(
            id = taskId,
            title = taskTitle,
            description = taskDesc
        )
    }
}

class TasksViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            return TasksViewModel(repository = repository) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }

}