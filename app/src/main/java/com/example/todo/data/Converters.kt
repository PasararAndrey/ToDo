package com.example.todo.data

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun dateToLong(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun dateFromLong(value: Long?): Date? {
        return value?.let { Date(it) }
    }
}