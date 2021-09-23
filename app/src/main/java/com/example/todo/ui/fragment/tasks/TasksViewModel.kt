package com.example.todo.ui.fragment.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todo.data.AppRepository
import com.example.todo.data.PreferencesManager
import com.example.todo.data.SortOrder
import com.example.todo.data.task.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


class TasksViewModel
@Inject
constructor(
    private val repository: AppRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    val searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val preferencesFlow = preferencesManager.preferencesFlow

    private var _tasks = combine(
        searchQuery,
        preferencesFlow
    ) { searchQuery, filterPreferences ->
        Pair(searchQuery, filterPreferences)
    }.flatMapLatest { (searchQuery, filterPreferences) ->
        repository.allTasks(
            searchQuery,
            filterPreferences.sortOrder,
            filterPreferences.anchorImportant
        )
    }.asLiveData()

    val tasks get() = _tasks

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onAnchorImportantClick(isImportant: Boolean) {
        viewModelScope.launch { preferencesManager.updateAnchorImportant(isImportant) }
    }

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