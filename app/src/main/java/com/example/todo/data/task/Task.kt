package com.example.todo.data.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "important")
    val important: Boolean = false,
    @ColumnInfo(name = "date")
    val termDate: Date?,
    @ColumnInfo
    val initDate: Date
)