package com.example.todo.ui.fragment.tasks

import androidx.lifecycle.*
import com.example.todo.data.AppRepository
import com.example.todo.data.PreferencesManager
import com.example.todo.data.SortOrder
import com.example.todo.data.task.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel
@Inject
constructor(
    private val repository: AppRepository,
    private val preferencesManager: PreferencesManager,
    state: SavedStateHandle,
) : ViewModel() {
    val searchQuery = state.getLiveData("searchQuery", "")
    val preferencesFlow = preferencesManager.preferencesFlow

    private val _taskEvent = MutableSharedFlow<TasksEvent>()
    val taskEvent = _taskEvent.asSharedFlow()

    private var _tasks = combine(
        searchQuery.asFlow(),
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

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
        _taskEvent.emit(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }

    fun onUndoDeleteClick(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
    }


    sealed class TasksEvent {
        class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
    }

}


