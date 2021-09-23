package com.example.todo.ui.fragment.addtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todo.data.AppRepository
import com.example.todo.data.task.Task
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class AddEditTaskViewModel @Inject constructor(private val repository: AppRepository) :
    ViewModel() {

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