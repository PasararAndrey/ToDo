package com.example.todo.di

import android.content.Context
import androidx.room.Room
import com.example.todo.data.TasksDatabase
import com.example.todo.data.task.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDao(tasksDatabase: TasksDatabase): TaskDao = tasksDatabase.taskDao()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): TasksDatabase =
        Room.databaseBuilder(appContext, TasksDatabase::class.java, "tasks.db")
            .fallbackToDestructiveMigration().build()

}

