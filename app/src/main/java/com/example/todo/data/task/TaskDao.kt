package com.example.todo.data.task

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM Task")
    suspend fun getTasks(): List<Task>

    @Delete
    suspend fun deleteTask(task: Task)

}