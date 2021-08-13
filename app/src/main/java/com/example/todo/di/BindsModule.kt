package com.example.todo.di

import com.example.todo.data.AppRepository
import com.example.todo.data.AppRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BindsModule {
    @Binds
    fun provideAppRepositoryImpl(repositoryImpl: AppRepositoryImpl): AppRepository
}