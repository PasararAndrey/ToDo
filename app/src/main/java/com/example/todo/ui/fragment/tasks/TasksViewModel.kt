package com.example.todo.ui.fragment.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todo.data.AppRepository
import com.example.todo.data.task.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


enum class SortOrder {
    BY_NAME, BY_CREATION_DATE, BY_DEADLINE_DATE
}

class TasksViewModel
@Inject
constructor(
    private val repository: AppRepository,
) : ViewModel() {
    val searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val sortOrder = MutableStateFlow(SortOrder.BY_CREATION_DATE)

    val anchorImportant = MutableStateFlow(false)
    private var _tasks = combine(
            searchQuery,
            sortOrder,
            anchorImportant
        ){ searchQuery,sortOrder, anchorImportant ->
            Triple(searchQuery,sortOrder,anchorImportant)
        }.flatMapLatest { (searchQuery,sortOrder, anchorImportant)->
                repository.allTasks(searchQuery, sortOrder, anchorImportant)
    }.asLiveData()
    val tasks get() = _tasks

    fun insertTask(
        title: String,
        important: Boolean,
        termDate: Date?,
        initDate: Date
    ) {

        val insertedTask = getInsertedTaskFromInput(title, important, termDate, initDate)
        insertTask(insertedTask)
    }

    private fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    private fun getInsertedTaskFromInput(
        title: String,
        important: Boolean,
        date: Date?,
        initDate: Date
    ): Task {
        return Task(title = title, important = important, termDate = date, initDate = initDate)
    }

    fun isEntryValid(title: String): Boolean {
        if (title.isBlank()) {
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
        id: Int,
        title: String, important: Boolean, termDate: Date?, initDate: Date
    ) {
        val updatedTask = getUpdatedTaskFromInput(id, title, important, termDate, initDate)
        updateTask(updatedTask)
    }

    private fun updateTask(updatedTask: Task) {
        viewModelScope.launch {
            repository.updateTask(updatedTask)
        }
    }

    private fun getUpdatedTaskFromInput(
        id: Int,
        title: String, important: Boolean, termDate: Date?, initDate: Date
    ): Task {
        return Task(
            id, title, important, termDate, initDate
        )
    }
}