package com.example.todo.data.task

import androidx.room.*
import com.example.todo.data.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    fun getTasks(
        searchQuery: String,
        sortOrder: SortOrder,
        anchorImportant: Boolean
    ): Flow<List<Task>> =
        when (sortOrder) {
            SortOrder.BY_NAME -> getTasksSortedByName(searchQuery, anchorImportant)
            SortOrder.BY_CREATION_DATE -> getTasksSortedByDateCreated(searchQuery, anchorImportant)
            SortOrder.BY_DEADLINE_DATE -> getTasksSortedByDateDeadline(searchQuery, anchorImportant)
        }

    @Query("SELECT * FROM task_table WHERE title LIKE '%' || :searchQuery || '%' ORDER BY CASE WHEN :anchorImportant = 1 THEN important END DESC, title     ")
    fun getTasksSortedByName(searchQuery: String, anchorImportant: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE title LIKE '%' || :searchQuery || '%' ORDER BY CASE WHEN :anchorImportant = 1 THEN important END DESC, initDate     ")
    fun getTasksSortedByDateCreated(searchQuery: String, anchorImportant: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE title LIKE '%' || :searchQuery || '%' ORDER BY CASE WHEN :anchorImportant = 1 THEN important END DESC,term_date")
    fun getTasksSortedByDateDeadline(
        searchQuery: String,
        anchorImportant: Boolean
    ): Flow<List<Task>>


    @Query("SELECT * FROM task_table WHERE id = :id")
    fun getTask(id: Int): Flow<Task>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

}
