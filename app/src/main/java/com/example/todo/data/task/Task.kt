package com.example.todo.data.task

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Parcelize
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
//    @ColumnInfo(name = "completed")
//    val completed: Boolean,
//    @ColumnInfo(name = "created")
//    val created: Long = System.currentTimeMillis()
) : Parcelable {
//    val createdDataFormat: String
//        get() = DateFormat.getDateTimeInstance().format(created)
}